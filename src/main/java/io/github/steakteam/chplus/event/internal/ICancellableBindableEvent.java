package io.github.steakteam.chplus.event.internal;

import com.laytonsmith.core.events.BindableEvent;
import org.bukkit.event.Cancellable;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public interface ICancellableBindableEvent extends BindableEvent, Cancellable {
}
