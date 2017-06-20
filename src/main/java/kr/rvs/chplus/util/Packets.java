package kr.rvs.chplus.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import kr.rvs.chplus.CHPlus;

import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class Packets {
    public static PacketContainer createPlayerListHeaderFooterPacket(String title, String footer) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        StructureModifier<WrappedChatComponent> components = packet.getChatComponents();

        components.write(0, WrappedChatComponent.fromText(title));
        components.write(1, WrappedChatComponent.fromText(footer));

        return packet;
    }

    @SuppressWarnings("unchecked")
    public static PacketContainer createChatPacket(WrappedChatComponent component, byte type) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.CHAT);

        // Chat type
        if (CHPlus.BUKKIT_VERSION.gt(CHPlus.V1_11_2)) {
            Class chatTypeClass = EnumWrappers.getChatTypeClass();
            StructureModifier components = packet.getSpecificModifier(chatTypeClass);

            try {
                Method findByByte = chatTypeClass.getMethod("a");
                components.write(0, findByByte.invoke(null, type));
            } catch (Exception e) {
                // Ignore
            }
        } else {
            packet.getBytes().write(0, type);
        }

        // Contents
        packet.getChatComponents().write(0, component);
        return packet;
    }

    public static PacketContainer createTitlePacket(EnumWrappers.TitleAction action, String content,
                                                    int fadeIn, int stay, int fadeOut) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.TITLE);

        packet.getTitleActions().write(0, action);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(content));

        StructureModifier<Integer> modifier = packet.getIntegers();
        modifier.write(0, fadeIn);
        modifier.write(1, stay);
        modifier.write(2, fadeOut);

        return packet;
    }

    public static PacketContainer createTitlePacket(EnumWrappers.TitleAction action, String content) {
        return createTitlePacket(action, content, -1, -1, -1);
    }

    public static PacketContainer createTitlePacket(int fadeIn, int stay, int fadeOut) {
        return createTitlePacket(EnumWrappers.TitleAction.TIMES, null, fadeIn, stay, fadeOut);
    }
}
