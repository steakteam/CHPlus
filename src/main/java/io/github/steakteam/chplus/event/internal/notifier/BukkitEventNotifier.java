package io.github.steakteam.chplus.event.internal.notifier;

import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import io.github.steakteam.chplus.event.internal.impl.BukkitInventoryMoveItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public class BukkitEventNotifier implements Listener {
    private static final BukkitEventNotifier INSTANCE = new BukkitEventNotifier();

    private BukkitEventNotifier() {
    }

    public static void init() {
        release();
        Bukkit.getPluginManager().registerEvents(INSTANCE, CommandHelperPlugin.self);
    }

    public static void release() {
        HandlerList.unregisterAll(INSTANCE);
    }

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent e) {
        BukkitInventoryMoveItemEvent moveItemEvent = BukkitInventoryMoveItemEvent.create(e);
        EventUtils.TriggerListener(Driver.EXTENSION, "inventory_move_item", moveItemEvent);
    }
}
