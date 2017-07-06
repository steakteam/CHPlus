package kr.rvs.chplus;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCFireworkEffect;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidWorldException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import kr.rvs.chplus.util.CHPlusFactory;
import kr.rvs.chplus.util.GUIHelper;
import kr.rvs.chplus.util.wrapper.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
public class Functions {
    @api
    public static class set_tab_msg extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            new PlayerWrapper(args[0], t).sendPacket(
                    CHPlusFactory.createPlayerListHeaderFooterPacket(args[1].val(), args[2].val())
            );
            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{3};
        }

        @Override
        public String docs() {
            return null;
        }
    }

    @api
    public static class send_action_msg extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            new PlayerWrapper(args[0], t).sendPacket(
                    CHPlusFactory.createChatPacket(
                            WrappedChatComponent.fromText(args[1].val()),
                            CHPlus.CHAT_TYPE_GAME_INFO
                    )
            );
            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        @Override
        public String docs() {
            return null;
        }
    }

    @api
    public static class send_title_msg extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            PlayerWrapper wrapper = new PlayerWrapper(args[0], t);
            String title = args[1].val();
            String subTitle = args[2].val();
            int fadeIn = Static.getInt32(args[3], t);
            int stay = Static.getInt32(args[4], t);
            int fadeOut = Static.getInt32(args[5], t);

            wrapper.sendPacket(
                    CHPlusFactory.createTitlePacket(fadeIn, stay, fadeOut),
                    CHPlusFactory.createTitlePacket(EnumWrappers.TitleAction.TITLE, title),
                    CHPlusFactory.createTitlePacket(EnumWrappers.TitleAction.SUBTITLE, subTitle)
            );
            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{6};
        }

        @Override
        public String docs() {
            return null;
        }
    }

    @api
    public static class send_json_msg extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            new PlayerWrapper(args[0], t).sendPacket(
                    CHPlusFactory.createChatPacket(
                            WrappedChatComponent.fromJson(args[1].val()),
                            CHPlus.CHAT_TYPE_SYSTEM
                    )
            );
            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        @Override
        public String docs() {
            return null;
        }
    }

    @api
    public static class launch_instant_firework extends AbstractFunction {
        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{
                    CRECastException.class,
                    CREFormatException.class,
                    CREInvalidWorldException.class
            };
        }

        @Override
        public boolean isRestricted() {
            return true;
        }

        @Override
        public Boolean runAsync() {
            return false;
        }

        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCLocation location = ObjectGenerator.GetGenerator().location(args[0], null, t);
            CArray array;

            if (args.length >= 2) {
                array = Static.getArray(args[1], t);
            } else {
                array = CArray.GetAssociativeArray(t);
            }

            MCFireworkEffect effect = ObjectGenerator.GetGenerator()
                    .fireworkEffect(array, t);
            final Firework firework = (Firework) location.getWorld().launchFirework(
                    location, 0, Collections.singletonList(effect)).getHandle();

            Bukkit.getScheduler().runTaskLater(CommandHelperPlugin.self, new Runnable() {
                @Override
                public void run() {
                    firework.detonate();
                }
            }, 2);

            return CVoid.VOID;
        }

        @Override
        public Version since() {
            return CHVersion.V3_3_2;
        }

        @Override
        public String getName() {
            return getClass().getSimpleName();
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        @Override
        public String docs() {
            return null;
        }
    }

    // Refer to CHExodius
    @api
    public static class user_input extends CHPlusFunction {
        private static final int INPUT_LEFT = 0;
        private static final int OUTPUT = 2;

        @Override
        public Construct exec(final Target t, Environment env, Construct... args) throws ConfigRuntimeException {
            MCPlayer player = Static.GetPlayer(args[0], t);
            final CClosure closure = Static.getObject(args[1], t, CClosure.class);

            GUIHelper helper = new GUIHelper(InventoryType.ANVIL)
                    .putListener(new GUIHelper.Listener() {
                        @Override
                        public void onClick(InventoryClickEvent e) {
                            e.setCancelled(true);
                            int rawSlot = e.getRawSlot();
                            if (rawSlot == OUTPUT) {
                                // Get input
                                String name = "";
                                ItemStack item = e.getCurrentItem();
                                if (item != null && item.hasItemMeta()) {
                                    ItemMeta meta = item.getItemMeta();
                                    if (meta.getDisplayName() != null)
                                        name = meta.getDisplayName();
                                }

                                e.getWhoClicked().closeInventory();
                                closure.execute(new CString(name, t));
                            }
                        }
                    });

            if (args.length >= 3) {
                helper.putItem(0,
                        ObjectGenerator.GetGenerator().item(args[2], t));
            } else {
                helper.putItem(0,
                        CHPlusFactory.createDefaultAnvilInputItem());
            }

            helper.open(player);

            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{2, 3};
        }

        @Override
        public String docs() {
            return "void {player, callback closure, [item array]} " +
                    "Open an Anvil GUI input for player, calling the callback closure when the player submits the input. " +
                    "The text the player typed in gets returned to the closure. " +
                    "Item can be an item array, already only the keys 'type', 'data' and 'display' are used. ";
        }
    }

    @api
    public static class pping extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
            return new CInt(new PlayerWrapper(args[0], t).getPing(), t);
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        @Override
        public String docs() {
            return "int {player} " +
                    "Returns a player's ping";
        }
    }

    @api
    public static class respawn extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
            MCPlayer player = Static.GetPlayer(args[0], t);
            Player nativePlayer = (Player) player.getHandle();

            try {
                nativePlayer.spigot().respawn();
            } catch (Throwable th) {
                throw new CREException("Can't use spigot method.", t);
            }

            return CVoid.VOID;
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        @Override
        public String docs() {
            return "void {player} " +
                    "Respawn a specific player.";
        }
    }

    static abstract class CHPlusFunction extends AbstractFunction {
        @Override
        public boolean isRestricted() {
            return true;
        }

        @Override
        public abstract Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException;

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{
                    CREPlayerOfflineException.class,
                    CRECastException.class
            };
        }

        @Override
        public Boolean runAsync() {
            return false;
        }

        @Override
        public Version since() {
            return CHVersion.V3_3_2;
        }

        @Override
        public String getName() {
            return getClass().getSimpleName();
        }
    }
}
