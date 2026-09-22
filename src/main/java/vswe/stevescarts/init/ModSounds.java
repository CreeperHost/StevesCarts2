package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Constants.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GEAR_SWITCH = register("gearswitch");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIN = register("win");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOP = register("boop");
    public static final DeferredHolder<SoundEvent, SoundEvent> GAME_OVER = register("gameover");
    public static final DeferredHolder<SoundEvent, SoundEvent> LINES_1 = register("1lines");
    public static final DeferredHolder<SoundEvent, SoundEvent> LINES_2 = register("2lines");
    public static final DeferredHolder<SoundEvent, SoundEvent> LINES_3 = register("3lines");
    public static final DeferredHolder<SoundEvent, SoundEvent> LINES_4 = register("4lines");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIGH_SCORE = register("highscore");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIT = register("hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> CLICK = register("click");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOB_CLICK = register("blobclick");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAG_CLICK = register("flagclick");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOOD_JOB = register("goodjob");

    private ModSounds() {
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
