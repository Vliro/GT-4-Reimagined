package trinsdar.gt4r.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import muramasa.antimatter.capability.IGuiHandler;
import muramasa.antimatter.gui.GuiInstance;
import muramasa.antimatter.gui.IGuiElement;
import muramasa.antimatter.gui.Widget;
import muramasa.antimatter.gui.container.ContainerMachine;
import muramasa.antimatter.gui.container.ContainerMultiMachine;
import muramasa.antimatter.gui.screen.AntimatterContainerScreen;
import muramasa.antimatter.gui.widget.MachineStateWidget;
import muramasa.antimatter.gui.widget.WidgetSupplier;
import muramasa.antimatter.integration.jei.AntimatterJEIPlugin;
import muramasa.antimatter.machine.MachineFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import trinsdar.gt4r.Ref;
import trinsdar.gt4r.tile.multi.TileEntityFusionReactor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FusionButtonWidget extends Widget {
    ResourceLocation middle = new ResourceLocation(Ref.ID, "textures/gui/machine/fusion_computer_middle_overlay.png");
    ResourceLocation top_bottom = new ResourceLocation(Ref.ID, "textures/gui/machine/fusion_computer_top_bottom_overlay.png");

    protected FusionButtonWidget(@Nonnull GuiInstance gui, @Nullable IGuiElement parent) {
        super(gui, parent);
        setW(18);
        setH(18);
    }

    @Override
    public void mouseOver(MatrixStack stack, double mouseX, double mouseY, float partialTicks) {
        super.mouseOver(stack, mouseX, mouseY, partialTicks);
        if (getTile().has(MachineFlag.RECIPE)) {
            renderTooltip(stack, new StringTextComponent("Show Recipes"), mouseX, mouseY);
        }
    }

    private TileEntityFusionReactor getTile() {
        return (TileEntityFusionReactor) gui.handler;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        AntimatterJEIPlugin.showCategory(getTile().getMachineType());
    }

    public static WidgetSupplier build() {
        return builder(FusionButtonWidget::new);
    }

    @Override
    public void render(MatrixStack matrixStack, double mouseX, double mouseY, float partialTicks) {
        TileEntityFusionReactor tile = getTile();
        if (tile.getDisplay() == TileEntityFusionReactor.Display.REGULAR){
            drawTexture(matrixStack, gui.handler.getGuiTexture(), realX() + 154, realY() + 22, 176, 0, 18, 18);
        } else if (tile.getDisplay() == TileEntityFusionReactor.Display.MIDDLE){
            drawTexture(matrixStack, gui.handler.getGuiTexture(), realX() + 6, realY()+ 22 + getH(), 176, 18, 18, 18);
            drawTexture(matrixStack, middle, realX() + 6, realY() + 6, 0, 0, 145, 145);
        } else {
            drawTexture(matrixStack, gui.handler.getGuiTexture(), realX() + 154, realY() + 58, 176, 36, 18, 18);
            drawTexture(matrixStack, top_bottom, realX() + 6, realY() + 6, 0, 0, 145, 145);
        }
    }
}
