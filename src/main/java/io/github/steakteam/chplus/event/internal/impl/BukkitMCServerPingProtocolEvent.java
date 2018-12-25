package io.github.steakteam.chplus.event.internal.impl;


import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Target;
import io.github.steakteam.chplus.event.internal.MCServerPingProtocolEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitMCServerPingProtocolEvent extends Event implements MCServerPingProtocolEvent {

    private static final HandlerList handlers = new HandlerList();
    WrappedServerPing event;

    public BukkitMCServerPingProtocolEvent(WrappedServerPing e) {
        event = e;
    }

    @Override
    public Object _GetObject() {
        return this;
    }

    @Override
    public CInt getPlayerCount(Target t) {
        return new CInt(event.getPlayersOnline(), t);
    }

    @Override
    public void setPlayerCount(int playerCount) {
        event.setPlayersOnline(playerCount);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
