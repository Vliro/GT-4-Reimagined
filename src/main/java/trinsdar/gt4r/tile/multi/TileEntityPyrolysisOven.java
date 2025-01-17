package trinsdar.gt4r.tile.multi;

import com.mojang.blaze3d.matrix.MatrixStack;
import muramasa.antimatter.capability.machine.MachineRecipeHandler;
import muramasa.antimatter.machine.event.IMachineEvent;
import muramasa.antimatter.machine.types.Machine;
import muramasa.antimatter.recipe.Recipe;
import muramasa.antimatter.tile.multi.TileEntityBasicMultiMachine;
import muramasa.antimatter.tile.multi.TileEntityMultiMachine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import trinsdar.gt4r.data.GT4RData;

import java.awt.Color;

public class TileEntityPyrolysisOven extends TileEntityBasicMultiMachine<TileEntityPyrolysisOven> {

    public TileEntityPyrolysisOven(Machine<?> type) {
        super(type);
        recipeHandler.set(() -> new PyrolysisRecipeHandler(this));
    }

    @Override
    public void onMachineEvent(IMachineEvent event, Object... data) {
        if (event instanceof TileEntityIndustrialBlastFurnace.BFEvent){
            recipeHandler.ifPresent(r -> {
                PyrolysisRecipeHandler r2 = (PyrolysisRecipeHandler) r;
                r2.heatingCapacity = 0;
                ItemStack stack = (ItemStack) data[0];
                if (!stack.isEmpty()){
                    if (stack.getItem() == GT4RData.CupronickelHeatingCoil){
                        r2.heatingCapacity += (100 * stack.getCount());
                    } else if (stack.getItem() == GT4RData.KanthalHeatingCoil) {
                        r2.heatingCapacity += (200 * stack.getCount());
                    } else {
                        r2.heatingCapacity += (300 * stack.getCount());
                    }
                }
            });

        }
        super.onMachineEvent(event, data);
    }
    //TODO
/*
    @Override
    public void drawInfo(MatrixStack stack, FontRenderer renderer, int left, int top) {
        // TODO: Replace by new TranslationTextComponent()
        this.recipeHandler.ifPresent(r -> {
            renderer.drawString(stack,"Heat: " + ((PyrolysisRecipeHandler)r).heatingCapacity + "K", 27, 62, Color.BLACK.getRGB());
        });
    }
*/
    public static class PyrolysisRecipeHandler extends MachineRecipeHandler<TileEntityPyrolysisOven> {
        protected final IIntArray GUI_SYNC_DATA = new IIntArray() {

            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return PyrolysisRecipeHandler.this.currentProgress;
                    case 1:
                        return PyrolysisRecipeHandler.this.maxProgress;
                    case 2:
                        return PyrolysisRecipeHandler.this.heatingCapacity;
                }
                return 0;
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        PyrolysisRecipeHandler.this.currentProgress = value;
                        break;
                    case 1:
                        PyrolysisRecipeHandler.this.maxProgress = value;
                        break;
                    case 2:
                        PyrolysisRecipeHandler.this.heatingCapacity = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
        private int heatingCapacity;
        public PyrolysisRecipeHandler(TileEntityPyrolysisOven tile) {
            super(tile);
        }

        @Override
        protected void activateRecipe(boolean reset) {
            //if (canOverclock)
            consumedResources = false;
            maxProgress = activeRecipe.getDuration();
            if (!generator){
                overclock = getOverclock();
                maxProgress = Math.max(1, maxProgress >>= overclock);
                float newProgress = maxProgress * ((float)heatingCapacity / 800);
                maxProgress *= Math.max(1, Math.round(newProgress));
            }
            tickTimer = 0;
            if (reset) {
                currentProgress = 0;
                tile.onRecipeActivated(activeRecipe);
            }
            lastRecipe = activeRecipe;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = super.serializeNBT();
            nbt.putInt("H", heatingCapacity);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            super.deserializeNBT(nbt);
            this.heatingCapacity = nbt.getInt("H");
        }
    }
}
