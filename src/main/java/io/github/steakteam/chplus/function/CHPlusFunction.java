package io.github.steakteam.chplus.function;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.*;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
public abstract class CHPlusFunction extends AbstractFunction {
    @Override
    public boolean isRestricted() {
        return true;
    }

    @Override
    public abstract Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException;

    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{
                CREPlayerOfflineException.class,
                CRECastException.class,
                CRELengthException.class,
                CREFormatException.class
        };
    }

    @Override
    public Boolean runAsync() {
        return false;
    }

    @Override
    public Version since() {
        return MSVersion.V3_3_2;
    }
}
