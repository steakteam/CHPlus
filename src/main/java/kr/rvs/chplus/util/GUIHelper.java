package kr.rvs.chplus.util;

import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import kr.rvs.chplus.proxy.ContainerMethodInterceptor;
import kr.rvs.chplus.util.wrapper.AnvilContainerWrapper;
import kr.rvs.chplus.util.wrapper.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class GUIHelper {
    private static final Listener INTERNAL_LISTENER = new InternalListener();
    private static final Map<UUID, GUIHelper> HELPER_MAP = new HashMap<>();

    private final InventoryType type;
    private Set<GUIHandler> listeners = new HashSet<>();
    private Map<Integer, ItemStack> itemMap = new HashMap<>();
    private String title;
    private int size = 9;

    public GUIHelper(InventoryType type) {
        this.type = type;
        this.title = type.getDefaultTitle();
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(
                INTERNAL_LISTENER, CommandHelperPlugin.self);
    }

    public static void unregister() {
        HandlerList.unregisterAll(INTERNAL_LISTENER);
    }

    private static GUIHelper release(Entity entity) {
        return HELPER_MAP.remove(entity.getUniqueId());
    }

    private static GUIHelper getHelper(Entity entity) {
        return HELPER_MAP.get(entity.getUniqueId());
    }

    private static void putEntity(Entity entity, GUIHelper helper) {
        HELPER_MAP.put(entity.getUniqueId(), helper);
    }

    public static boolean isIn(Entity entity) {
        return HELPER_MAP.containsKey(entity.getUniqueId());
    }

    public GUIHelper setTitle(String title) {
        this.title = title;
        return this;
    }

    public GUIHelper setSize(int size) {
        this.size = size;
        return this;
    }

    public Inventory build() {
        Inventory inv = this.type == InventoryType.CHEST
                ? Bukkit.createInventory(null, size, title)
                : Bukkit.createInventory(null, type, title);
        setItemToInventory(inv);
        return inv;
    }

    private void setItemToInventory(Inventory inv) {
        for (Map.Entry<Integer, ItemStack> entry : itemMap.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }
    }

    public GUIHelper open(MCPlayer player) {
        final Player nativePlayer = (Player) player.getHandle();
        if (type == InventoryType.ANVIL) {
            PlayerWrapper playerWrapper = new PlayerWrapper(nativePlayer);
            AnvilContainerWrapper containerWrapper = CHPlusFactory.createAnvilContainer(
                    new ContainerMethodInterceptor(), playerWrapper);
            Inventory inv = containerWrapper.getTopInventory();
            setItemToInventory(inv);
            int counter = playerWrapper.nextContainerCounter();

            playerWrapper.sendPacket(
                    CHPlusFactory.createOpenWindowPacket(
                            counter, "minecraft:anvil", CHPlusFactory.createChatMessage("Repairing")
                    ),
                    CHPlusFactory.createSetSlotPacket(0, 0, itemMap.get(0))
            );

            containerWrapper.setWindowId(counter);
            containerWrapper.addSlotListener(playerWrapper);
            playerWrapper.setActiveContainer(containerWrapper.getHandle());
        } else {
            nativePlayer.openInventory(build());
        }
        putEntity(nativePlayer, this);

        return this;
    }

    public GUIHelper putListener(GUIHandler listener) {
        listeners.add(listener);
        return this;
    }

    public GUIHelper putItem(Integer index, MCItemStack item) {
        return putItem(index, (ItemStack) item.getHandle());
    }

    public GUIHelper putItem(Integer index, ItemStack item) {
        this.itemMap.put(index, item);
        return this;
    }

    private void notifyListeners(InventoryEvent e) {
        listeners.forEach(listener -> listener.onEvent(e));
    }

    public interface GUIHandler {
        void onEvent(InventoryEvent e);
    }

    private static class InternalListener implements org.bukkit.event.Listener {
        @EventHandler
        public void onClick(InventoryClickEvent e) {
            HumanEntity clickedHuman = e.getWhoClicked();
            GUIHelper helper = getHelper(clickedHuman);
            InventoryAction action = e.getAction();
            Inventory inv = e.getInventory();
            int rawSlot = e.getRawSlot();

            if (helper == null)
                return;

            if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                rawSlot = inv.firstEmpty();
            } else if (action == InventoryAction.COLLECT_TO_CURSOR) {
                for (int i = 0; i < inv.getSize(); i++) {
                    ItemStack item = inv.getItem(i);
                    if (e.getCursor().isSimilar(item)) {
                        e.setCancelled(true);
                        break;
                    }
                }
            }

            if (rawSlot > inv.getSize())
                return;

            helper.notifyListeners(e);
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e) {
            Optional.ofNullable(release(e.getPlayer())).ifPresent(helper ->
                    helper.notifyListeners(e));
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            release(e.getPlayer());
        }
    }
}
