package trinsdar.gt4r.datagen;

import com.google.common.collect.ImmutableMap;
import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.datagen.providers.AntimatterRecipeProvider;
import muramasa.antimatter.material.Material;
import muramasa.antimatter.ore.BlockOre;
import muramasa.antimatter.recipe.ingredient.PropertyIngredient;
import muramasa.antimatter.tool.AntimatterToolType;
import muramasa.antimatter.tool.IAntimatterTool;
import muramasa.antimatter.util.TagUtils;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import trinsdar.gt4r.Ref;
import trinsdar.gt4r.data.GT4RData;
import trinsdar.gt4r.loader.MaterialRecipeLoader;
import trinsdar.gt4r.loader.crafting.BlockCrafting;
import trinsdar.gt4r.loader.crafting.MachineCrafting;
import trinsdar.gt4r.loader.crafting.Parts;
import trinsdar.gt4r.loader.crafting.ToolCrafting;
import trinsdar.gt4r.loader.crafting.ToolCraftingTableRecipes;
import trinsdar.gt4r.loader.machines.FurnaceLoader;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;
import static muramasa.antimatter.Data.*;
import static muramasa.antimatter.material.MaterialTag.GRINDABLE;
import static muramasa.antimatter.material.MaterialTag.HANDLE;
import static muramasa.antimatter.material.MaterialTag.RUBBERTOOLS;
import static muramasa.antimatter.util.TagUtils.getForgeItemTag;
import static muramasa.antimatter.util.TagUtils.nc;
import static muramasa.antimatter.util.Utils.getConventionalMaterialType;
import static muramasa.antimatter.util.Utils.getConventionalStoneType;
import static trinsdar.gt4r.data.Materials.Steel;
import static trinsdar.gt4r.loader.crafting.CraftingHelper.criterion;

public class GT4RRecipes extends AntimatterRecipeProvider {

    public GT4RRecipes(String providerDomain, String providerName, DataGenerator gen) {
        super(providerDomain, providerName, gen);
        registerCraftingLoaders();
        GT4RData.buildTierMaps();
    }

    protected void registerCraftingLoaders() {
        this.craftingLoaders.add(Parts::loadRecipes);
        this.craftingLoaders.add(ToolCraftingTableRecipes::loadRecipes);
        this.craftingLoaders.add(MachineCrafting::loadRecipes);
        this.craftingLoaders.add(MaterialRecipeLoader::loadRecipes);
        this.craftingLoaders.add(FurnaceLoader::loadRecipes);
        this.craftingLoaders.add(BlockCrafting::loadRecipes);
        this.craftingLoaders.add(ToolCrafting::loadRecipes);
    }

    @Override
    public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerToolRecipes(consumer, muramasa.antimatter.Ref.ID);
        craftingLoaders.forEach(cl -> cl.loadRecipes(consumer,this));
        addConditionalRecipe(consumer, getStackRecipe("", "has_sulfur_dust", criterion(getForgeItemTag("dusts/sulfur"), this),
                new ItemStack(Blocks.TORCH, 6), of('D', getForgeItemTag("dusts/sulfur"), 'R', Tags.Items.RODS_WOODEN), "D", "R"), Ref.class, "sulfurTorch", Ref.ID, "sulfur_torch");
        addItemRecipe(consumer, Ref.ID, "chainmail_helmet", "chainmail_armor", "has_hammer", hasSafeItem(HAMMER.getTag()),
                Items.CHAINMAIL_HELMET, of('R', RING.getMaterialTag(Steel), 'H', HAMMER.getTag()), "RRR", "RHR");
        addItemRecipe(consumer, Ref.ID, "chainmail_chestplate", "chainmail_armor", "has_hammer", hasSafeItem(HAMMER.getTag()),
                Items.CHAINMAIL_CHESTPLATE, of('R', RING.getMaterialTag(Steel), 'H', HAMMER.getTag()), "RHR", "RRR", "RRR");
        addItemRecipe(consumer, Ref.ID, "chainmail_leggings", "chainmail_armor", "has_hammer", hasSafeItem(HAMMER.getTag()),
                Items.CHAINMAIL_LEGGINGS, of('R', RING.getMaterialTag(Steel), 'H', HAMMER.getTag()), "RRR", "RHR", "R R");
        addItemRecipe(consumer, Ref.ID, "chainmail_boots", "chainmail_armor", "has_hammer", hasSafeItem(HAMMER.getTag()),
                Items.CHAINMAIL_BOOTS, of('R', RING.getMaterialTag(Steel), 'H', HAMMER.getTag()), "R R", "RHR");
        addItemRecipe(consumer, Ref.ID, "saddle", "", "has_leather", hasSafeItem(Items.LEATHER), Items.SADDLE,
                of('L', Items.LEATHER, 'R', RING.getMaterialTag(Steel), 'S', SCREW.getMaterialTag(Steel)), "LLL", "LSL", "R R");
        addItemRecipe(consumer,  Ref.ID,"sticky_piston_from_resin", "", "has_piston", criterion(Blocks.PISTON, this), Blocks.STICKY_PISTON, of('S', GT4RData.StickyResin, 'P', Blocks.PISTON), "S", "P");
        shapeless(consumer, "gravel_to_flint", "mortar_recipes", "has_mortar", hasSafeItem(MORTAR.getTag()), new ItemStack(Items.FLINT), MORTAR.getTag(), Items.GRAVEL);
    }

    @Override
    protected void registerToolRecipes(Consumer<IFinishedRecipe> consumer, String providerDomain) {
        if (providerDomain.equals(muramasa.antimatter.Ref.ID)){
            final ICriterionInstance in = this.hasSafeItem(WRENCH.getTag());


            addToolRecipe(TOOL_BUILDER.get(HAMMER.getId()), consumer, muramasa.antimatter.Ref.ID, HAMMER.getId() + "_" +"recipe", "antimatter_tools",
                    "has_wrench", in, Collections.singletonList(HAMMER.getToolStack(NULL, NULL)), of('I', PropertyIngredient.builder("primary").types(INGOT, GEM).tool(HAMMER, true).build(), 'R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build()), "II ", "IIR", "II ");

            addToolRecipe(ARMOR_BUILDER.get(HELMET.getId()), consumer, muramasa.antimatter.Ref.ID, HELMET.getId() + "_recipe", "antimatter_helmets",
                    "has_wrench", in, Collections.singletonList(HELMET.getToolStack(NULL)), of('I', PropertyIngredient.builder("primary").types(PLATE, GEM).tags(ARMOR).build(), 'H', HAMMER.getTag()), "III", "IHI");
            addToolRecipe(ARMOR_BUILDER.get(CHESTPLATE.getId()), consumer, muramasa.antimatter.Ref.ID, CHESTPLATE.getId() + "_recipe", "antimatter_chestplates",
                    "has_wrench", in, Collections.singletonList(CHESTPLATE.getToolStack(NULL)), of('I', PropertyIngredient.builder("primary").types(PLATE, GEM).tags(ARMOR).build(), 'H', HAMMER.getTag()), "IHI", "III", "III");
            addToolRecipe(ARMOR_BUILDER.get(LEGGINGS.getId()), consumer, muramasa.antimatter.Ref.ID, LEGGINGS.getId() + "_recipe", "antimatter_leggings",
                    "has_wrench", in, Collections.singletonList(LEGGINGS.getToolStack(NULL)), of('I', PropertyIngredient.builder("primary").types(PLATE, GEM).tags(ARMOR).build(), 'H', HAMMER.getTag()), "III", "IHI", "I I");
            addToolRecipe(ARMOR_BUILDER.get(BOOTS.getId()), consumer, muramasa.antimatter.Ref.ID, BOOTS.getId() + "_recipe", "antimatter_boots",
                    "has_wrench", in, Collections.singletonList(BOOTS.getToolStack(NULL)), of('I', PropertyIngredient.builder("primary").types(PLATE, GEM).tags(ARMOR).build(), 'H', HAMMER.getTag()), "I I", "IHI");

            /*addToolRecipe(TOOL_BUILDER.get(PLUNGER.getId()), consumer, muramasa.antimatter.Ref.ID, PLUNGER.getId() + "_recipe", "antimatter_plungers",
                    "has_wrench", in, Collections.singletonList(PLUNGER.getToolStack(NULL, NULL)),
                    of('W', WIRE_CUTTER.getTag(), 'I',  PropertyIngredient.of(INGOT, "primary"), 'S', Tags.Items.SLIMEBALLS, 'R', PropertyIngredient.builder("secondary").types(ROD).tags(RUBBERTOOLS).build(), 'F', FILE.getTag()), "WIS", " RI", "R F");*/

            addToolRecipe(TOOL_BUILDER.get(WRENCH.getId()), consumer, muramasa.antimatter.Ref.ID, WRENCH.getId() + "_recipe", "antimatter_wrenches",
                    "has_wrench", in, WRENCH.getToolStack(NULL, NULL), of('I', PropertyIngredient.builder("primary").types(INGOT, GEM).tool(WRENCH, true).build(), 'H', HAMMER.getTag()), "IHI", "III", " I ");

            addToolRecipe(TOOL_BUILDER.get(MORTAR.getId()), consumer, muramasa.antimatter.Ref.ID, MORTAR.getId() + "_recipe", "antimatter_mortars",
                    "has_wrench", in, MORTAR.getToolStack(NULL, NULL), of('I', PropertyIngredient.builder("primary").types(INGOT, GEM).tool(MORTAR, true).build(), 'S', Tags.Items.STONE), " I ", "SIS", "SSS");

            addToolRecipe(TOOL_BUILDER.get(FILE.getId()), consumer, muramasa.antimatter.Ref.ID, FILE.getId() + "_recipe", "antimatter_files",
                    "has_wrench", in, FILE.getToolStack(NULL, NULL), of('P', PropertyIngredient.builder("primary").types(PLATE, GEM).tool(FILE, true).build(), 'R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build()), "P", "P", "R");

            addToolRecipe(TOOL_BUILDER.get(SCREWDRIVER.getId()), consumer, muramasa.antimatter.Ref.ID, SCREWDRIVER.getId() + "_recipe", "antimatter_screwdrivers",
                    "has_wrench", in, SCREWDRIVER.getToolStack(NULL, NULL),
                    of('M', PropertyIngredient.builder("primary").types(ROD).tool(SCREWDRIVER, true).build(), 'R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build(), 'F', FILE.getTag(), 'H', HAMMER.getTag()), " FM", " MH", "R  ");

            addToolRecipe(TOOL_BUILDER.get(SAW.getId()), consumer, muramasa.antimatter.Ref.ID, SAW.getId() + "_recipe", "antimatter_saws",
                    "has_wrench", in, SAW.getToolStack(NULL, NULL), of('P', PropertyIngredient.of(PLATE, "primary"), 'R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build(), 'F', FILE.getTag(), 'H', HAMMER.getTag()), "PPR", "FH ");

            addToolRecipe(TOOL_BUILDER.get(WIRE_CUTTER.getId()), consumer, muramasa.antimatter.Ref.ID, WIRE_CUTTER.getId() + "_recipe_noscrew", "antimatter_files",
                    "has_wrench", in, WIRE_CUTTER.getToolStack(NULL, NULL), b ->
                            b.put('P', PropertyIngredient.builder("primary").inverse().tool(SCREWDRIVER, true).types(PLATE, GEM).tags(SCREW).build()).put('R',PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build()).put('F', FILE.getTag()).put('H', HAMMER.getTag())
                                    .put('S', SCREWDRIVER.getTag())
                    , "PFP", "HPS", "R R");
            addToolRecipe(TOOL_BUILDER.get(WIRE_CUTTER.getId()), consumer, muramasa.antimatter.Ref.ID, WIRE_CUTTER.getId() + "_recipe_screw", "antimatter_files",
                    "has_wrench", in, WIRE_CUTTER.getToolStack(NULL, NULL), b ->
                            b.put('P', PropertyIngredient.builder("primary").types(PLATE, GEM).tags(SCREW).build()).put('R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build()).put('F', FILE.getTag()).put('H', HAMMER.getTag())
                                    .put('S', SCREWDRIVER.getTag()).put('W', PropertyIngredient.of(SCREW, "primary"))
                    , "PFP", "HPS", "RWR");

            Function<AntimatterToolType, ImmutableMap<Character, Object>> map1 = type -> of('I', PropertyIngredient.builder("primary").types(INGOT, GEM).tool(type, true).build(), 'R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build(), 'P', PropertyIngredient.builder("primary").types(PLATE, GEM).tool(type, true).build(), 'F', FILE.getTag(), 'H', HAMMER.getTag());

            Function<AntimatterToolType, ImmutableMap<Character, Object>> map2 = type -> of('R', PropertyIngredient.builder("secondary").types(ROD).tags(HANDLE).build(), 'P', PropertyIngredient.builder("primary").types(PLATE, GEM).tool(type, true).build(), 'F', FILE.getTag(), 'H', HAMMER.getTag());

            String[] strings1 = new String[]{"PII", "FRH", " R "};
            String[] strings3 = new String[]{" P ", "FPH", " R "};

            String[] strings2 = new String[]{"FPH", " R ", " R "};
            String[] strings2Gem = new String[]{"FGH", " R ", " R "};

            addToolRecipe(TOOL_BUILDER.get(PICKAXE.getId()), consumer, muramasa.antimatter.Ref.ID, PICKAXE.getId() + "_with" , "antimatter_pickaxes",
                    "has_wrench", in, PICKAXE.getToolStack(NULL, NULL), map1.apply(PICKAXE), strings1);

            addToolRecipe(TOOL_BUILDER.get(SHOVEL.getId()), consumer, muramasa.antimatter.Ref.ID, SHOVEL.getId() + "_with" , "antimatter_shovels",
                    "has_wrench", in, SHOVEL.getToolStack(NULL, NULL), map2.apply(SHOVEL), strings2);

            addToolRecipe(TOOL_BUILDER.get(AXE.getId()), consumer, muramasa.antimatter.Ref.ID, AXE.getId() + "_with" , "antimatter_axes",
                    "has_wrench", in, AXE.getToolStack(NULL, NULL), map1.apply(AXE), "PIH", "PR ", "FR ");


            addToolRecipe(TOOL_BUILDER.get(SWORD.getId()), consumer, muramasa.antimatter.Ref.ID, SWORD.getId() + "_with" , "antimatter_swords",
                    "has_wrench", in, SWORD.getToolStack(NULL, NULL), map2.apply(SWORD), " P ", "FPH", " R ");

            addToolRecipe(TOOL_BUILDER.get(HOE.getId()), consumer, muramasa.antimatter.Ref.ID, HOE.getId() + "_with" , "antimatter_swords",
                    "has_wrench", in, HOE.getToolStack(NULL, NULL), map1.apply(HOE), "PIH", "FR ", " R ");

            addToolRecipe(CROWBAR_BUILDER.get(CROWBAR.getId()),  consumer, muramasa.antimatter.Ref.ID, CROWBAR.getId() + "_recipe", "antimatter_crowbars",
                    "has_wrench", in, CROWBAR.getToolStack(NULL, NULL), of('H', HAMMER.getTag(), 'C', PropertyIngredient.builder("secondary").itemTags(Tags.Items.DYES).build(), 'R', PropertyIngredient.builder("primary").types(ROD).tool(CROWBAR, true).build(), 'F', FILE.getTag()), "HCR", "CRC", "RCF");
        }

        List<Material> handleMats = AntimatterAPI.all(Material.class).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.isHandle())).collect(Collectors.toList());

        handleMats.forEach(handle -> AntimatterAPI.all(Material.class).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.has(RUBBERTOOLS))).forEach(rubber -> {
            ITag<Item> ingotTag = TagUtils.getForgeItemTag("ingots/" + rubber.getId()), rodTag = TagUtils.getForgeItemTag("rods/" + handle.getId());
            addStackRecipe(consumer, Ref.ID, PLUNGER.getId() + "_" + handle.getId() + "_" + rubber.getId(), "antimatter_plungers",
                    "has_material_" + rubber.getId(), hasSafeItem(ingotTag), PLUNGER.getToolStack(handle, rubber),
                    of('W', WIRE_CUTTER.getTag(), 'I', ingotTag, 'S', Tags.Items.SLIMEBALLS, 'R', rodTag, 'F', FILE.getTag()), "WIS", " RI", "R F");
        }));

        //Temp override till bug in new recipe system gets fixed
        /*List<Material> mainMats = AntimatterAPI.all(Material.class, providerDomain).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.has(TOOLS))).collect(Collectors.toList());
        List<Material> armorMats = AntimatterAPI.all(Material.class, providerDomain).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.has(ARMOR))).collect(Collectors.toList());
        List<Material> handleMats = AntimatterAPI.all(Material.class).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.isHandle())).collect(Collectors.toList());

        handleMats.forEach(handle -> AntimatterAPI.all(Material.class).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.has(RUBBERTOOLS))).forEach(rubber -> {
            ITag<Item> ingotTag = TagUtils.getForgeItemTag("ingots/" + rubber.getId()), rodTag = TagUtils.getForgeItemTag("rods/" + handle.getId());
            addStackRecipe(consumer, Ref.ID, PLUNGER.getId() + "_" + handle.getId() + "_" + rubber.getId(), "antimatter_plungers",
                    "has_material_" + rubber.getId(), hasSafeItem(ingotTag), PLUNGER.getToolStack(handle, rubber),
                    of('W', WIRE_CUTTER.getTag(), 'I', ingotTag, 'S', Tags.Items.SLIMEBALLS, 'R', rodTag, 'F', FILE.getTag()), "WIS", " RI", "R F");
        }));

        armorMats.forEach(armor -> {
            if (!armor.has(PLATE) && !armor.has(GEM)) return; // TODO: For time being
            final ITag<Item> inputTag;
            if (armor.has(PLATE)){
                inputTag = PLATE.getMaterialTag(armor);
            } else {
                inputTag = GEM.getMaterialTag(armor);
            }
            final ICriterionInstance inputTrigger = this.hasSafeItem(inputTag);
            addStackRecipe(consumer, Ref.ID, HELMET.getId() + "_" + armor.getId(), "antimatter_helmets",
                    "has_material_" + armor.getId(), inputTrigger, HELMET.getToolStack(armor), of('I', inputTag, 'H', HAMMER.getTag()), "III", "IHI");
            addStackRecipe(consumer, Ref.ID, CHESTPLATE.getId() + "_" + armor.getId(), "antimatter_chestplates",
                    "has_material_" + armor.getId(), inputTrigger, CHESTPLATE.getToolStack(armor), of('I', inputTag, 'H', HAMMER.getTag()), "IHI", "III", "III");
            addStackRecipe(consumer, Ref.ID, LEGGINGS.getId() + "_" + armor.getId(), "antimatter_leggings",
                    "has_material_" + armor.getId(), inputTrigger, LEGGINGS.getToolStack(armor), of('I', inputTag, 'H', HAMMER.getTag()), "III", "IHI", "I I");
            addStackRecipe(consumer, Ref.ID, BOOTS.getId() + "_" + armor.getId(), "antimatter_boots",
                    "has_material_" + armor.getId(), inputTrigger, BOOTS.getToolStack(armor), of('I', inputTag, 'H', HAMMER.getTag()), "I I", "IHI");
        });

        mainMats.forEach(main -> {
            if (!main.has(INGOT) && !main.has(GEM)) return; // TODO: For time being
            String ingotGem = main.has(INGOT) ? "ingots" : "gems";
            String plate = main.has(PLATE) ? "plates" : ingotGem;
            final ITag<Item> ingotTag = TagUtils.getForgeItemTag(ingotGem + "/" + main.getId()), plateTag = TagUtils.getForgeItemTag(plate + "/" + main.getId()), mainRodTag = TagUtils.getForgeItemTag("rods/" + main.getId());
            final ICriterionInstance ingotTrigger = this.hasSafeItem(ingotTag), plateTrigger = this.hasSafeItem(plateTag), rodTrigger = this.hasSafeItem(mainRodTag);

            if (main.getToolTypes().contains(WRENCH))
                addStackRecipe(consumer, Ref.ID, WRENCH.getId() + "_" + main.getId(), "antimatter_wrenches",
                        "has_material_" + main.getId(), ingotTrigger, WRENCH.getToolStack(main, NULL), of('I', ingotTag, 'H', HAMMER.getTag()), "IHI", "III", " I ");

            if (main.getToolTypes().contains(MORTAR))
                addStackRecipe(consumer, Ref.ID, MORTAR.getId() + "_" + main.getId(), "antimatter_mortars",
                        "has_material_" + main.getId(), ingotTrigger, MORTAR.getToolStack(main, NULL), of('I', ingotTag, 'S', Tags.Items.STONE), " I ", "SIS", "SSS");

            if (main.getToolTypes().contains(CROWBAR)) {
                for (DyeColor colour : DyeColor.values()) {
                    int colourValue = colour.getMapColor().colorValue;
                    ItemStack crowbarStack = CROWBAR.getToolStack(main, NULL);
                    crowbarStack.getChildTag(muramasa.antimatter.Ref.TAG_TOOL_DATA).putInt(muramasa.antimatter.Ref.KEY_TOOL_DATA_SECONDARY_COLOUR, colourValue);
                    addStackRecipe(consumer, Ref.ID, CROWBAR.getId() + "_" + main.getId() + "_" + colour.toString(), "antimatter_crowbars",
                            "has_material_" + main.getId(), rodTrigger, crowbarStack, of('H', HAMMER.getTag(), 'C', colour.getTag(), 'R', mainRodTag, 'F', FILE.getTag()), "HCR", "CRC", "RCF");
                }
            }

            for (Material handle : handleMats) {
                String handleId = handle.getId().equals("wood") ? "wooden" : handle.getId();
                final ITag<Item> rodTag = TagUtils.getForgeItemTag("rods/" + handleId);

                ImmutableMap<Character, Object> map1 = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? of('I', ingotTag, 'R', rodTag, 'P', plateTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()) : of('I', ingotTag, 'R', rodTag, 'P', plateTag);
                ImmutableMap<Character, Object> map2 = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? of('R', rodTag, 'P', plateTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()) : of('R', rodTag, 'P', plateTag);
                String[] strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{"PII", "FRH", " R "} : new String[]{"PII", " R ", " R "};
                if (main.getToolTypes().contains(PICKAXE))
                    addStackRecipe(consumer, Ref.ID, PICKAXE.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_pickaxes",
                            "has_material_" + main.getId(), ingotTrigger, PICKAXE.getToolStack(main, handle), map1, strings);
                strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{" FP", " RH", "R  "} : new String[]{"  P", " R ", "R  "};
                if (main.getToolTypes().contains(PICKAXE))
                    addStackRecipe(consumer, muramasa.antimatter.Ref.ID, SPEAR.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_spears",
                        "has_material_" + main.getId(), ingotTrigger, SPEAR.getToolStack(main, handle), map2, strings);
                strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{"IIH", "PR ", "FR "} : new String[]{"II", "PR", " R"};
                if (main.getToolTypes().contains(AXE))
                    addStackRecipe(consumer, Ref.ID, AXE.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_axes",
                            "has_material_" + main.getId(), ingotTrigger, AXE.getToolStack(main, handle), map1, strings);
                strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{"PIH", "FR ", " R "} : new String[]{"PI", " R", " R"};
                if (main.getToolTypes().contains(HOE))
                    addStackRecipe(consumer, Ref.ID, HOE.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_hoes",
                            "has_material_" + main.getId(), ingotTrigger, HOE.getToolStack(main, handle), map1, strings);
                strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{"FPH", " R ", " R "} : new String[]{"P", "R", "R"};
                if (main.getToolTypes().contains(SHOVEL))
                    addStackRecipe(consumer, Ref.ID, SHOVEL.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_shovels",
                            "has_material_" + main.getId(), ingotTrigger, SHOVEL.getToolStack(main, handle), map2, strings);
                strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{" P ", "FPH", " R "} : new String[]{"P", "P", "R"};
                if (main.getToolTypes().contains(SWORD))
                    addStackRecipe(consumer, Ref.ID, SWORD.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_swords",
                            "has_material_" + main.getId(), ingotTrigger, SWORD.getToolStack(main, handle), map2, strings);

                if (main.getToolTypes().contains(HAMMER))
                    addStackRecipe(consumer, Ref.ID, HAMMER.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_hammers",
                            "has_material_" + main.getId(), ingotTrigger, HAMMER.getToolStack(main, handle), of('I', ingotTag, 'R', rodTag), "II ", "IIR", "II ");

                if (main.getToolTypes().contains(SAW))
                    addStackRecipe(consumer, Ref.ID, SAW.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_saws",
                            "has_material_" + main.getId(), plateTrigger, SAW.getToolStack(main, handle), of('P', plateTag, 'R', rodTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()), "PPR", "FH ");

                if (main.getToolTypes().contains(FILE))
                    addStackRecipe(consumer, Ref.ID, FILE.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_files",
                            "has_material_" + main.getId(), plateTrigger, FILE.getToolStack(main, handle), of('P', plateTag, 'R', rodTag), "P", "P", "R");

                if (main.getToolTypes().contains(KNIFE))
                    addStackRecipe(consumer, Ref.ID, KNIFE.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_knives",
                            "has_material_" + main.getId(), plateTrigger, KNIFE.getToolStack(main, handle), of('P', plateTag, 'R', rodTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()), "FPH", " R ");

                if (main.getToolTypes().contains(SCREWDRIVER))
                    addStackRecipe(consumer, Ref.ID, SCREWDRIVER.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_screwdrivers",
                            "has_material_" + main.getId(), rodTrigger, SCREWDRIVER.getToolStack(main, handle),
                            of('M', mainRodTag, 'R', rodTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()), " FM", " MH", "R  ");

                if (main.getToolTypes().contains(WIRE_CUTTER)) {
                    if (main.has(SCREW)){
                        addStackRecipe(consumer, Ref.ID, WIRE_CUTTER.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_wire_cutters",
                                "has_material_" + main.getId(), plateTrigger, WIRE_CUTTER.getToolStack(main, handle),
                                b -> b.put('P', plateTag).put('R', rodTag).put('F', FILE.getTag()).put('H', HAMMER.getTag())
                                        .put('S', SCREWDRIVER.getTag()).put('W', getForgeItemTag("screws/" + main.getId())), "PFP", "HPS", "RWR");
                    } else {
                        addStackRecipe(consumer, Ref.ID, WIRE_CUTTER.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_wire_cutters",
                                "has_material_" + main.getId(), plateTrigger, WIRE_CUTTER.getToolStack(main, handle),
                                b -> b.put('P', plateTag).put('R', rodTag).put('F', FILE.getTag()).put('H', HAMMER.getTag())
                                        .put('S', SCREWDRIVER.getTag()), "PFP", "HPS", "R R");
                    }
                }
            }
        });*/

        /*super.registerToolRecipes(consumer, providerDomain);
        List<Material> mainMats = AntimatterAPI.all(Material.class, providerDomain).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.has(TOOLS))).collect(Collectors.toList());
        List<Material> handleMats = AntimatterAPI.all(Material.class).stream().filter(m -> (m.getDomain().equals(providerDomain) && m.isHandle())).collect(Collectors.toList());

        mainMats.forEach(main -> {
            if (!main.has(INGOT) && !main.has(GEM)) return; // TODO: For time being
            String ingotGem = main.has(INGOT) ? "ingots" : "gems";
            String plate = main.has(PLATE) ? "plates" : ingotGem;
            final ITag<Item> ingotTag = TagUtils.getForgeItemTag(ingotGem + "/" + main.getId()), plateTag = TagUtils.getForgeItemTag(plate + "/" + main.getId()), mainRodTag = TagUtils.getForgeItemTag("rods/" + main.getId());
            final ICriterionInstance ingotTrigger = this.hasSafeItem(ingotTag), plateTrigger = this.hasSafeItem(plateTag), rodTrigger = this.hasSafeItem(mainRodTag);

            for (Material handle : handleMats) {
                String handleId = handle.getId().equals("wood") ? "wooden" : handle.getId();
                final ITag<Item> rodTag = TagUtils.getForgeItemTag("rods/" + handleId);

                ImmutableMap<Character, Object> map1 = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? of('R', rodTag, 'P', plateTag, 'F', FILE.getTag(), 'H', HAMMER.getTag()) : of('R', rodTag, 'P', plateTag);
                String[] strings = main.getToolTypes().contains(HAMMER) && main.getToolTypes().contains(FILE) ? new String[]{" FP", " RH", "R  "} : new String[]{"  P", " R ", "R  "};
                if (main.getToolTypes().contains(PICKAXE)){}
                    addStackRecipe(consumer, muramasa.antimatter.Ref.ID, SPEAR.getId() + "_" + main.getId() + "_" + handle.getId(), "antimatter_spears",
                            "has_material_" + main.getId(), ingotTrigger, SPEAR.getToolStack(main, handle), map1, strings);
            }
        });*/
    }

    public static ItemStack resolveStack(IAntimatterTool tool, Material primary, Material secondary, long startingEnergy, long maxEnergy) {
        ItemStack stack = new ItemStack(tool.getItem());
        tool.validateTag(stack, primary, secondary, startingEnergy, maxEnergy);
        Map<Enchantment, Integer> mainEnchants = primary.getEnchantments();
        if (!mainEnchants.isEmpty()) {
            mainEnchants.entrySet().stream().filter(e -> e.getKey().canApply(stack)).forEach(e -> stack.addEnchantment(e.getKey(), e.getValue()));
        }
        return stack;
    }

    @Override
    public String getName() {
        return "GT4R Crafting Recipes";
    }

    private String fixLoc(String attach) {
        return Ref.ID.concat(":").concat(attach);
    }

}
