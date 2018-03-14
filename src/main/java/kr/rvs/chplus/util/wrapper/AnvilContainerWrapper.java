package kr.rvs.chplus.util.wrapper;

import kr.rvs.chplus.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class AnvilContainerWrapper {
    private final Object handle;

    public AnvilContainerWrapper(Object handle) {
        this.handle = handle;
    }

    public Inventory getTopInventory() {
        try {
            Method getBukkitView = handle.getClass().getMethod("getBukkitView");
            Object bukkitView = getBukkitView.invoke(handle);
            Method topInventory = bukkitView.getClass().getMethod("getTopInventory");

            return (Inventory) topInventory.invoke(bukkitView);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get inventory");
        }
    }

    public void setWindowId(int counter) {
        try {
            Field windowIdField = handle.getClass().getSuperclass().getField("windowId");
            windowIdField.set(handle, counter);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, e, () -> "Failed setWindowId");
        }
    }

    public void addSlotListener(PlayerWrapper playerWrapper) {
        try {
            Method addSlotListenerMethod = handle.getClass().getMethod("addSlotListener",
                    Tools.getNMSClass("ICrafting"));
            addSlotListenerMethod.invoke(handle, playerWrapper.getEntityPlayer());
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, e, () -> "Failed addSlotListener");
        }
    }

    public Object getHandle() {
        return handle;
    }
}
