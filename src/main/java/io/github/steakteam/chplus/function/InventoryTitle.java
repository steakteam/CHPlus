package io.github.steakteam.chplus.function;

import com.laytonsmith.abstraction.MCInventory;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.util.Reader;

/**
 * Created by JunHyeong Lim on 2018-03-14
 */
@api
public class InventoryTitle extends CHPlusFunction {
    @Override
    public Mixed exec(Target t, Environment env, Reader<Mixed> args) throws ConfigRuntimeException {
        MCPlayer player = args.size() > 0
                ? Static.GetPlayer(args.read(), t)
                : Static.getPlayer(env, t);
        MCInventory topInv = player.getOpenInventory().getTopInventory();
        String title = topInv != null ? topInv.getTitle() : "null";
        return new CString(title, t);
    }

    @Override
    public String getName() {
        return "inv_title";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{0, 1};
    }

    @Override
    public String docs() {
        return "string {[player]} " +
                "Returns a top inventory title " +
                "If not opened a top inventory on that player, returns  \"null\" string.";
    }
}
