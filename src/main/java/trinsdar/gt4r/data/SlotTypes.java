package trinsdar.gt4r.data;

import muramasa.antimatter.gui.SlotType;
import muramasa.antimatter.gui.slot.AbstractSlot;
import muramasa.antimatter.machine.event.ContentEvent;
import muramasa.antimatter.tool.IAntimatterTool;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.wrapper.EmptyHandler;
import tesseract.api.capability.TesseractGTCapability;
import trinsdar.gt4r.gui.slots.SlotCoil;
import trinsdar.gt4r.gui.slots.SlotCrafting;
import trinsdar.gt4r.gui.slots.SlotDisplay;
import trinsdar.gt4r.items.ItemTurbineRotor;

public class SlotTypes {
    public static SlotType<SlotCoil> COIL = new SlotType<>("coil_bf", (type, gui, item, i, d) -> new SlotCoil(type,  gui,item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<SlotDisplay> DISPLAY = new SlotType<>("display", (type, gui, item, i, d) -> new SlotDisplay(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> false, ContentEvent.ITEM_INPUT_CHANGED, false, false);
    public static SlotType<AbstractSlot> STORAGE = new SlotType<>("storage", (type, gui, item, i, d) -> new AbstractSlot(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot> TOOLS = new SlotType<>("tools", (type, gui, item, i, d) -> new AbstractSlot(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> i.getItem() instanceof IAntimatterTool || i.getItem().isDamageable(), ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot> TOOL_CHARGE = new SlotType<>("tool_charge", (type, gui, item, i, d) -> new AbstractSlot(type,gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> {
        if (t instanceof ICapabilityProvider) {
            return ((ICapabilityProvider)t).getCapability(TesseractGTCapability.ENERGY_HANDLER_CAPABILITY).map(eh -> i.getCapability(TesseractGTCapability.ENERGY_HANDLER_CAPABILITY).map(inner -> ((inner.getInputVoltage() | inner.getOutputVoltage()) <= (eh.getInputVoltage() | eh.getOutputVoltage()) )).orElse(false)).orElse(false) || i.getItem() instanceof IAntimatterTool || i.getItem().isDamageable();
        }
        return true;
    }, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<SlotCrafting> CRAFTING = new SlotType<>("crafting", (type, gui, item, i, d) -> new SlotCrafting(item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot> PARK = new SlotType<>("park", (type, gui, item, i, d) -> new AbstractSlot(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> true, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot> ROTOR = new SlotType<>("rotor", ((type, gui, item, i, d) -> new AbstractSlot(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY())), (t, i) -> i.getItem() instanceof ItemTurbineRotor, ContentEvent.ITEM_INPUT_CHANGED);
    public static SlotType<AbstractSlot> FILTER = new SlotType<>("filter", (type, gui, item, i, d) -> new AbstractSlot(type, gui, item.getOrDefault(type, new EmptyHandler()), i, d.getX(), d.getY()), (t, i) -> i.getItem() == GT4RData.LavaFilter, ContentEvent.ITEM_INPUT_CHANGED);
}
