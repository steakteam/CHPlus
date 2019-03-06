package io.github.steakteam.chplus.function;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CRELengthException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.Reader;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
public abstract class CHPlusFunction extends AbstractFunction {
    @Override
    public boolean isRestricted() {
        return true;
    }

    @Override
    public final Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
        return exec(t, environment, new Reader<>(args));
    }

    public abstract Mixed exec(Target t, Environment env, Reader<Mixed> args) throws ConfigRuntimeException;

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
