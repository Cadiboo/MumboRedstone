package com.supercat765.mumboredstone.config;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.client.BlockColorHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraft.util.ColorHelper.PackedColor.*;
import static net.minecraftforge.common.ForgeConfigSpec.Builder;
import static net.minecraftforge.common.ForgeConfigSpec.IntValue;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MumboRedstone.MOD_ID, bus = Bus.MOD)
public class MRConfig {

    /**
     * Called from inside the mod constructor.
     *
     * @param context The ModLoadingContext to register the configs to
     */
    public static void register(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, Client.SPEC);
        context.registerConfig(ModConfig.Type.SERVER, Server.SPEC);
    }

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        final ForgeConfigSpec spec = event.getConfig().getSpec();
        if (spec == Server.SPEC)
            Server.bake();
        else if (spec == Client.SPEC)
            Client.bake();
    }

    public static class Client {

        static int DEFAULT_BLUESTONE_UNPOWERED_COLOR = packColor(255, 6, 13, 40);
        static int DEFAULT_BLUESTONE_FULLY_POWERED_COLOR = packColor(255, 19, 59, 219);

        static final Impl INSTANCE;
        static final ForgeConfigSpec SPEC;

        static {
            final Pair<Impl, ForgeConfigSpec> specPair = new Builder().configure(Impl::new);
            SPEC = specPair.getRight();
            INSTANCE = specPair.getLeft();
        }

        public static void bake() {
            // In my NoCubes mod I use another library to parse colors
            // IDK what you guys usually do so I've gone with this as it's similar to how your config worked in 1.12.2
            int unpoweredR = INSTANCE.bluestoneUnpoweredColorR.get();
            int unpoweredG = INSTANCE.bluestoneUnpoweredColorG.get();
            int unpoweredB = INSTANCE.bluestoneUnpoweredColorB.get();
            int fullyPoweredR = INSTANCE.bluestoneFullyPoweredColorR.get();
            int fullyPoweredG = INSTANCE.bluestoneFullyPoweredColorG.get();
            int fullyPoweredB = INSTANCE.bluestoneFullyPoweredColorB.get();
            final int fullAlpha = 255;
            int unpowered = packColor(fullAlpha, unpoweredR, unpoweredG, unpoweredB);
            int fullyPowered = packColor(fullAlpha, fullyPoweredR, fullyPoweredG, fullyPoweredB);
            BlockColorHandler.setBluestoneWirePowerColors(unpowered, fullyPowered);
        }

        private static class Impl {
            private final IntValue bluestoneUnpoweredColorR;
            private final IntValue bluestoneUnpoweredColorG;
            private final IntValue bluestoneUnpoweredColorB;
            private final IntValue bluestoneFullyPoweredColorR;
            private final IntValue bluestoneFullyPoweredColorG;
            private final IntValue bluestoneFullyPoweredColorB;

            private Impl(final Builder builder) {
                bluestoneUnpoweredColorR = defineColor(builder, "bluestoneUnpoweredColorR", getRed(DEFAULT_BLUESTONE_UNPOWERED_COLOR));
                bluestoneUnpoweredColorG = defineColor(builder, "bluestoneUnpoweredColorG", getGreen(DEFAULT_BLUESTONE_UNPOWERED_COLOR));
                bluestoneUnpoweredColorB = defineColor(builder, "bluestoneUnpoweredColorB", getBlue(DEFAULT_BLUESTONE_UNPOWERED_COLOR));
                bluestoneFullyPoweredColorR = defineColor(builder, "bluestoneFullyPoweredColorR", getRed(DEFAULT_BLUESTONE_FULLY_POWERED_COLOR));
                bluestoneFullyPoweredColorG = defineColor(builder, "bluestoneFullyPoweredColorG", getGreen(DEFAULT_BLUESTONE_FULLY_POWERED_COLOR));
                bluestoneFullyPoweredColorB = defineColor(builder, "bluestoneFullyPoweredColorB", getBlue(DEFAULT_BLUESTONE_FULLY_POWERED_COLOR));
            }

            private IntValue defineColor(Builder builder, String name, int defaultValue) {
                return builder
                    .translation(translationKey(name))
                    .defineInRange(name, defaultValue, 0, 255);
            }
        }
    }

    private static String translationKey(String name) {
        return MumboRedstone.MOD_ID + ".config." + name;
    }

    public static class Server {

        static final Impl INSTANCE;
        static final ForgeConfigSpec SPEC;

        public static double detectorRange;// = 10;
        public static double detectorAngle;// = 3*(Math.PI)/4;
        public static int DetectorRefresh = 2;

        public static int ExtenderFactor = 4;

        public static int[] BluestoneColor = {19, 59, 219, 6, 13, 40};

        public static int PowerPistonStrength = 24;

        public static int SurvivalWirelessChannels = 3;
        public static double WirelessRange = 15;
        public static int Resistor_strength = 4;

        public static float BreakerEntityPush = .5f;
        public static int BreakerRefresh = 15;
        public static String[] BreakerDisable = {};

        public static boolean AllowSaplingPlacement = true;
        public static boolean AllowCropPlacement = true;
        public static String[] PlacementDisable = {};


        static {
            final Pair<Impl, ForgeConfigSpec> specPair = new Builder().configure(Impl::new);
            SPEC = specPair.getRight();
            INSTANCE = specPair.getLeft();
        }

        public static void bake() {
        }

        private static class Impl {
            private Impl(final Builder builder) {

            }
        }
    }

}
