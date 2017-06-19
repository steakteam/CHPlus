package kr.rvs.chplus.util;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class PlayerWrapper {
    private Player player;

    public PlayerWrapper(Player player) {
        this.player = player;
    }

    public PlayerWrapper(Construct name, Target t) {
        this((Player) Static.GetPlayer(name, t).getHandle());
    }

    public void sendPacket(PacketContainer... containers) {
        try {
            for (PacketContainer container : containers) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
            }
        } catch (InvocationTargetException e) {
            // Ignore
        }
    }
}
