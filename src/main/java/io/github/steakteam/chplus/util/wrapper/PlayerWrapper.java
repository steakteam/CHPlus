package io.github.steakteam.chplus.util.wrapper;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class PlayerWrapper {
    private final Player player;
    private final Object entityPlayer;

    public static PlayerWrapper of(Player player) {
        try {
            Method getHandleMethod = player.getClass().getMethod("getHandle");
            Object entityPlayer = getHandleMethod.invoke(player);

            return new PlayerWrapper(player, entityPlayer);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed wrapping player");
        }
    }

    public static PlayerWrapper of(MCPlayer mcPlayer) {
        Object handle = mcPlayer.getHandle();
        Validate.isTrue(handle instanceof Player);
        return of((Player) handle);
    }

    public static PlayerWrapper of(Construct name, Target t) {
        return of(Static.GetPlayer(name, t));
    }

    private PlayerWrapper(Player player, Object entityPlayer) {
        this.player = player;
        this.entityPlayer = entityPlayer;
    }

    public void sendPacket(PacketContainer... containers) {
        for (PacketContainer container : containers) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
            } catch (InvocationTargetException e) {
                // Ignore
            }
        }
    }

    public int nextContainerCounter() {
        try {
            Method increaseMethod = entityPlayer.getClass().getDeclaredMethod("nextContainerCounter");
            return (int) increaseMethod.invoke(entityPlayer);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a containerCounter value");
        }
    }

    public int getPing() {
        int ret = -1;
        try {
            Field pingField = entityPlayer.getClass().getField("ping");
            ret = (int) pingField.get(entityPlayer);
        } catch (Exception e) {
            // Ignore
        }

        return ret;
    }

    public void setActiveContainer(Object container) {
        try {
            Field activeContainerField = entityPlayer.getClass().getSuperclass().getField("activeContainer");
            activeContainerField.set(entityPlayer, container);
        } catch (Exception e) {
            // Ignore
        }
    }

    public Object getPlayerInventory() {
        try {
            Field playerInventoryField = entityPlayer.getClass().getSuperclass().getField("inventory");
            return playerInventoryField.get(entityPlayer);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a inventory value");
        }
    }

    public Object getWorld() {
        try {
            Field worldField = entityPlayer.getClass().getSuperclass().getField("world");
            return worldField.get(entityPlayer);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a world value");
        }
    }

    public Object getEntityPlayer() {
        return entityPlayer;
    }
}
