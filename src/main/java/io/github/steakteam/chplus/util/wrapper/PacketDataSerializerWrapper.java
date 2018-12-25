package io.github.steakteam.chplus.util.wrapper;

import com.comphenix.protocol.utility.MinecraftReflection;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
public class PacketDataSerializerWrapper {
    private final ByteBuf serializer;

    @SuppressWarnings("unchecked")
    public static PacketDataSerializerWrapper of(ByteBuf buf) {
        try {
            Constructor<ByteBuf> serializerConst = (Constructor<ByteBuf>)
                    MinecraftReflection.getPacketDataSerializerClass().getConstructor(ByteBuf.class);
            ByteBuf serializer = serializerConst.newInstance(buf);
            return new PacketDataSerializerWrapper(serializer);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private PacketDataSerializerWrapper(ByteBuf serializer) {
        this.serializer = serializer;
    }

    public PacketDataSerializerWrapper writeVarInt(int val) {
        while ((val & -128) != 0) {
            serializer.writeByte(val & 127 | 128);
            val >>>= 7;
        }
        serializer.writeByte(val);
        return this;
    }

    public PacketDataSerializerWrapper writeEnum(Enum enumVal) {
        return writeVarInt(enumVal.ordinal());
    }

    public ByteBuf getHandle() {
        return serializer;
    }
}
