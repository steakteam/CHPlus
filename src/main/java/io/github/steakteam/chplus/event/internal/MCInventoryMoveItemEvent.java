package io.github.steakteam.chplus.event.internal;

import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCItemStack;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public interface MCInventoryMoveItemEvent extends ICancellableBindableEvent {
    MCInventory getSource();

    MCInventory getDestination();

    MCInventory getInitiator();

    MCItemStack getItem();
}
