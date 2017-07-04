package kr.rvs.chplus.util;

import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class GUIHelper {
    public static final Listener EMPTY_LISTENER = new Listener() {
        @Override
        public void onClick(InventoryClickEvent e) {
            // Empty
        }
    };
    private static final org.bukkit.event.Listener INTERNAL_LISTENER = new InternalListener();
    private static final Map<UUID, GUIHelper> HELPER_MAP = new HashMap<>();

    private final InventoryType type;
    private Map<Integer, Listener> listenerMap = new HashMap<>();
    private Map<Integer, ItemStack> itemMap = new HashMap<>();
    private String title;
    private int size = 9;

    public GUIHelper(InventoryType type) {
        this.type = type;
        this.title = type.getDefaultTitle();
    }

    public static void init() {
        HandlerList.unregisterAll(INTERNAL_LISTENER);
        Bukkit.getPluginManager().registerEvents(
                INTERNAL_LISTENER, CommandHelperPlugin.self);
    }

    private static void close(Entity entity) {
        HELPER_MAP.remove(entity.getUniqueId());
    }

    private static GUIHelper getHelper(Entity entity) {
        return HELPER_MAP.get(entity.getUniqueId());
    }

    private static void putEntity(Entity entity, GUIHelper helper) {
        HELPER_MAP.put(entity.getUniqueId(), helper);
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
        Inventory inv;
        if (this.type == InventoryType.CHEST) {
            inv = Bukkit.createInventory(null, size, title);
        } else {
            inv = Bukkit.createInventory(null, type, title);
        }

        // Put items
        for (Map.Entry<Integer, ItemStack> entry : itemMap.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

        return inv;
    }

    public GUIHelper open(MCPlayer player) {
        Player nativePlayer = (Player) player.getHandle();
        nativePlayer.openInventory(build());

        putEntity(nativePlayer, this);

        return this;
    }

    public GUIHelper putListener(Integer index, Listener listener) {
        this.listenerMap.put(index, listener);
        return this;
    }

    public GUIHelper putItem(Integer index, MCItemStack item) {
        this.itemMap.put(index, (ItemStack) item.getHandle());
        return this;
    }

    private Listener getListener(Integer index) {
        return listenerMap.containsKey(index) ?
                listenerMap.get(index) : EMPTY_LISTENER;
    }

    public interface Listener {
        void onClick(InventoryClickEvent e);
    }

    private static class InternalListener implements org.bukkit.event.Listener {
        @EventHandler
        public void onClick(InventoryClickEvent e) {
            HumanEntity clickedHuman = e.getWhoClicked();
            GUIHelper helper = getHelper(clickedHuman);

            if (helper == null)
                return;

            helper.getListener(e.getRawSlot())
                    .onClick(e);
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e) {
            close(e.getPlayer());
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            close(e.getPlayer());
        }
    }
}
