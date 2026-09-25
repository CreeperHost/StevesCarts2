package vswe.stevescarts.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vswe.stevescarts.Constants;
import vswe.stevescarts.internal.MinecartLinkData;

public final class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MinecartLinkData>> MINECART_LINKS =
            ATTACHMENTS.register("minecart_links", () -> AttachmentType.builder(() -> MinecartLinkData.EMPTY)
                    .sync(MinecartLinkData.STREAM_CODEC)
                    .build());

    private ModAttachments() {
    }

    public static void init(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
    }
}
