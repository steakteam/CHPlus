package kr.rvs.chplus.function;

import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import kr.rvs.chplus.util.CHPlusFactory;
import kr.rvs.chplus.util.GUIHelper;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by JunHyeong Lim on 2018-03-13
 */
@api
public class AnvilUserInput extends CHPlusFunction {
    @Override
    public Construct exec(final Target t, Environment env, Construct... args) throws ConfigRuntimeException {
        Queue<Construct> queue = new ArrayDeque<>(Arrays.asList(args));
        MCPlayer player = queue.size() >= 2
                ? Static.GetPlayer(queue.poll(), t)
                : Static.getPlayer(env, t);
        CClosure closure = Static.getObject(queue.poll(), t, CClosure.class);
        GUIHelper helper = new GUIHelper(InventoryType.ANVIL)
                .putListener(new AnvilInputHandler(closure, t));
        helper.putItem(0, queue.isEmpty()
                ? CHPlusFactory.createDefaultAnvilInputItem()
                : ObjectGenerator.GetGenerator().item(queue.poll(), t));

        helper.open(player);
        return CVoid.VOID;
    }

    @Override
    public String getName() {
        return "user_input";
    }

    @Override
    public Integer[] numArgs() {
        return new Integer[]{1, 2, 3};
    }

    @Override
    public String docs() {
        return "void {[player], callback closure, [item array]} " +
                "Open an Anvil GUI input for player, calling the callback closure when the player submits the input. " +
                "The text the player typed in gets returned to the closure. " +
                "Item can be an item array, already only the keys 'type', 'data' and 'display' are used. ";
    }

    private static class AnvilInputHandler implements GUIHelper.GUIHandler {
        private static final int OUTPUT_SLOT = 2;
        private final CClosure closure;
        private final Target target;

        public AnvilInputHandler(CClosure closure, Target target) {
            this.closure = closure;
            this.target = target;
        }

        @Override
        public void onEvent(InventoryEvent e) {
            if (e instanceof InventoryClickEvent) {
                InventoryClickEvent clickEvent = ((InventoryClickEvent) e);
                clickEvent.setCancelled(true);
                int rawSlot = clickEvent.getRawSlot();
                if (rawSlot == OUTPUT_SLOT) {
                    // Get input
                    String name = "";
                    ItemStack item = clickEvent.getCurrentItem();
                    if (item != null && item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.getDisplayName() != null)
                            name = meta.getDisplayName();
                    }

                    e.getView().getTopInventory().clear();
                    clickEvent.getWhoClicked().closeInventory();
                    closure.execute(new CString(name, target));
                }
            } else if (e instanceof InventoryCloseEvent) {
                e.getInventory().clear();
            }
        }
    }
}
