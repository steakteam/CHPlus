package io.github.steakteam.chplus.event.internal;

import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;

public interface MCServerPingProtocolEvent extends BindableEvent {
    CInt getPlayerCount(Target t);

    void setPlayerCount(int playerCount);
}
