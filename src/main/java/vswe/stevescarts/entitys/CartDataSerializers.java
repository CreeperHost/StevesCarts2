package vswe.stevescarts.entitys;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;

public class CartDataSerializers
{
    public static void init()
    {
        DataSerializers.registerSerializer(VARINT);
    }

    public static final IDataSerializer<int[]> VARINT = new IDataSerializer<int[]>()
    {
        @Override
        public void write(PacketBuffer buf, int[] value)
        {
            buf.writeVarIntArray(value);
        }

        @Override
        public int[] read(PacketBuffer buf)
        {
            return buf.readVarIntArray();
        }

        @Override
        public int[] copy(int[] p_192717_1_)
        {
            return p_192717_1_;
        }

        @Override
        public DataParameter<int[]> createAccessor(int id)
        {
            return new DataParameter<>(id, this);
        }
    };
}
