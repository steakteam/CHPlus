package io.github.steakteam.chplus;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import io.github.steakteam.chplus.event.internal.notifier.BukkitEventNotifier;
import io.github.steakteam.chplus.event.internal.notifier.ServerPingProtocolNotifier;
import io.github.steakteam.chplus.util.GUIHelper;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
@MSExtension("CHPlus")
public class CHPlus extends AbstractExtension {
    public static final Version BUKKIT_VERSION = new SimpleVersion(Bukkit.getBukkitVersion());
    public static final Version V1_12 = new SimpleVersion(1, 12, 0);

    @Override
    public Version getVersion() {
        return new SimpleVersion(1, 7, 0);
    }

    @Override
    public void onStartup() {
        Logger logger = CommandHelperPlugin.self.getLogger();
        GUIHelper.init();
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            ServerPingProtocolNotifier.init();
        } else {
            logger.log(Level.WARNING, "ProtocolLib not found.");
        }
        BukkitEventNotifier.init();
        logger.log(Level.INFO, "CHPlus " + getVersion() + " enabled.");
    }

    @Override
    public void onShutdown() {
        GUIHelper.release();
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            ServerPingProtocolNotifier.release();
        }
        BukkitEventNotifier.release();
        Bukkit.getLogger().log(Level.INFO, "CHPlus " + getVersion() + " disabled.");
    }
}
