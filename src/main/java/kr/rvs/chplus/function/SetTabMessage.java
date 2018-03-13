package kr.rvs.chplus.function;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import kr.rvs.chplus.util.CHPlusFactory;
import kr.rvs.chplus.util.wrapper.PlayerWrapper;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class SetTabMessage extends CHPlusFunction {
    @Override
    public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
        PlayerWrapper.of(args[0], t).sendPacket(
                CHPlusFactory.createPlayerListHeaderFooterPacket(args[1].val(), args[2].val())
        );
        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "set_tab_msg";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{3};
    }

    @Override
    public String docs() {
        return null;
    }
}
