package kr.rvs.chplus;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import org.bukkit.Bukkit;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
@MSExtension("CHPlus")
public class CHPlus extends AbstractExtension {
    public static final Version BUKKIT_VERSION = new SimpleVersion(Bukkit.getVersion());
    public static final Version V1_12 = new SimpleVersion(1, 12, 0);
    public static final byte CHAT_TYPE_SYSTEM = 2;
    public static final byte CHAT_TYPE_GAME_INFO = 2;

    @Override
    public Version getVersion() {
        return new SimpleVersion(1, 0, 0);
    }

    @Override
    public void onStartup() {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            System.out.println("CHPlus " + getVersion() + " enabled.");
        } else {
            System.out.println("[CHPlus] ProtocolLib not found.");
            throw new IllegalStateException("ProtocolLib not found.");
        }
    }

    @Override
    public void onShutdown() {
        System.out.println("CHPlus " + getVersion() + " disabled.");
    }
}
