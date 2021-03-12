package trinsdar.gt4r.datagen;

import muramasa.antimatter.datagen.providers.AntimatterRecipeProvider;
import trinsdar.gt4r.Ref;
import trinsdar.gt4r.data.GT4RData;
import trinsdar.gt4r.loader.crafting.Machines;
import trinsdar.gt4r.loader.crafting.Parts;
import trinsdar.gt4r.loader.crafting.WireCablesPlates;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.google.common.collect.ImmutableMap.of;
import static muramasa.antimatter.Data.*;
import static muramasa.antimatter.util.Utils.getForgeItemTag;

public class GT4RRecipes extends AntimatterRecipeProvider {

    public GT4RRecipes(String providerDomain, String providerName, DataGenerator gen) {
        super(providerDomain, providerName, gen);
        registerCraftingLoaders();
        //Depends on certain data so TIER MAPS cannot be static {} initialized.
        //GregTechData.buildTierMaps();
    }

    protected void registerCraftingLoaders() {
        this.craftingLoaders.add(Parts::loadRecipes);
        this.craftingLoaders.add(WireCablesPlates::loadRecipes);
        this.craftingLoaders.add(Machines::loadRecipes);
    }

    @Override
    public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        super.registerRecipes(consumer);
        addConditionalRecipe(consumer, getStackRecipe("", "has_sulfur_dust", this.hasItem(getForgeItemTag("dusts/sulfur")),
                new ItemStack(Blocks.TORCH, 6), of('D', getForgeItemTag("dusts/sulfur"), 'R', Tags.Items.RODS_WOODEN), "D", "R"), Ref.class, "sulfurTorch", Ref.ID, "sulfur_torch");

        addItemRecipe(consumer, Ref.ID, "hopper", "", "has_wrench", this.hasItem(WRENCH.getTag()),
                Blocks.HOPPER, of('C', Blocks.CHEST, 'I', getForgeItemTag("plates/iron"), 'W', WRENCH.getTag()), "IWI", "ICI", " I ");


        addItemRecipe(consumer, "gears", "has_stone", this.hasItem(Blocks.PISTON), Blocks.STICKY_PISTON, of('S', GT4RData.StickyResin, 'P', Blocks.PISTON), "S", "P");

//        RegistrationHelper.getMaterialsForDomain(Ref.ID).stream().filter(m -> m.has(PLATE, INGOT)).forEach(mat -> {
//            Item plate = PLATE.get(mat);
//            Item ingot = INGOT.get(mat);
//            Tag<Item> ingotTag = getForgeItemTag("ingots/".concat(mat.getId()));
//            ShapedRecipeBuilder.shapedRecipe(plate).key('H', HAMMER.getTag()).key('I', ingotTag)
//                    .patternLine("H").patternLine("I").patternLine("I").setGroup("ingots_hammer_to_plates")
//                    .addCriterion("has_ingot_" + mat.getId(), this.hasItem(ingotTag)).build(consumer, sigh(ingot.getRegistryName().getPath() + "_hammer_to_" + plate.getRegistryName().getPath()));
//        });
//
//        RegistrationHelper.getMaterialsForDomain(Ref.ID).stream().filter(m -> m.has(DUST)).forEach(mat -> {
//            Item dust = DUST.get(mat);
//            if (mat.has(ROCK)) {
//                Tag<Item> rockTag = getForgeItemTag("rocks/".concat(mat.getId()));
//                Item rock = ROCK.get(mat);
//                Item smallDust = DUST_SMALL.get(mat);
//                ShapelessRecipeBuilder.shapelessRecipe(dust)
//                        .addIngredient(rockTag).addIngredient(rockTag).addIngredient(rockTag)
//                        .addIngredient(rockTag).addIngredient(rockTag).addIngredient(rockTag)
//                        .addIngredient(rockTag).addIngredient(rockTag).addIngredient(MORTAR.getTag())
//                        .addCriterion("has_rock_" + mat.getId(), this.hasItem(rockTag))
//                        .setGroup("rocks_grind_to_dust").build(consumer, sigh(rock.getRegistryName().getPath() + "_grind_to_" + dust.getRegistryName().getPath()));
//
//                ShapelessRecipeBuilder.shapelessRecipe(smallDust)
//                        .addIngredient(rockTag).addIngredient(rockTag)
//                        .addIngredient(rockTag).addIngredient(rockTag).addIngredient(MORTAR.getTag())
//                        .addCriterion("has_rock_" + mat.getId(), this.hasItem(getForgeItemTag("rocks/".concat(mat.getId()))))
//                        .setGroup("rocks_grind_to_small_dust").build(consumer, sigh(rock.getRegistryName().getPath() + "_grind_to_" + smallDust.getRegistryName().getPath()));
//            }
//            if (mat.has(INGOT, GRINDABLE)) {
//                Item ingot = INGOT.get(mat);
//                Tag<Item> ingotTag = getForgeItemTag("ingots/".concat(mat.getId()));
//                ShapelessRecipeBuilder.shapelessRecipe(dust).addIngredient(ingotTag).addIngredient(MORTAR.getTag())
//                        .addCriterion("has_ingot_" + mat.getId(), this.hasItem(getForgeItemTag("ingots/".concat(mat.getId()))))
//                        .setGroup("ingots_grind_to_dust")
//                        .build(consumer, sigh(ingot.getRegistryName().getPath() + "_grind_to_" + dust.getRegistryName().getPath()));
//            }
//        });
    }

    @Override
    public String getName() {
        return "GregTech Crafting Recipes";
    }

    private String fixLoc(String attach) {
        return Ref.ID.concat(":").concat(attach);
    }

}