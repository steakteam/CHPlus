package kr.rvs.chplus.function;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import kr.rvs.chplus.util.wrapper.PlayerWrapper;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class PlayerPing extends CHPlusFunction {
    @Override
    public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
        return new CInt(PlayerWrapper.of(args[0], t).getPing(), t);
    }

    @Override
    public String getName() {
        return "pping";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{1};
    }

    @Override
    public String docs() {
        return "int {player} " +
                "Returns a player's ping";
    }
}
