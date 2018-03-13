package kr.rvs.chplus.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.netty.buffer.Unpooled;
import kr.rvs.chplus.CHPlus;
import kr.rvs.chplus.util.wrapper.AnvilContainerWrapper;
import kr.rvs.chplus.util.wrapper.PacketDataSerializerWrapper;
import kr.rvs.chplus.util.wrapper.PlayerWrapper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class CHPlusFactory {
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
        if (CHPlus.BUKKIT_VERSION.gte(CHPlus.V1_12)) {
            Class chatTypeClass = EnumWrappers.getChatTypeClass();
            StructureModifier components = packet.getSpecificModifier(chatTypeClass);

            // TODO: ProtocolLib 4.3.1 안정화 시 제거
            try {
                Method findByByte = chatTypeClass.getMethod("a", byte.class);
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

    public static PacketContainer createOpenWindowPacket(int next, String type, WrappedChatComponent component) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.OPEN_WINDOW);

        StructureModifier<Integer> modifier = packet.getIntegers();
        modifier.write(0, next);
        packet.getStrings().write(0, type);
        packet.getChatComponents().write(0, component);

        return packet;
    }

    public static PacketContainer createSetSlotPacket(int type, int slot, ItemStack item) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.SET_SLOT);

        StructureModifier<Integer> modifier = packet.getIntegers();
        modifier.write(0, type);
        modifier.write(1, slot);
        packet.getItemModifier().write(0, item);

        return packet;
    }

    @SuppressWarnings("unchecked")
    public static PacketContainer createCustomPayload(String channel, EnumWrappers.Hand hand) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
        PacketDataSerializerWrapper serializerWrapper = PacketDataSerializerWrapper.of(Unpooled.buffer())
                .writeEnum(hand);
        StructureModifier serializerModifier = packet.getSpecificModifier(MinecraftReflection.getPacketDataSerializerClass());

        packet.getStrings().write(0, channel);
        serializerModifier.write(0, serializerWrapper.getHandle());

        return packet;
    }

    public static AnvilContainerWrapper createAnvilContainer(MethodInterceptor interceptor, PlayerWrapper playerWrapper) {
        Enhancer proxy = new Enhancer();
        proxy.setSuperclass(Static.getNMSClass("ContainerAnvil"));
        proxy.setCallback(interceptor);
        proxy.setClassLoader(CHPlus.class.getClassLoader());

        Class[] argumentTypes = new Class[4];
        Object[] arguments = new Object[4];

        argumentTypes[0] = Static.getNMSClass("PlayerInventory");
        argumentTypes[1] = Static.getNMSClass("World");
        argumentTypes[2] = Static.getNMSClass("BlockPosition");
        argumentTypes[3] = Static.getNMSClass("EntityHuman");

        arguments[0] = playerWrapper.getPlayerInventory();
        arguments[1] = playerWrapper.getWorld();
        arguments[2] = Static.getEmptyBlockPosition();
        arguments[3] = playerWrapper.getEntityPlayer();

        return new AnvilContainerWrapper(proxy.create(argumentTypes, arguments));
    }

    public static WrappedChatComponent createChatMessage(String msg) {
        try {
            Class clazz = Static.getNMSClass("ChatMessage");
            Constructor conzt = clazz.getConstructor(String.class, Object[].class);
            Object chatMessage = conzt.newInstance(msg, new Object[0]);

            return WrappedChatComponent.fromHandle(chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't find a ChatMessage class");
        }
    }

    public static ItemStack createDefaultAnvilInputItem() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Enter input...");
        item.setItemMeta(meta);

        return item;
    }
}
