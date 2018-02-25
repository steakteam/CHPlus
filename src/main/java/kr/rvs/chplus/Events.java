package kr.rvs.chplus;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;
import kr.rvs.chplus.events.MCServerPingProtocolEvent;

import java.util.HashMap;
import java.util.Map;

public class Events {

    @api
    public static class server_ping_protocol extends AbstractEvent {

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
        public Version since() {
            return CHVersion.V3_3_2;
        }

        @Override
        public boolean matches(Map<String, Construct> map, BindableEvent bindableEvent) throws PrefilterNonMatchException {
            return bindableEvent instanceof MCServerPingProtocolEvent;
        }

        @Override
        public BindableEvent convert(CArray cArray, Target target) {
            throw new UnsupportedOperationException("This is not supported");
        }

        @Override
        public Map<String, Construct> evaluate(BindableEvent bindableEvent) throws EventException {
            if(bindableEvent instanceof MCServerPingProtocolEvent) {
                MCServerPingProtocolEvent e = (MCServerPingProtocolEvent) bindableEvent;
                Target t = Target.UNKNOWN;
                Map<String, Construct> ret = new HashMap<>();
                ret.put("players", e.getPlayerCount(t));
                return ret;
            } else {
                throw new EventException("This is not server ping by protocollib event.");
            }
        }

        @Override
        public Driver driver() {
            return Driver.EXTENSION;
        }

        @Override
        public boolean modifyEvent(String s, Construct construct, BindableEvent bindableEvent) {
            if(bindableEvent instanceof MCServerPingProtocolEvent) {
                MCServerPingProtocolEvent e = (MCServerPingProtocolEvent) bindableEvent;
                if(s.equalsIgnoreCase("players")) {
                    e.setPlayerCount(Static.getInt32(construct, construct.getTarget()));
                    return true;
                }
            }
            return false;
        }
    }
}
