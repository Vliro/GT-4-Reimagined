package trinsdar.gt4r.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import muramasa.antimatter.capability.IGuiHandler;
import muramasa.antimatter.gui.GuiInstance;
import muramasa.antimatter.gui.IGuiElement;
import muramasa.antimatter.gui.container.ContainerMachine;
import muramasa.antimatter.gui.screen.AntimatterContainerScreen;
import muramasa.antimatter.gui.widget.MachineStateWidget;
import muramasa.antimatter.gui.widget.WidgetSupplier;
import muramasa.antimatter.machine.MachineFlag;
import muramasa.antimatter.machine.MachineState;
import muramasa.antimatter.tile.TileEntityMachine;
import muramasa.antimatter.util.int2;

public class MachineStateWidgetMoved extends MachineStateWidget {
    final int2 location;

    protected MachineStateWidgetMoved(GuiInstance gui, IGuiElement parent, int2 loc) {
        super(gui, parent);
        this.location = loc;
    }

    @Override
    public void render(MatrixStack stack, double mouseX, double mouseY, float partialTicks) {
        TileEntityMachine<?> m = ((TileEntityMachine<?>) gui.handler);
        MachineState state = m.getMachineState();
        //Draw error.
        if (isRecipe) {
            if (state == MachineState.POWER_LOSS) {
                drawTexture(stack, gui.handler.getGuiTexture(), realX(), realY(), this.location.x, this.location.y, this.getW(), this.getH());
            }
        }
    }

    public static WidgetSupplier build(int xLoc, int yLoc) {
        return builder((screen, handler) -> new MachineStateWidgetMoved(screen, handler, new int2(xLoc, yLoc)));
    }
}
