package trinsdar.gt4r.worldgen;

import muramasa.antimatter.worldgen.feature.AntimatterFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import static muramasa.antimatter.Data.NULL;
import static net.minecraftforge.common.BiomeDictionary.Type.*;
import static trinsdar.gt4r.data.Materials.*;

public class GT4RFeatures {
    public static final AntimatterFeature<?> ORE = new FeatureVanillaTypeOre();

    //public static final GT4ROreFeatureConfig IRON = new GT4ROreFeatureConfig("iron", 1, "iron", "nickel", 5);
    public static final GT4ROreFeatureConfig COPPER = new GT4ROreFeatureConfig("copper", 0, 100, 15, 10, Copper, Gold, 2, World.OVERWORLD);
    public static final GT4ROreFeatureConfig TIN = new GT4ROreFeatureConfig("tin", 0, 100, 25, 6, Tin, Iron, 3, World.OVERWORLD);
    public static final GT4ROreFeatureConfig URANITE = new GT4ROreFeatureConfig("uranite", 0, 100, 8, 4, Uraninite, NULL, 0, World.OVERWORLD).setInvalidBiomes(DEAD);
    public static final GT4ROreFeatureConfig URANITE_DEAD = new GT4ROreFeatureConfig("uranite_dead", 0, 100, 20, 4, Uraninite, NULL, 0, World.OVERWORLD).setValidBiomes(DEAD);
    public static final GT4ROreFeatureConfig CASSITERITE = new GT4ROreFeatureConfig("cassiterite", 30, 80, 2, 32, Cassiterite, Tin, 5, World.OVERWORLD).setValidBiomes(MUSHROOM, MOUNTAIN, CONIFEROUS, COLD).setInvalidBiomes(JUNGLE);
    public static final GT4ROreFeatureConfig TETRAHEDRITE = new GT4ROreFeatureConfig("tetrahedrite", 20, 70, 7, 6, Tetrahedrite, Copper, 1, World.OVERWORLD).setValidBiomes(JUNGLE, SWAMP, MOUNTAIN, MUSHROOM).setInvalidBiomes(COLD);
    public static final GT4ROreFeatureConfig GALENA = new GT4ROreFeatureConfig("galena", 0, 64, 12, 10, Galena, NULL, 0, World.OVERWORLD);
    public static final GT4ROreFeatureConfig BAUXITE = new GT4ROreFeatureConfig("bauxite", 50, 120, 6, 16, Bauxite, NULL, 0, World.OVERWORLD).setValidBiomes(FOREST, PLAINS);
    public static final GT4ROreFeatureConfig RUBY = new GT4ROreFeatureConfig("ruby", 0, 48, 3, 6, Ruby, NULL, 0, World.OVERWORLD).setValidBiomes(HOT).setInvalidBiomes(JUNGLE, OCEAN);
    public static final GT4ROreFeatureConfig SAPPHIRE = new GT4ROreFeatureConfig("sapphire", 0, 48, 3, 6, Sapphire, NULL, 0, World.OVERWORLD).setValidBiomes(OCEAN, BEACH);
    public static final GT4ROreFeatureConfig PLATINUM = new GT4ROreFeatureConfig("platinum", 10, 30, 3, 8, Platinum, Sphalerite, 5, World.OVERWORLD).setValidBiomes(JUNGLE);
    public static final GT4ROreFeatureConfig IRIDIUM = new GT4ROreFeatureConfig("iridium", 0, 128, 1, 2, Iridium, NULL, 0, World.OVERWORLD);
    public static final GT4ROreFeatureConfig EMERALD = new GT4ROreFeatureConfig("emerald", 0, 32, 4, 6, Emerald, NULL, 0, World.OVERWORLD).setValidBiomes(MOUNTAIN);
    public static final GT4ROreFeatureConfig PYRITE = new GT4ROreFeatureConfig("pyrite", 0, 64, 8, 16, Pyrite, NULL, 0, World.THE_NETHER);
    public static final GT4ROreFeatureConfig SPHALERITE = new GT4ROreFeatureConfig("sphalerite", 32, 96, 8, 16, Sphalerite, NULL, 0, World.THE_NETHER);
    public static final GT4ROreFeatureConfig CINNABAR = new GT4ROreFeatureConfig("cinnabar",64, 128, 7, 16, Cinnabar, NULL, 0, World.THE_NETHER);
    public static final GT4ROreFeatureConfig TUNGSTATE = new GT4ROreFeatureConfig("tungstate", 0, 80, 2, 16, Tungstate, NULL, 0, World.THE_END);
    public static final GT4ROreFeatureConfig PLATINUM_END = new GT4ROreFeatureConfig("platinum_end", 0, 80, 2, 5, Platinum, NULL, 0, World.THE_END);
    public static final GT4ROreFeatureConfig OLIVINE = new GT4ROreFeatureConfig("olivine", 0, 80, 5, 8, Olivine, NULL, 0, World.THE_END);
    public static final GT4ROreFeatureConfig SODALITE = new GT4ROreFeatureConfig("sodalite", 0, 80, 6, 16, Sodalite, NULL, 0, World.THE_END);
    public static final GT4ROreFeatureConfig CHROMITE = new GT4ROreFeatureConfig("chromite", 0, 80, 4, 5, Chromite, NULL, 0, World.THE_END);

    public static void init(){

    }
}