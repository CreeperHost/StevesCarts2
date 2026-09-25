package vswe.stevescarts.api.upgrades;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

/**
 * Immutable definition of an assembler upgrade.
 *
 * <p>The identifier is the stable identity used by new saves and registrations. The legacy numeric id is retained
 * solely so worlds saved by older versions can be migrated.</p>
 */
public final class AssemblerUpgrade {
    private final Identifier id;
    private final Integer legacyId;
    private final List<BaseUpgradeEffect> effects;

    private AssemblerUpgrade(Identifier id, Integer legacyId, List<BaseUpgradeEffect> effects) {
        this.id = Objects.requireNonNull(id, "id");
        if (legacyId != null && (legacyId < Byte.MIN_VALUE || legacyId > Byte.MAX_VALUE)) {
            throw new IllegalArgumentException("Legacy upgrade id is outside the byte range: " + legacyId);
        }
        this.legacyId = legacyId;
        this.effects = List.copyOf(effects);
    }

    public static Builder builder(Identifier id) {
        return new Builder(id);
    }

    public Identifier getId() {
        return id;
    }

    public OptionalInt getLegacyId() {
        return legacyId == null ? OptionalInt.empty() : OptionalInt.of(legacyId);
    }

    public String getName() {
        return "block." + id.getNamespace() + "." + id.getPath();
    }

    public List<BaseUpgradeEffect> getEffects() {
        return effects;
    }

    public <T extends BaseUpgradeEffect> T getEffect(Class<T> effectType) {
        for (BaseUpgradeEffect effect : effects) {
            if (effectType.isInstance(effect)) {
                return effectType.cast(effect);
            }
        }
        return null;
    }

    public void init(TileEntityUpgrade upgrade) {
        effects.forEach(effect -> effect.init(upgrade));
    }

    public void load(TileEntityUpgrade upgrade, ValueInput input) {
        effects.forEach(effect -> effect.load(upgrade, input));
    }

    public void save(TileEntityUpgrade upgrade, ValueOutput output) {
        effects.forEach(effect -> effect.save(upgrade, output));
    }

    public void update(TileEntityUpgrade upgrade) {
        effects.forEach(effect -> effect.update(upgrade));
    }

    public void removed(TileEntityUpgrade upgrade) {
        effects.forEach(effect -> effect.removed(upgrade));
    }

    public static final class Builder {
        private final Identifier id;
        private Integer legacyId;
        private final List<BaseUpgradeEffect> effects = new ArrayList<>();

        private Builder(Identifier id) {
            this.id = Objects.requireNonNull(id, "id");
        }

        public Builder legacyId(int legacyId) {
            this.legacyId = legacyId;
            return this;
        }

        public Builder addEffect(BaseUpgradeEffect effect) {
            effects.add(Objects.requireNonNull(effect, "effect"));
            return this;
        }

        public AssemblerUpgrade build() {
            return new AssemblerUpgrade(id, legacyId, effects);
        }
    }
}
