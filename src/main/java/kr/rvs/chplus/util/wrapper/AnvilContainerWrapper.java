package kr.rvs.chplus.util.wrapper;

import kr.rvs.chplus.util.Static;
import kr.rvs.chplus.util.Storage;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class AnvilContainerWrapper extends Storage {
    public AnvilContainerWrapper(Object object) {
        super(object);
    }

    public Inventory getTopInventory() {
        try {
            Method getBukkitView = getHandle().getClass().getMethod("getBukkitView");
            Object bukkitView = getBukkitView.invoke(getHandle());
            Method topInventory = bukkitView.getClass().getMethod("getTopInventory");

            return (Inventory) topInventory.invoke(bukkitView);
        } catch (Throwable th) {
            throw new IllegalStateException("Can't get inventory");
        }
    }

    public void setWindowId(int counter) {
        try {
            Field windowIdField = getHandle().getClass().getSuperclass().getField("windowId");
            windowIdField.set(getHandle(), counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSlotListener(PlayerWrapper playerWrapper) {
        try {
            Method addSlotListenerMethod = getHandle().getClass().getMethod("addSlotListener",
                    Static.getNMSClass("ICrafting"));
            addSlotListenerMethod.invoke(getHandle(), playerWrapper.getEntityPlayer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
