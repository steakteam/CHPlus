package io.github.steakteam.chplus.event;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import io.github.steakteam.chplus.event.internal.MCInventoryMoveItemEvent;
import io.github.steakteam.chplus.util.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JunHyeong Lim on 2018-12-25
 */
@api
public class InventoryMoveItem extends CHPlusAbstractEvent {
    @Override
    public String getName() {
        return "inventory_move_item";
    }

    @Override
    public String docs() {
        return "";
    }

    @Override
    public boolean matches(Map<String, Mixed> map, BindableEvent bindableEvent) {
        return bindableEvent instanceof MCInventoryMoveItemEvent;
    }

    @Override
    public Map<String, Mixed> evaluate(BindableEvent bindableEvent) throws EventException {
        if (bindableEvent instanceof MCInventoryMoveItemEvent) {
            Target t = Target.UNKNOWN;
            MCInventoryMoveItemEvent e = ((MCInventoryMoveItemEvent) bindableEvent);
            Map<String, Mixed> ret = new HashMap<>();
            ret.put("source", Tools.toArray(t, e.getSource()));
            ret.put("destination", Tools.toArray(t, e.getDestination()));
            ret.put("initiator", new CString(e.getInitiator() == e.getSource() ? "source" : "destination", t));
            ret.put("item", ObjectGenerator.GetGenerator().item(e.getItem(), t));
            return ret;
        }
        throw new EventException();
    }

    @Override
    public boolean modifyEvent(String s, Mixed construct, BindableEvent bindableEvent) {
        return false;
    }
}
