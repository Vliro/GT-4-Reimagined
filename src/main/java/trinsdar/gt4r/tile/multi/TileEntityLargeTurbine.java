package trinsdar.gt4r.tile.multi;

import muramasa.antimatter.capability.machine.MachineRecipeHandler;
import muramasa.antimatter.machine.types.Machine;
import muramasa.antimatter.recipe.Recipe;
import muramasa.antimatter.tile.multi.TileEntityMultiMachine;
import muramasa.antimatter.util.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import trinsdar.gt4r.data.Materials;
import trinsdar.gt4r.data.SlotTypes;
import trinsdar.gt4r.items.ItemTurbineRotor;


public class TileEntityLargeTurbine extends TileEntityMultiMachine<TileEntityLargeTurbine> {

    public TileEntityLargeTurbine(Machine type) {
        super(type);
        this.recipeHandler.set(() ->
            new MachineRecipeHandler<TileEntityLargeTurbine>(this) {

                private Recipe sourceRecipe;
                private int ticker = 0;
                private double efficiency;

                @Override
                protected boolean validateRecipe(Recipe r) {
                    return true;
                }

                @Override
                public Recipe findRecipe() {
                    Recipe r = super.findRecipe();
                    if (r == null) return null;
                    sourceRecipe = r;
                    //Source recipe contains fluid amounts, map to turbine
                    FluidStack[] stacks = r.getInputFluids();
                    if (stacks == null || stacks.length == 0) return null;
                    //ItemStack t = tile.itemHandler.map(tt -> tt.getSpecial()).orElse(ItemStack.EMPTY);
                    //if (!(t.getItem() instanceof ItemTurbine)) return null;
                   // ItemTurbine turbine = (ItemTurbine) t.getItem();
                    int flow = 120;//turbine.optimalEUT;
                    efficiency = getEfficiency();//turbine.efficency;
                    if (efficiency <= 0.0F) return null;
                    long toConsume = calculateGeneratorConsumption(flow, sourceRecipe);

                    return Utils.getFluidPoweredRecipe(new FluidStack[]{new FluidStack(stacks[0].getFluid(),(int) toConsume)},
                            new FluidStack[0],
                           // new FluidStack[]{new FluidStack(DistilledWater.getLiquid(), stacks[0].getAmount())},// Arrays.stream(sourceRecipe.getOutputFluids()).map(tt -> new FluidStack(tt.getFluid(), (int) (tt.getAmount()*toConsume))).toArray(FluidStack[]::new),
                            1, flow,1);
                }
                @Override
                public void consumeInputs() {

                }

                @Override
                protected boolean consumeGeneratorResources(boolean simulate) {
                    if (!activeRecipe.hasInputFluids()) {
                        throw new RuntimeException("Missing fuel in active generator recipe!");
                    }
                    //boolean shouldRun = tile.energyHandler.map(h -> h.insert((long)(tile.getMachineType().getMachineEfficiency()*(double)tile.getMachineTier().getVoltage()),true) > 0).orElse(false);
                    ///if (!shouldRun) return false;
                    int recipeAmount = activeRecipe.getInputFluids()[0].getAmount();
                    long toConsume = recipeAmount; // calculateGeneratorConsumption(tile.getMachineTier().getVoltage(), activeRecipe);// (long) ((double)tile.getMachineTier().getVoltage() / (activeRecipe.getPower() /(double) Objects.requireNonNull(activeRecipe.getInputFluids())[0].getAmount()));
                    int consumed = tile.fluidHandler.map(h -> {
                        /*
                            How much wiggle room? So, at optimal flow : generate regular. Otherwise, dampen by a factor of 1/(optimal-current) or 1/(current-optimal). Allow
                            consuming up to 1.5x optimal
                         */
                        int amount = h.getInputTanks().drain(new FluidStack(activeRecipe.getInputFluids()[0],(int)(toConsume*1.5)), IFluidHandler.FluidAction.SIMULATE).getAmount();

                        if (amount > 0) {
                            if (!simulate)
                                h.getInputTanks().drain(new FluidStack(activeRecipe.getInputFluids()[0], amount), IFluidHandler.FluidAction.EXECUTE);
                            return amount;
                        }
                        return 0;
                    }).orElse(0);
                    if (consumed > 0 && efficiency > 0.0F){
                        if (consumed < recipeAmount) consumed *= Math.pow(1d/(recipeAmount-consumed),0.04);
                        if (consumed > recipeAmount) consumed *= Math.pow(1d/(consumed-recipeAmount),0.04);
                        //Input energy
                        int finalConsumed = consumed;
                        if (ticker == 20){
                            ticker = 0;
                            tile.itemHandler.ifPresent(h -> {
                                ItemStack copy = h.getHandler(SlotTypes.ROTOR).getStackInSlot(0).copy();
                                if (h.getHandler(SlotTypes.ROTOR).getStackInSlot(0).attemptDamageItem(1, tile.world.rand, null)){
                                    if (copy.getItem() instanceof ItemTurbineRotor){
                                        h.getHandler(SlotTypes.ROTOR).setStackInSlot(0, Materials.BROKEN_TURBINE_ROTOR.get(((ItemTurbineRotor)copy.getItem()).getMaterial(), 1));
                                    }
                                }
                            });
                        }
                        ticker++;
                        if (!simulate) tile.energyHandler.ifPresent(handler -> {
                            handler.insert((long) (efficiency*activeRecipe.getPower()*finalConsumed/ recipeAmount), false);
                        });
                        return true;
                    }
                    return false;
                }

                public float getEfficiency(){
                    ItemStack stack = tile.itemHandler.map(i -> i.getHandler(SlotTypes.ROTOR).getStackInSlot(0)).orElse(ItemStack.EMPTY);
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemTurbineRotor){
                        return ((ItemTurbineRotor)stack.getItem()).getRotorEfficiency();
                    }
                    return 0.0F;
                }
            }
        );
    }
}
