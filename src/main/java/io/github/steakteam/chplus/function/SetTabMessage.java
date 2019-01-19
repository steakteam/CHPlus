package io.github.steakteam.chplus.function;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.CHPlusFactory;
import io.github.steakteam.chplus.util.wrapper.PlayerWrapper;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class SetTabMessage extends CHPlusFunction {
    @Override
    public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
        Queue<Mixed> queue = new ArrayDeque<>(Arrays.asList(args));
        MCPlayer player = queue.size() >= 3
                ? Static.GetPlayer(queue.poll(), t)
                : Static.getPlayer(environment, t);
        String title = queue.poll().val();
        String footer = queue.poll().val();

        PlayerWrapper.of(player).sendPacket(
                CHPlusFactory.createPlayerListHeaderFooterPacket(title, footer)
        );
        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "set_tab_msg";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{2, 3};
    }

    @Override
    public String docs() {
        return "void {[player], header, footer}";
    }
}
