package kr.rvs.chplus;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import kr.rvs.chplus.event.internal.bukkit.ServerPingProtocolListener;
import kr.rvs.chplus.util.GUIHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.logging.Level;

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

    private static ServerPingProtocolListener sppl;

    @Override
    public Version getVersion() {
        return new SimpleVersion(1, 4, 0);
    }

    @Override
    public void onStartup() {
        Validate.isTrue(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"), "[CHPlus] ProtocolLib not found.");

        GUIHelper.init();
        sppl = new ServerPingProtocolListener();
        Bukkit.getLogger().log(Level.INFO, "CHPlus " + getVersion() + " enabled.");
    }

    @Override
    public void onShutdown() {
        GUIHelper.unregister();
        sppl.unregister();
        Bukkit.getLogger().log(Level.INFO, "CHPlus " + getVersion() + " disabled.");
    }
}
