package kr.rvs.chplus.events.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import org.bukkit.Bukkit;

public class ServerPingProtocolListener {
    private PacketAdapter adapter;
    public ServerPingProtocolListener() {
        adapter = new PacketAdapter(PacketAdapter.params(Bukkit.getPluginManager().getPlugin("CommandHelper"), PacketType.Status.Server.SERVER_INFO).optionAsync()) {
            @Override
            public void onPacketSending(PacketEvent event) {
                BukkitMCServerPingProtocolEvent serverPing = new BukkitMCServerPingProtocolEvent(event.getPacket().getServerPings().read(0));
                EventUtils.TriggerListener(Driver.EXTENSION, "server_ping_protocol", serverPing);
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
    }

    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener(adapter);
    }
}
