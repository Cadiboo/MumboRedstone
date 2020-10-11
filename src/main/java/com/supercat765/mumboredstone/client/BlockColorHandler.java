package com.supercat765.mumboredstone.client;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.block.BluestoneWireBlock;
import com.supercat765.mumboredstone.init.MRBlocks;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Arrays;

import static net.minecraft.util.ColorHelper.PackedColor.*;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MumboRedstone.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class BlockColorHandler {

    static final int WHITE = 0xFF_FF_FF_FF;

    /** The colors for bluestone wire, stored by power level. */
    public static int[] BLUESTONE_WIRE_POWER_COLORS = new int[BluestoneWireBlock.POWER_VALUES];

    static {
        Arrays.fill(BLUESTONE_WIRE_POWER_COLORS, WHITE);
    }

    @SubscribeEvent
    public static void onBlockColorsInit(ColorHandlerEvent.Block event) {
        registerBluestoneWireColorHandler(event.getBlockColors());
    }

    private static void registerBluestoneWireColorHandler(BlockColors blockColors) {
        blockColors.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null || state.getBlock() != MRBlocks.BLUESTONE_WIRE.get())
                return WHITE;
            int power = state.get(BluestoneWireBlock.POWER);
            return BLUESTONE_WIRE_POWER_COLORS[power];
        }, MRBlocks.BLUESTONE_WIRE.get());
    }

    public static void setBluestoneWirePowerColors(int unpoweredColor, int fullyPoweredColor) {
        for (int wirePowerLevel = 0; wirePowerLevel < BLUESTONE_WIRE_POWER_COLORS.length; wirePowerLevel++) {
            int color = getBluestoneWireColorForPowerLevel(wirePowerLevel, unpoweredColor, fullyPoweredColor);
            BLUESTONE_WIRE_POWER_COLORS[wirePowerLevel] = color;
            BluestoneWireBlock.POWER_COLORS[wirePowerLevel] = new Vector3f(getRed(color), getGreen(color), getBlue(color));
        }
    }

    static int getBluestoneWireColorForPowerLevel(int i, int unpoweredColor, int fullyPoweredColor) {
        float percent = map(i, 0, BluestoneWireBlock.POWER_VALUES - 1, 0, 100);
        // Set to full alpha
        unpoweredColor |= 0xFF000000;
        fullyPoweredColor |= 0xFF000000;
        return blend(unpoweredColor, fullyPoweredColor, percent / 100F);
    }

    /** "https://stackoverflow.com/a/20332789" */
    static int blend(int i1, int i2, float ratio) {
        if (ratio > 1f)
            ratio = 1f;
        else if (ratio < 0f)
            ratio = 0f;
        float iRatio = 1.0f - ratio;

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return packColor(a, r, g, b);
    }

    /** "https://stackoverflow.com/a/7506169" */
    private static float map(float value, int fromMin, int fromMax, int toMin, int toMax) {
        return (value - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin;
    }

}
