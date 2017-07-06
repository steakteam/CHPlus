package kr.rvs.chplus.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by Junhyeong Lim on 2017-07-05.
 */
public class Static {
    private static String nmsPackageName;
    private static Object emptyBlockPosition;

    public static String getNMSPackage(String name) {
        if (nmsPackageName == null) {
            Server server = Bukkit.getServer();
            try {
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                String packageName = consoleField.get(server).getClass().getName();
                nmsPackageName = packageName.substring(0,
                        packageName.indexOf(".", 29));
            } catch (Throwable e) {
                // Ignore
            }
        }

        return nmsPackageName + "." + name;
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName(getNMSPackage(name));
        } catch (Exception e) {
            throw new IllegalStateException("Can't find a " + name + " class");
        }
    }

    public static Object getEmptyBlockPosition() {
        if (emptyBlockPosition == null) {
            Class blockPositionClass = Static.getNMSClass("BlockPosition");
            try {
                Constructor constructor = blockPositionClass.getConstructor(int.class, int.class, int.class);
                emptyBlockPosition = constructor.newInstance(0, 0, 0);
            } catch (Exception e) {
                throw new IllegalStateException("Can't create a BlockPosition");
            }
        }

        return emptyBlockPosition;
    }
}
