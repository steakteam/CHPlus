package io.github.steakteam.chplus.event.internal;

import org.bukkit.event.Cancellable;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public abstract class CancellableBindableEvent implements ICancellableBindableEvent {
    private final Cancellable cancellable;

    public CancellableBindableEvent(Cancellable cancellable) {
        this.cancellable = cancellable;
    }

    @Override
    public Object _GetObject() {
        return cancellable;
    }

    @Override
    public boolean isCancelled() {
        return cancellable.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancellable.setCancelled(cancel);
    }
}
