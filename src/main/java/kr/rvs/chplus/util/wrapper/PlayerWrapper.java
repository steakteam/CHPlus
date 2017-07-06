package kr.rvs.chplus.util.wrapper;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import kr.rvs.chplus.util.Storage;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class PlayerWrapper extends Storage<Player> {
    private Object entityPlayer;

    public PlayerWrapper(Player handle) {
        super(handle);
    }

    public PlayerWrapper(Construct name, Target t) {
        this((Player) Static.GetPlayer(name, t).getHandle());
    }

    public void sendPacket(PacketContainer... containers) {
        for (PacketContainer container : containers) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(getHandle(), container);
            } catch (InvocationTargetException e) {
                // Ignore
            }
        }
    }

    public int nextContainerCounter() {
        Object entityPlayer = getEntityPlayer();
        try {
            Field containerCounterField = entityPlayer.getClass().getDeclaredField("containerCounter");
            containerCounterField.setAccessible(true);
            return (int) containerCounterField.get(entityPlayer);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a containerCounter value");
        }
    }

    public int getPing() {
        int ret = -1;
        Object entityPlayer = getEntityPlayer();
        try {
            Field pingField = entityPlayer.getClass().getField("ping");
            ret = (int) pingField.get(entityPlayer);
        } catch (Exception e) {
            // Ignore
        }

        return ret;
    }

    public Object getEntityPlayer() {
        if (entityPlayer == null) {
            try {
                Method getHandleMethod = getHandle().getClass().getMethod("getHandle");
                entityPlayer = getHandleMethod.invoke(getHandle());
            } catch (Exception e) {
                throw new IllegalStateException("Can't invoke a getHandle method");
            }
        }

        return entityPlayer;
    }

    public void setActiveContainer(Object container) {
        Object entityPlayer = getEntityPlayer();
        try {
            Field activeContainerField = entityPlayer.getClass().getSuperclass().getField("activeContainer");
            activeContainerField.set(entityPlayer, container);
        } catch (Exception e) {
            // Ignore
        }
    }

    public Object getPlayerInventory() {
        Object entityPlayer = getEntityPlayer();
        try {
            Field playerInventoryField = entityPlayer.getClass().getSuperclass().getField("inventory");
            return playerInventoryField.get(getEntityPlayer());
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a inventory value");
        }
    }

    public Object getWorld() {
        Object entityPlayer = getEntityPlayer();
        try {
            Field worldField = entityPlayer.getClass().getSuperclass().getField("world");
            return worldField.get(getEntityPlayer());
        } catch (Exception e) {
            throw new IllegalStateException("Can't get a world value");
        }
    }
}
