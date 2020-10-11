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

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MumboRedstone.MOD_ID, bus = Bus.MOD)
public class MRConfig {

    /**
     * Called from inside the mod constructor.
     *
     * @param context The ModLoadingContext to register the configs to
     */
    public static void register(ModLoadingContext context) {
//        context.registerConfig(ModConfig.Type.CLIENT, Client.SPEC);
        context.registerConfig(ModConfig.Type.SERVER, Server.SPEC);
    }

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent event) {
        final ForgeConfigSpec spec = event.getConfig().getSpec();
        if (spec == Server.SPEC)
            Server.bake();
//        else if (spec == Client.SPEC)
//            Client.bake();
    }

    //    public static class Client {
//
//        static final Impl INSTANCE;
//        static final ForgeConfigSpec SPEC;
//        public static Color bluestoneColor;
//
//        static {
//            final Pair<Impl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Impl::new);
//            SPEC = specPair.getRight();
//            INSTANCE = specPair.getLeft();
//        }
//
//        public static void bake() {
//            bluestoneColor = parseColorOrThrow(INSTANCE.bluestoneColor);
//        }
//
//        private static class Impl {
//
//        }
//    }
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
            final Pair<Impl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Impl::new);
            SPEC = specPair.getRight();
            INSTANCE = specPair.getLeft();
        }

        public static void bake() {
//            bluestoneColor = parseColorOrThrow(INSTANCE.bluestoneColor);
//            BlockColorHandler.setBluestoneWirePowerColors()
        }

        private static class Impl {
            private Impl(final ForgeConfigSpec.Builder builder) {

            }
        }
    }

}
