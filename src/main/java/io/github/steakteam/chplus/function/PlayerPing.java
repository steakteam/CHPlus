package io.github.steakteam.chplus.function;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.wrapper.PlayerWrapper;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class PlayerPing extends CHPlusFunction {
    @Override
    public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
        MCPlayer player = args.length > 0
                ? Static.GetPlayer(args[0], t)
                : Static.getPlayer(env, t);
        return new CInt(PlayerWrapper.of(player).getPing(), t);
    }

    @Override
    public String getName() {
        return "pping";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{0, 1};
    }

    @Override
    public String docs() {
        return "int {player} " +
                "Returns a player's ping";
    }
}
