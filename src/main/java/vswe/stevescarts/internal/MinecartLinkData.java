package vswe.stevescarts.internal;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record MinecartLinkData(int firstEntityId, int secondEntityId) {
    public static final MinecartLinkData EMPTY = new MinecartLinkData(-1, -1);
    public static final StreamCodec<RegistryFriendlyByteBuf, MinecartLinkData> STREAM_CODEC = StreamCodec.of(
            (buffer, value) -> {
                buffer.writeInt(value.firstEntityId);
                buffer.writeInt(value.secondEntityId);
            },
            buffer -> new MinecartLinkData(buffer.readInt(), buffer.readInt())
    );

    public int get(int index) {
        return switch (index) {
            case 0 -> firstEntityId;
            case 1 -> secondEntityId;
            default -> throw new IndexOutOfBoundsException("A cart only has two direct link slots");
        };
    }
}
