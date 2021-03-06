package io.github.steakteam.chplus.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.bukkit.BukkitMCItemStack;
import io.github.steakteam.chplus.CHPlus;
import io.github.steakteam.chplus.util.wrapper.AnvilContainerWrapper;
import io.github.steakteam.chplus.util.wrapper.PacketDataSerializerWrapper;
import io.github.steakteam.chplus.util.wrapper.PlayerWrapper;
import io.netty.buffer.Unpooled;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;

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

    public static PacketContainer createChatPacket(WrappedChatComponent component, EnumWrappers.ChatType type) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.CHAT);

        // Chat type
        if (CHPlus.BUKKIT_VERSION.gte(CHPlus.V1_12)) {
            packet.getChatTypes().write(0, type);
        } else {
            packet.getBytes().write(0, type.getId());
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
        StructureModifier<Object> serializerModifier = packet.getSpecificModifier((Class<Object>) MinecraftReflection.getPacketDataSerializerClass());

        packet.getStrings().write(0, channel);
        serializerModifier.write(0, serializerWrapper.getHandle());

        return packet;
    }

    public static AnvilContainerWrapper createAnvilContainer(MethodInterceptor interceptor, PlayerWrapper playerWrapper) {
        Enhancer proxy = new Enhancer();
        proxy.setSuperclass(Tools.getNMSClass("ContainerAnvil"));
        proxy.setCallback(interceptor);
        proxy.setClassLoader(CHPlus.class.getClassLoader());

        Class[] argumentTypes = new Class[4];
        Object[] arguments = new Object[4];

        argumentTypes[0] = Tools.getNMSClass("PlayerInventory");
        argumentTypes[1] = Tools.getNMSClass("World");
        argumentTypes[2] = Tools.getNMSClass("BlockPosition");
        argumentTypes[3] = Tools.getNMSClass("EntityHuman");

        arguments[0] = playerWrapper.getPlayerInventory();
        arguments[1] = playerWrapper.getWorld();
        arguments[2] = Tools.getEmptyBlockPosition();
        arguments[3] = playerWrapper.getEntityPlayer();

        return new AnvilContainerWrapper(proxy.create(argumentTypes, arguments));
    }

    public static WrappedChatComponent createChatMessage(String msg) {
        try {
            Class<?> clazz = Tools.getNMSClass("ChatMessage");
            Constructor<?> conzt = clazz.getConstructor(String.class, Object[].class);
            Object chatMessage = conzt.newInstance(msg, new Object[0]);

            return WrappedChatComponent.fromHandle(chatMessage);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find a ChatMessage class", e);
        }
    }

    public static MCItemStack createDefaultAnvilInputItem() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Enter input...");
        item.setItemMeta(meta);

        return new BukkitMCItemStack(item);
    }
}
