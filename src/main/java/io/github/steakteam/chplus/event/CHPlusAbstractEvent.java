package io.github.steakteam.chplus.event;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
public abstract class CHPlusAbstractEvent extends AbstractEvent {
    @Override
    public Version since() {
        return CHVersion.V3_3_2;
    }

    @Override
    public BindableEvent convert(CArray cArray, Target target) {
        throw new UnsupportedOperationException("This is not supported");
    }

    @Override
    public Driver driver() {
        return Driver.EXTENSION;
    }
}
