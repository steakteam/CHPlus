package io.github.steakteam.chplus.event.internal.notifier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import io.github.steakteam.chplus.event.internal.impl.BukkitMCServerPingProtocolEvent;
import org.bukkit.Bukkit;

public class ServerPingProtocolNotifier extends PacketAdapter {
    private static final ServerPingProtocolNotifier INSTANCE = new ServerPingProtocolNotifier();

    public static void init() {
        release();
        ProtocolLibrary.getProtocolManager().addPacketListener(INSTANCE);
    }

    public static void release() {
        ProtocolLibrary.getProtocolManager().removePacketListener(INSTANCE);
    }

    private ServerPingProtocolNotifier() {
        super(PacketAdapter.params(Bukkit.getPluginManager().getPlugin("CommandHelper"), PacketType.Status.Server.SERVER_INFO).optionAsync());
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        BukkitMCServerPingProtocolEvent serverPing = new BukkitMCServerPingProtocolEvent(event.getPacket().getServerPings().read(0));
        EventUtils.TriggerListener(Driver.EXTENSION, "server_ping_protocol", serverPing);
    }
}
