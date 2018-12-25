package io.github.steakteam.chplus.event.internal.impl;

import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCItemStack;
import com.laytonsmith.abstraction.bukkit.BukkitMCInventory;
import com.laytonsmith.abstraction.bukkit.BukkitMCItemStack;
import io.github.steakteam.chplus.event.internal.CancellableBindableEvent;
import io.github.steakteam.chplus.event.internal.MCInventoryMoveItemEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public class BukkitInventoryMoveItemEvent extends CancellableBindableEvent implements MCInventoryMoveItemEvent {
    private final MCInventory source;
    private final MCInventory destination;
    private final MCInventory initiator;
    private final MCItemStack item;

    public BukkitInventoryMoveItemEvent(Cancellable cancellable, MCInventory source, MCInventory destination, MCInventory initiator, MCItemStack item) {
        super(cancellable);
        this.source = source;
        this.destination = destination;
        this.initiator = initiator;
        this.item = item;
    }

    public static BukkitInventoryMoveItemEvent create(InventoryMoveItemEvent e) {
        return new BukkitInventoryMoveItemEvent(
                e,
                new BukkitMCInventory(e.getSource()),
                new BukkitMCInventory(e.getDestination()),
                new BukkitMCInventory(e.getInitiator()),
                new BukkitMCItemStack(e.getItem())
        );
    }

    @Override
    public MCInventory getSource() {
        return source;
    }

    @Override
    public MCInventory getDestination() {
        return destination;
    }

    @Override
    public MCInventory getInitiator() {
        return initiator;
    }

    @Override
    public MCItemStack getItem() {
        return item;
    }
}
