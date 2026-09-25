package vswe.stevescarts.api.carts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import vswe.stevescarts.api.events.CartEvents;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModAttachments;
import vswe.stevescarts.internal.MinecartLinkData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/** Shared persistent link graph and movement handling for every minecart type. */
public final class CartTrainLinker {
    private static final String LINK_COUNT = "stevescarts_link_count";
    private static final String LINK_PREFIX = "stevescarts_link_";
    private static final double LINK_DISTANCE = 1.6D;
    private static final double MAX_LINK_DISTANCE = 2.25D;
    private static final double MAX_CATCH_UP_SPEED = 0.35D;
    private static final int MAX_RAIL_PATH_NODES = 128;
    private static final Map<AbstractMinecart, Long> LAST_TICK = new WeakHashMap<>();
    private static final Map<AbstractMinecart, Long> LAST_SOLVED = new WeakHashMap<>();

    private CartTrainLinker() {
    }

    public static CartLinkResult link(AbstractMinecart first, AbstractMinecart second) {
        if (first == null || second == null || first == second || !first.isAlive() || !second.isAlive()) {
            return CartLinkResult.INVALID_CART;
        }
        if (first.level() != second.level()) {
            return CartLinkResult.DIFFERENT_LEVEL;
        }
        if (!(first.level() instanceof ServerLevel)) {
            return CartLinkResult.INVALID_CART;
        }
        if (isLinked(first, second) || isConnected(first, second)) {
            return CartLinkResult.ALREADY_CONNECTED;
        }
        List<UUID> firstLinks = new ArrayList<>(getLinkedCartIds(first));
        List<UUID> secondLinks = new ArrayList<>(getLinkedCartIds(second));
        if (firstLinks.size() >= CartTrain.MAX_DIRECT_LINKS || secondLinks.size() >= CartTrain.MAX_DIRECT_LINKS) {
            return CartLinkResult.NO_FREE_LINK;
        }
        if (getTrainSize(first) + getTrainSize(second) > CartTrain.MAX_TRAIN_CARTS) {
            return CartLinkResult.TRAIN_FULL;
        }

        CartEvents.CartLinkEvent event = new CartEvents.CartLinkEvent(first, second);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return CartLinkResult.CANCELLED;
        }

        firstLinks.add(second.getUUID());
        secondLinks.add(first.getUUID());
        setLinkedCartIds(first, firstLinks);
        setLinkedCartIds(second, secondLinks);
        syncDirectLinks(first);
        syncDirectLinks(second);
        return CartLinkResult.LINKED;
    }

    public static boolean unlink(AbstractMinecart first, AbstractMinecart second) {
        if (first == null || second == null || first == second || first.level() != second.level()
                || first.level().isClientSide()) {
            return false;
        }
        List<UUID> firstLinks = new ArrayList<>(getLinkedCartIds(first));
        List<UUID> secondLinks = new ArrayList<>(getLinkedCartIds(second));
        boolean removed = firstLinks.remove(second.getUUID());
        boolean removedOther = secondLinks.remove(first.getUUID());
        if (!removed && !removedOther) {
            return false;
        }
        setLinkedCartIds(first, firstLinks);
        setLinkedCartIds(second, secondLinks);
        syncDirectLinks(first);
        syncDirectLinks(second);
        NeoForge.EVENT_BUS.post(new CartEvents.CartUnlinkEvent(first, second));
        return true;
    }

    public static int unlinkAll(AbstractMinecart cart) {
        List<UUID> links = getLinkedCartIds(cart);
        int linkCount = links.size();
        if (cart.level() instanceof ServerLevel serverLevel) {
            for (UUID linkedId : links) {
                Entity entity = serverLevel.getEntity(linkedId);
                if (entity instanceof AbstractMinecart linkedCart) {
                    unlink(cart, linkedCart);
                }
            }
        }
        setLinkedCartIds(cart, List.of());
        syncDirectLinks(cart);
        return linkCount;
    }

    public static boolean isLinked(AbstractMinecart first, AbstractMinecart second) {
        return second != null && getLinkedCartIds(first).contains(second.getUUID());
    }

    public static boolean isConnected(AbstractMinecart first, AbstractMinecart second) {
        return second != null && getConnectedCarts(first).contains(second);
    }

    public static List<UUID> getLinkedCartIds(AbstractMinecart cart) {
        CompoundTag data = cart.getPersistentData();
        int count = Math.min(data.getIntOr(LINK_COUNT, 0), CartTrain.MAX_DIRECT_LINKS);
        List<UUID> links = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String value = data.getStringOr(LINK_PREFIX + i, "");
            try {
                if (!value.isEmpty()) {
                    links.add(UUID.fromString(value));
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return List.copyOf(links);
    }

    public static void setLinkedCartIds(AbstractMinecart cart, List<UUID> links) {
        CompoundTag data = cart.getPersistentData();
        int oldCount = data.getIntOr(LINK_COUNT, 0);
        for (int i = 0; i < oldCount; i++) {
            data.remove(LINK_PREFIX + i);
        }
        int count = Math.min(links.size(), CartTrain.MAX_DIRECT_LINKS);
        data.putInt(LINK_COUNT, count);
        for (int i = 0; i < count; i++) {
            data.putString(LINK_PREFIX + i, links.get(i).toString());
        }
    }

    public static List<AbstractMinecart> getDirectlyLinkedCarts(AbstractMinecart cart) {
        if (!(cart.level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        return getLinkedCartIds(cart).stream()
                .map(serverLevel::getEntity)
                .filter(AbstractMinecart.class::isInstance)
                .map(AbstractMinecart.class::cast)
                .filter(Entity::isAlive)
                .toList();
    }

    public static List<AbstractMinecart> getConnectedCarts(AbstractMinecart cart) {
        if (!(cart.level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        List<AbstractMinecart> connected = new ArrayList<>();
        Set<UUID> visited = new HashSet<>();
        ArrayDeque<UUID> pending = new ArrayDeque<>(getLinkedCartIds(cart));
        visited.add(cart.getUUID());
        while (!pending.isEmpty()) {
            UUID id = pending.removeFirst();
            if (!visited.add(id)) {
                continue;
            }
            Entity entity = serverLevel.getEntity(id);
            if (entity instanceof AbstractMinecart linkedCart && linkedCart.isAlive()) {
                connected.add(linkedCart);
                pending.addAll(getLinkedCartIds(linkedCart));
            }
        }
        return List.copyOf(connected);
    }

    public static List<AbstractMinecart> getTrainCarts(AbstractMinecart cart) {
        List<AbstractMinecart> train = new ArrayList<>(getConnectedCarts(cart));
        train.add(cart);
        train.sort(Comparator.comparingInt(Entity::getId));
        return List.copyOf(train);
    }

    public static int getTrainSize(AbstractMinecart cart) {
        return getConnectedCarts(cart).size() + 1;
    }

    public static void tick(AbstractMinecart cart) {
        if (!(cart.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        long gameTime = serverLevel.getGameTime();
        LAST_TICK.put(cart, gameTime);
        syncDirectLinks(cart);

        List<AbstractMinecart> train = getTrainCarts(cart);
        if (train.size() < 2 || train.stream().anyMatch(member -> LAST_TICK.getOrDefault(member, Long.MIN_VALUE) != gameTime)) {
            return;
        }
        AbstractMinecart leader = train.getFirst();
        if (LAST_SOLVED.getOrDefault(leader, Long.MIN_VALUE) == gameTime) {
            return;
        }
        LAST_SOLVED.put(leader, gameTime);
        synchronizeCreativeTrainSpeed(serverLevel, train);
        applyLinkConstraints(train);
        for (AbstractMinecart member : train) {
            syncDirectLinks(member);
            if (member instanceof ModularMinecart modularCart) {
                modularCart.syncTrainCartIds(train);
            }
        }
    }

    public static void onCartDestroyed(AbstractMinecart cart) {
        if (!(cart.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        int links = unlinkAll(cart);
        if (links > 0 && serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
            cart.spawnAtLocation(serverLevel, new ItemStack(Items.IRON_CHAIN, links));
        }
    }

    private static void syncDirectLinks(AbstractMinecart cart) {
        if (!(cart.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        List<UUID> links = getLinkedCartIds(cart);
        Entity first = links.isEmpty() ? null : serverLevel.getEntity(links.get(0));
        Entity second = links.size() < 2 ? null : serverLevel.getEntity(links.get(1));
        MinecartLinkData updated = new MinecartLinkData(
                first instanceof AbstractMinecart ? first.getId() : -1,
                second instanceof AbstractMinecart ? second.getId() : -1
        );
        MinecartLinkData current = cart.getExistingData(ModAttachments.MINECART_LINKS).orElse(MinecartLinkData.EMPTY);
        if (!updated.equals(current)) {
            cart.setData(ModAttachments.MINECART_LINKS, updated);
        }
    }

    public static int getLinkedCartEntityId(AbstractMinecart cart, int index) {
        if (index < 0 || index >= CartTrain.MAX_DIRECT_LINKS) {
            throw new IndexOutOfBoundsException("A cart only has " + CartTrain.MAX_DIRECT_LINKS + " direct link slots");
        }
        return cart.getExistingData(ModAttachments.MINECART_LINKS).orElse(MinecartLinkData.EMPTY).get(index);
    }

    private static void synchronizeCreativeTrainSpeed(ServerLevel level, List<AbstractMinecart> train) {
        List<ModuleBase> modules = train.stream()
                .filter(ModularMinecart.class::isInstance)
                .map(ModularMinecart.class::cast)
                .flatMap(cart -> cart.modules().stream())
                .toList();
        boolean poweredByCreativeEngine = modules.stream()
                .filter(ModuleBase::usesExperimentalMaxMinecartSpeed)
                .anyMatch(module -> module.getCart().isEngineBurning());
        if (!poweredByCreativeEngine) {
            return;
        }

        double targetSpeed = level.getGameRules().get(GameRules.MAX_MINECART_SPEED) / 20.0D;
        double moduleLimit = modules.stream()
                .mapToDouble(ModuleBase::getMaxSpeed)
                .filter(speed -> speed < ModuleBase.DEFAULT_MAX_SPEED)
                .min()
                .orElse(targetSpeed);
        targetSpeed = Math.min(targetSpeed, moduleLimit);

        for (AbstractMinecart member : train) {
            Vec3 movement = member.getDeltaMovement();
            Vec3 horizontal = movement.horizontal();
            if (targetSpeed <= 0.0D) {
                member.setDeltaMovement(0.0D, movement.y, 0.0D);
                continue;
            }
            if (horizontal.lengthSqr() < 1.0E-6D) {
                continue;
            }
            horizontal = horizontal.normalize();
            member.setDeltaMovement(horizontal.scale(targetSpeed).add(0.0D, movement.y, 0.0D));
        }
    }

    private static void applyLinkConstraints(List<AbstractMinecart> train) {
        for (AbstractMinecart cart : train) {
            for (AbstractMinecart linkedCart : getDirectlyLinkedCarts(cart)) {
                if (cart.getId() < linkedCart.getId()) {
                    applyLinkConstraint(cart, linkedCart);
                }
            }
        }
    }

    private static void applyLinkConstraint(AbstractMinecart cart, AbstractMinecart linkedCart) {
        if (!(cart.level() instanceof ServerLevel level)) {
            return;
        }
        Optional<RailPath> railPath = findRailPath(level, cart, linkedCart);
        if (railPath.isEmpty()) {
            return;
        }

        RailPath path = railPath.get();
        double error = path.distance() - LINK_DISTANCE;
        if (Math.abs(error) < 0.05D) {
            return;
        }

        AbstractMinecart leadingCart = path.secondAhead() ? linkedCart : cart;
        AbstractMinecart trailingCart = path.secondAhead() ? cart : linkedCart;
        if (error > 0.0D) {
            Vec3 trailingDirection = path.secondAhead() ? path.startDirection() : path.endDirection().reverse();
            double leadingSpeed = leadingCart.getDeltaMovement().horizontalDistance();
            double urgency = path.distance() > MAX_LINK_DISTANCE ? 0.5D : 0.25D;
            double catchUp = Math.min(MAX_CATCH_UP_SPEED, error * urgency);
            accelerateAlongRail(trailingCart, trailingDirection, leadingSpeed + catchUp);
        } else {
            slowBy(trailingCart, Math.min(MAX_CATCH_UP_SPEED, -error * 0.25D));
        }
    }

    private static void accelerateAlongRail(AbstractMinecart cart, Vec3 direction, double targetSpeed) {
        Vec3 horizontalDirection = direction.horizontal();
        if (horizontalDirection.lengthSqr() < 1.0E-6D || targetSpeed <= 0.0D) {
            return;
        }
        Vec3 movement = cart.getDeltaMovement();
        Vec3 normalizedDirection = horizontalDirection.normalize();
        double speedAlongRail = movement.horizontal().dot(normalizedDirection);
        if (speedAlongRail >= targetSpeed) {
            return;
        }
        cart.setDeltaMovement(normalizedDirection.scale(targetSpeed).add(0.0D, movement.y(), 0.0D));
    }

    private static void slowBy(AbstractMinecart cart, double amount) {
        Vec3 movement = cart.getDeltaMovement();
        double speed = movement.horizontalDistance();
        if (speed < 1.0E-6D) {
            return;
        }
        double scale = Math.max(0.0D, speed - amount) / speed;
        cart.setDeltaMovement(movement.x() * scale, movement.y(), movement.z() * scale);
    }

    private static Optional<RailPath> findRailPath(ServerLevel level, AbstractMinecart first, AbstractMinecart second) {
        BlockPos start = first.getCurrentBlockPosOrRailBelow();
        BlockPos target = second.getCurrentBlockPosOrRailBelow();
        if (!level.getBlockState(start).is(BlockTags.RAILS) || !level.getBlockState(target).is(BlockTags.RAILS)) {
            return Optional.empty();
        }

        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        Map<BlockPos, BlockPos> previous = new HashMap<>();
        pending.add(start);
        previous.put(start, start);
        while (!pending.isEmpty() && previous.size() <= MAX_RAIL_PATH_NODES) {
            BlockPos current = pending.removeFirst();
            if (current.equals(target)) {
                break;
            }
            for (BlockPos neighbor : getRailNeighbors(level, current, first)) {
                if (!previous.containsKey(neighbor)) {
                    previous.put(neighbor, current);
                    pending.addLast(neighbor);
                }
            }
        }
        if (!previous.containsKey(target)) {
            return Optional.empty();
        }

        List<BlockPos> blocks = new ArrayList<>();
        for (BlockPos current = target; !current.equals(start); current = previous.get(current)) {
            blocks.add(current);
        }
        blocks.add(start);
        Collections.reverse(blocks);

        Vec3 previousPoint = first.position();
        double distance = 0.0D;
        for (int i = 1; i < blocks.size(); i++) {
            Vec3 point = i == blocks.size() - 1 ? second.position() : Vec3.atBottomCenterOf(blocks.get(i));
            distance += point.distanceTo(previousPoint);
            previousPoint = point;
        }
        if (blocks.size() == 1) {
            distance = first.position().distanceTo(second.position());
        }

        Vec3 startDirection = blocks.size() > 1
                ? Vec3.atBottomCenterOf(blocks.get(1)).subtract(first.position()).horizontal()
                : second.position().subtract(first.position()).horizontal();
        Vec3 endDirection = blocks.size() > 1
                ? second.position().subtract(Vec3.atBottomCenterOf(blocks.get(blocks.size() - 2))).horizontal()
                : startDirection;
        double travelScore = directionScore(first, startDirection) + directionScore(second, endDirection);
        if (Math.abs(travelScore) < 1.0E-6D) {
            return Optional.empty();
        }
        return Optional.of(new RailPath(distance, travelScore > 0.0D, startDirection, endDirection));
    }

    private static double directionScore(AbstractMinecart cart, Vec3 pathDirection) {
        if (pathDirection.lengthSqr() < 1.0E-6D) {
            return 0.0D;
        }
        return cart.getDeltaMovement().horizontal().dot(pathDirection.normalize());
    }

    private static List<BlockPos> getRailNeighbors(ServerLevel level, BlockPos pos, AbstractMinecart cart) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BaseRailBlock rail)) {
            return List.of();
        }
        var exits = AbstractMinecart.exits(rail.getRailDirection(state, level, pos, cart));
        List<BlockPos> neighbors = new ArrayList<>(2);
        addRailNeighbor(level, neighbors, pos.offset(exits.getFirst()));
        addRailNeighbor(level, neighbors, pos.offset(exits.getSecond()));
        return neighbors;
    }

    private static void addRailNeighbor(ServerLevel level, List<BlockPos> neighbors, BlockPos candidate) {
        for (Vec3i offset : List.of(Vec3i.ZERO, new Vec3i(0, 1, 0), new Vec3i(0, -1, 0))) {
            BlockPos railPos = candidate.offset(offset);
            if (level.getBlockState(railPos).is(BlockTags.RAILS)) {
                neighbors.add(railPos.immutable());
                return;
            }
        }
    }

    private record RailPath(double distance, boolean secondAhead, Vec3 startDirection, Vec3 endDirection) {
    }
}
