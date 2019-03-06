package io.github.steakteam.chplus.function;

import com.laytonsmith.abstraction.MCFireworkEffect;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.annotations.api;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidWorldException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.Reader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;

import java.util.Collections;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class LaunchInstantFirework extends CHPlusFunction {
    @Override
    public Mixed exec(Target t, Environment env, Reader<Mixed> args) throws ConfigRuntimeException {
        MCLocation location = ObjectGenerator.GetGenerator().location(args.read(), null, t);
        CArray array = args.isRemain()
                ? Static.getArray(args.read(), t)
                : CArray.GetAssociativeArray(t);
        MCFireworkEffect effect = ObjectGenerator.GetGenerator()
                .fireworkEffect(array, t);
        Firework firework = (Firework) location.getWorld().launchFirework(
                location, 0, Collections.singletonList(effect)).getHandle();

        Bukkit.getScheduler().runTaskLater(CommandHelperPlugin.self, firework::detonate, 2);

        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "launch_instant_firework";
    }

    @Override
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{
                CRECastException.class,
                CREFormatException.class,
                CREInvalidWorldException.class
        };
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{1, 2};
    }

    @Override
    public String docs() {
        return "void {locationArray, [fireworkArray]}";
    }
}
