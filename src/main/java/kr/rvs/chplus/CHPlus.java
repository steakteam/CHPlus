package kr.rvs.chplus;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import kr.rvs.chplus.util.GUIHelper;
import org.bukkit.Bukkit;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
@MSExtension("CHPlus")
public class CHPlus extends AbstractExtension {
    public static final Version BUKKIT_VERSION = new SimpleVersion(Bukkit.getBukkitVersion());
    public static final Version V1_12 = new SimpleVersion(1, 12, 0);

    // TODO: ProtocolLib 4.3.1 안정화 시 제거
    public static final byte CHAT_TYPE_SYSTEM = 1;
    public static final byte CHAT_TYPE_GAME_INFO = 2;

    @Override
    public Version getVersion() {
        return new SimpleVersion(1, 1, 3);
    }

    @Override
    public void onStartup() {
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            System.out.println("CHPlus " + getVersion() + " enabled.");
        } else {
            System.out.println("[CHPlus] ProtocolLib not found.");
            throw new IllegalStateException("ProtocolLib not found.");
        }

        GUIHelper.init();
    }

    @Override
    public void onShutdown() {
        System.out.println("CHPlus " + getVersion() + " disabled.");
    }
}
