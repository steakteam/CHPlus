package io.github.steakteam.chplus.function;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import io.github.steakteam.chplus.CHPlus;
import io.github.steakteam.chplus.util.CHPlusFactory;
import io.github.steakteam.chplus.util.wrapper.PlayerWrapper;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class SendJsonMessage extends CHPlusFunction {
    @Override
    public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
        Queue<Construct> queue = new ArrayDeque<>(Arrays.asList(args));
        MCPlayer player = queue.size() >= 2
                ? Static.GetPlayer(queue.poll(), t)
                : Static.getPlayer(environment, t);
        String json = queue.poll().val();

        PlayerWrapper.of(player).sendPacket(
                CHPlusFactory.createChatPacket(
                        WrappedChatComponent.fromJson(json),
                        CHPlus.CHAT_TYPE_SYSTEM
                )
        );
        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "send_json_msg";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{1, 2};
    }

    @Override
    public String docs() {
        return "void {[player], json}";
    }
}
