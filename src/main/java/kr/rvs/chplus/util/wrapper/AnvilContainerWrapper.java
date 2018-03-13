package kr.rvs.chplus.util.wrapper;

import kr.rvs.chplus.util.Static;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        } catch (Throwable th) {
            throw new IllegalStateException("Can't get inventory");
        }
    }

    public void setWindowId(int counter) {
        try {
            Field windowIdField = handle.getClass().getSuperclass().getField("windowId");
            windowIdField.set(handle, counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSlotListener(PlayerWrapper playerWrapper) {
        try {
            Method addSlotListenerMethod = handle.getClass().getMethod("addSlotListener",
                    Static.getNMSClass("ICrafting"));
            addSlotListenerMethod.invoke(handle, playerWrapper.getEntityPlayer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getHandle() {
        return handle;
    }
}
