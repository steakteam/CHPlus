package kr.rvs.chplus;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCFireworkEffect;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.annotations.api;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import kr.rvs.chplus.util.Packets;
import kr.rvs.chplus.util.PlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;

import java.util.Collections;

/**
 * Created by Junhyeong Lim on 2017-06-18.
 */
@SuppressWarnings("unchecked")
public class Functions {
    private static Construct getOrDefault(Construct[] args, Integer index, Construct def) {
        Construct ret = null;

        if (args.length > index)
            ret = args[index];

        return ret != null ? ret : def;
    }

    @api
    public static class send_tab_msg extends CHPlusFunction {
        @Override
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            new PlayerWrapper(args[0], t).sendPacket(
                    Packets.createPlayerListHeaderFooterPacket(args[1].val(), args[2].val())
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
                    Packets.createChatPacket(
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
                    Packets.createTitlePacket(fadeIn, stay, fadeOut),
                    Packets.createTitlePacket(EnumWrappers.TitleAction.TITLE, title),
                    Packets.createTitlePacket(EnumWrappers.TitleAction.SUBTITLE, subTitle)
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
                    Packets.createChatPacket(
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
            return new Class[0];
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
            Firework firework = (Firework) location.getWorld().launchFirework(
                    location, 0, Collections.singletonList(effect)).getHandle();

            Bukkit.getScheduler().runTaskLater(CommandHelperPlugin.self,
                    firework::detonate, 2);

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

    static abstract class CHPlusFunction extends AbstractFunction {
        @Override
        public boolean isRestricted() {
            return true;
        }

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
