package com.supercat765.mumboredstone;

import com.supercat765.mumboredstone.blocks.BlockColoredWire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;

public class ModColorHandler {
	private static final Minecraft minecraft = Minecraft.getMinecraft();

	public static void registerColorHandlers() {
		final BlockColors blockColors = minecraft.getBlockColors();
		registerBlockColorHandlers(blockColors);
	}

	private static void registerBlockColorHandlers(final BlockColors blockColors) {
		final IBlockColor ColorWireHandler = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null && blockAccess.getBlockState(pos).getBlock() instanceof BlockColoredWire) {
				BlockColoredWire Wire = (BlockColoredWire) (blockAccess.getBlockState(pos).getBlock());
				return Wire.colorMultiplier(blockAccess.getBlockState(pos).getValue(BlockColoredWire.POWER));
			}

			return 16777215;
		};

		if(MRConfig.LOAD_BLUESTONE)blockColors.registerBlockColorHandler(ColorWireHandler, MRBlocks.BlueStoneWire);
	}
	}