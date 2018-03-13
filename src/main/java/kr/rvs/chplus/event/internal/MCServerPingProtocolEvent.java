package kr.rvs.chplus.event.internal;

import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;

public interface MCServerPingProtocolEvent extends BindableEvent {
    public CInt getPlayerCount(Target t);

    public void setPlayerCount(int playerCount);
}
