package trinsdar.gt4r.datagen;


import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.Data;
import muramasa.antimatter.block.BlockStone;
import muramasa.antimatter.datagen.providers.AntimatterLanguageProvider;
import muramasa.antimatter.fluid.AntimatterFluid;
import muramasa.antimatter.item.ItemFluidCell;
import muramasa.antimatter.machine.BlockMachine;
import muramasa.antimatter.machine.MachineFlag;
import muramasa.antimatter.machine.Tier;
import muramasa.antimatter.machine.types.Machine;
import muramasa.antimatter.material.Material;
import muramasa.antimatter.material.MaterialItem;
import muramasa.antimatter.material.MaterialTag;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import trinsdar.gt4r.Ref;
import trinsdar.gt4r.block.BlockCasing;
import trinsdar.gt4r.block.BlockCasingMachine;
import trinsdar.gt4r.block.BlockTurbineCasing;
import trinsdar.gt4r.data.GT4RData;
import trinsdar.gt4r.data.Machines;
import trinsdar.gt4r.items.ItemIntCircuit;
import net.minecraft.data.DataGenerator;
import trinsdar.gt4r.items.ItemMatch;
import trinsdar.gt4r.items.ItemPowerUnit;

import java.util.Collection;

import static muramasa.antimatter.Data.GEM;
import static muramasa.antimatter.Data.LIQUID;
import static muramasa.antimatter.Data.NULL;
import static muramasa.antimatter.machine.Tier.BRONZE;
import static muramasa.antimatter.machine.Tier.LV;
import static muramasa.antimatter.machine.Tier.MV;
import static muramasa.antimatter.machine.Tier.STEEL;
import static muramasa.antimatter.util.Utils.lowerUnderscoreToUpperSpaced;
import static muramasa.antimatter.util.Utils.lowerUnderscoreToUpperSpacedRotated;
import static muramasa.antimatter.util.Utils.validateNBT;
import static trinsdar.gt4r.data.Machines.ELECTROLYZER;
import static trinsdar.gt4r.data.Machines.HATCH_DYNAMO;
import static trinsdar.gt4r.data.Machines.MACERATOR;
import static trinsdar.gt4r.data.Materials.BROKEN_TURBINE_ROTOR;
import static trinsdar.gt4r.data.ToolTypes.ROCK_CUTTER;

public class GT4RLocalizations {

    public static class en_US extends AntimatterLanguageProvider {

        public en_US(DataGenerator gen) {
            super(Ref.ID, Ref.NAME + " en_us Localization", "en_us", gen);
        }

        @Override
        protected void addTranslations() {
            super.addTranslations();
            add(Ref.ID + ".advancements.gt4r", "GT4 Renewed");
            add(Ref.ID + ".advancements.gt4r.desc", "Getting familiar with your surroundings");
            add(Ref.ID + ".tooltip.occurrence", "Indicates occurrence of ");
            add("block.gt4r.rubber_leaves", "Rubber Leaves");
            add("block.gt4r.rubber_log", "Rubber Log");
            add("block.gt4r.rubber_sapling", "Rubber Sapling");
            add("attribute.name.generic.gt4r.attackReach", "Attack Reach");;
            add("message.gt4r.pickaxe_torch_right_click", "Fyi there is no need to put a torch in your offhand, just right click with a pickaxe");
        }

        @Override
        protected void processTranslations(String domain, String locale) {
            super.processTranslations(domain, locale);
            AntimatterAPI.all(BlockCasing.class, domain).forEach(i -> add(i, lowerUnderscoreToUpperSpaced(i.getId())));
            AntimatterAPI.all(ItemMatch.class, domain).forEach(i -> {
                String value = lowerUnderscoreToUpperSpaced(i.getId());
                if (i == GT4RData.Lighter) value = value.concat(" (Full)");
                add(i, value);
            });
            AntimatterAPI.all(ItemPowerUnit.class, domain).forEach(i -> {
                String value = lowerUnderscoreToUpperSpaced(i.getId());
                if (i.getId().startsWith("power_unit")) value = lowerUnderscoreToUpperSpacedRotated(i.getId());
                add(i, value);
            });
            AntimatterAPI.all(ItemIntCircuit.class, domain).forEach(i -> add(i, "Integrated Circuit (" + i.circuitId + ")"));
            add(GT4RData.MixedMetal, lowerUnderscoreToUpperSpaced(GT4RData.MixedMetal.getId()));
            add(GT4RData.SAP_BAG, lowerUnderscoreToUpperSpaced(GT4RData.SAP_BAG.getId()));
            add(GT4RData.CraftingModule, lowerUnderscoreToUpperSpaced(GT4RData.CraftingModule.getId()));
            override(ROCK_CUTTER.getToolStack(NULL, NULL).getItem().getTranslationKey(), "Rock Cutter");
            override(GT4RData.LighterEmpty.getTranslationKey(), "Lighter (Empty)");
            AntimatterAPI.all(Machine.class, domain).forEach(i -> {
                Collection<Tier> tiers =  i.getTiers();
                if (i.has(MachineFlag.BASIC)) {
                    tiers.forEach(t -> {
                        String value = lowerUnderscoreToUpperSpacedRotated(i.getId() + "_" + t.getId());
                        String id = i == MACERATOR && t == MV ? "universal_macerator" : i == ELECTROLYZER ? t == LV ? "basic_electrolyzer" : "industrial_electrolyzer" : "";
                        if (i.getId().contains("battery_buffer")){
                            String tier = lowerUnderscoreToUpperSpaced(t.getId());
                            String number = value.contains("One") ? " 1x " : value.contains("Four") ? " 4x " : " 8x ";
                            String afterTier = lowerUnderscoreToUpperSpaced("battery_buffer");
                            override("machine." + i.getId() + "." + t.getId(), tier + number + afterTier);
                            return;
                        }
                        override("machine." + i.getId() + "." + t.getId(), value.contains("Infinite") || value.contains("Transformer") || value.contains("Battery") || t == BRONZE || t == STEEL || i == HATCH_DYNAMO ? value : lowerUnderscoreToUpperSpaced(id.isEmpty() ? i.getId() : id));
                    });
                }
            });
            AntimatterAPI.all(AntimatterFluid.class, domain).forEach(s -> {
                String mat;
                if (s.getId().startsWith("liquid") || s.getId().startsWith("plasma")){
                    mat = s.getId().substring(7);
                } else {
                    mat = s.getId().substring(4);
                }
                String id = mat;
                if (s.getId().startsWith("plasma")){
                    id = mat + "_plasma";
                }
                Material m = Material.get(mat);
                if (m != NULL){
                    if (m.has(LIQUID) && m.has(MaterialTag.METAL)){
                        id = "molten_" + mat;
                    }
                    override(s.getAttributes().getTranslationKey(), lowerUnderscoreToUpperSpaced(id));
                    Item bucket = AntimatterAPI.get(Item.class, s.getId()+ "_bucket");
                    if (bucket != null) override(bucket.getTranslationKey(), lowerUnderscoreToUpperSpaced(id) + " Bucket");
                }
            });
            AntimatterAPI.all(ItemFluidCell.class, domain).forEach(i -> override(i.getTranslationKey(), lowerUnderscoreToUpperSpacedRotated(i.getId())));
            GEM.all().forEach(m -> override(GEM.get(m).getTranslationKey(), lowerUnderscoreToUpperSpaced(m.getId())));
            BROKEN_TURBINE_ROTOR.all().forEach(m -> override(BROKEN_TURBINE_ROTOR.get(m).getTranslationKey(), lowerUnderscoreToUpperSpaced(m.getId() + "_" + BROKEN_TURBINE_ROTOR.getId())));
        }
    }

}
