package io.github.steakteam.chplus.event;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.event.internal.MCServerPingProtocolEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class ServerPingProtocol extends CHPlusAbstractEvent {
    @Override
    public String getName() {
        return "server_ping_protocol";
    }

    @Override
    public String docs() {
        return "{}"
                + " Basically same as server_ping event, but only event data is players(which represents online player's count) and it's mutable field."
                + "{players: The number of players online}"
                + "{players}"
                + "{}";
    }

    @Override
    public boolean matches(Map<String, Mixed> map, BindableEvent bindableEvent) {
        return bindableEvent instanceof MCServerPingProtocolEvent;
    }

    @Override
    public Map<String, Mixed> evaluate(BindableEvent bindableEvent) throws EventException {
        if (bindableEvent instanceof MCServerPingProtocolEvent) {
            MCServerPingProtocolEvent e = (MCServerPingProtocolEvent) bindableEvent;
            Target t = Target.UNKNOWN;
            Map<String, Mixed> ret = new HashMap<>();
            ret.put("players", e.getPlayerCount(t));
            return ret;
        } else {
            throw new EventException("This is not server ping by protocollib event.");
        }
    }

    @Override
    public boolean modifyEvent(String s, Mixed construct, BindableEvent bindableEvent) {
        if (bindableEvent instanceof MCServerPingProtocolEvent) {
            MCServerPingProtocolEvent e = (MCServerPingProtocolEvent) bindableEvent;
            if (s.equalsIgnoreCase("players")) {
                e.setPlayerCount(Static.getInt32(construct, construct.getTarget()));
                return true;
            }
        }
        return false;
    }
}
