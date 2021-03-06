package io.github.steakteam.chplus.function;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.CHPlusFactory;
import io.github.steakteam.chplus.util.Reader;
import io.github.steakteam.chplus.util.wrapper.PlayerWrapper;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class SendJsonMessage extends CHPlusFunction {
    @Override
    public Mixed exec(Target t, Environment environment, Reader<Mixed> args) throws ConfigRuntimeException {
        MCPlayer player = args.size() >= 2
                ? Static.GetPlayer(args.read(), t)
                : Static.getPlayer(environment, t);
        String json = args.read().val();

        PlayerWrapper.of(player).sendPacket(
                CHPlusFactory.createChatPacket(
                        WrappedChatComponent.fromJson(json),
                        EnumWrappers.ChatType.SYSTEM
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
