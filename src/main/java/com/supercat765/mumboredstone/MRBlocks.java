package com.supercat765.mumboredstone;

import java.util.ArrayList;

import com.supercat765.mumboredstone.blocks.*;
import com.supercat765.mumboredstone.items.ItemBlockRotator;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class MRBlocks {

	public static Block RotatorCW;
	public static Block RotatorCCW;
	public static Block Rotator180;
	public static Block PowerPiston;
	public static Block PowerStickyPiston;
	public static Block PistonExtention;
	public static Block ExtenderRepeater;
	public static Block ExtenderRepeater_lit;
	public static Block Detector;
	public static Block Capacitor;
	public static Block Resister;
	public static Block BlueStoneWire;
	
	public static Block WirelessReciever;
	public static Block WirelessLever;
	public static Block WirelessButton;
	
	public static Block BlockPlacer;
	public static Block BlockBreaker;
	
	private static ArrayList<BlockRegister> blocks = new ArrayList<BlockRegister>();
	
	
	public static void Load() {
		if(MRConfig.LOAD_ROTATORS){
		blocks.add(new BlockRegister(RotatorCW = new BlockRotator(1).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"rotatorcw")).setUnlocalizedName("rotatorcw"),new ItemBlockRotator(RotatorCW)));
		blocks.add(new BlockRegister(RotatorCCW = new BlockRotator(3).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"rotatorccw")).setUnlocalizedName("rotatorccw"),new ItemBlockRotator(RotatorCCW)));
		blocks.add(new BlockRegister(Rotator180 = new BlockRotator(2).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"rotator180")).setUnlocalizedName("rotator180"),new ItemBlockRotator(Rotator180)));
		}
		
		if(MRConfig.LOAD_POWERPISTON){
		blocks.add(new BlockRegister(PistonExtention = new BlockPowerPistonExtension().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"powerpiston_extention")).setUnlocalizedName("powerpiston_extention"),false));
		
		blocks.add(new BlockRegister(PowerPiston = new BlockPowerPistonBase(false,PistonExtention,MRConfig.PowerPistonStrength).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"powerpiston")).setUnlocalizedName("powerpiston"),true));
		blocks.add(new BlockRegister(PowerStickyPiston = new BlockPowerPistonBase(true,PistonExtention,MRConfig.PowerPistonStrength).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"powerpiston_sticky")).setUnlocalizedName("powerpiston_sticky"),true));
		}
		
		if(MRConfig.LOAD_EXTENDERREPEATER){
		blocks.add(new BlockRegister(ExtenderRepeater = new BlockExtenderRepeater(false).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"extendrepeater")).setUnlocalizedName("extendrepeater"),true));
		blocks.add(new BlockRegister(ExtenderRepeater_lit = new BlockExtenderRepeater(true).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"extendrepeater_lit")).setUnlocalizedName("extendrepeater_lit"),false));
		}
		
		
		if(MRConfig.LOAD_DETECTOR)blocks.add(new BlockRegister(Detector = new BlockDetector().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"detector")).setUnlocalizedName("detector"),true));
		
		if(MRConfig.LOAD_CAPACITOR)blocks.add(new BlockRegister(Capacitor = new BlockCapacitor().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"capacitor")).setUnlocalizedName("capacitor"),true));
		
		if(MRConfig.LOAD_RESISTOR)blocks.add(new BlockRegister(Resister = new BlockResister(MRConfig.Resistor_strength).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"resister")).setUnlocalizedName("resister"),true));
		
		if(MRConfig.LOAD_BLUESTONE){
		blocks.add(new BlockRegister(BlueStoneWire = new BlockColoredWire(MRConfig.BluestoneColor[0],MRConfig.BluestoneColor[1],MRConfig.BluestoneColor[2],MRConfig.BluestoneColor[3],MRConfig.BluestoneColor[4],MRConfig.BluestoneColor[5]).setRegistryName(new ResourceLocation(MumboRedMod.MODID,"blue_wire")).setUnlocalizedName("blue_wire"),false));
		((BlockColoredWire)BlueStoneWire).setDrop(MRItems.BlueStone);
		}
		
		if(MRConfig.LOAD_WIRELESS){
		blocks.add(new BlockRegister(WirelessReciever = new BlockWirelessReciever().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"wireless_reciever")).setUnlocalizedName("wireless_reciever"),true));
		blocks.add(new BlockRegister(WirelessLever = new BlockWirelessLever().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"wireless_lever")).setUnlocalizedName("wireless_lever"),true));
		blocks.add(new BlockRegister(WirelessButton = new BlockWirelessButton().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"wireless_button")).setUnlocalizedName("wireless_button"),true));
		}
		
		if(MRConfig.LOAD_PLACER)
		blocks.add(new BlockRegister(BlockPlacer = new BlockPlacer().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"placer")).setUnlocalizedName("placer"),true));
		
		if(MRConfig.LOAD_BREAKER)
		blocks.add(new BlockRegister(BlockBreaker = new BlockBreaker().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"breaker")).setUnlocalizedName("breaker"),true));
	}

	public static void register(IForgeRegistry<Block> registry) {
		for(BlockRegister b:blocks){
			registry.register(b.getBlock());
		}
		
		//registry.register();
	}

	public static void registerItem(IForgeRegistry<Item> registry) {
		for(BlockRegister b:blocks){
			if(b.isNeedsItem()){
				if(b.hascustomItem()){
					registry.register(b.getItem().setRegistryName(b.getBlock().getRegistryName()));
				}
				else
				{
					registry.register(new ItemBlock(b.getBlock()).setRegistryName(b.getBlock().getRegistryName()));
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		
		for(BlockRegister b:blocks){
			if(b.isNeedsItem()){
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b.getBlock()), 0, new ModelResourceLocation(b.getBlock().getRegistryName(), "inventory"));
			}
		}
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(), 0, new ModelResourceLocation(.getRegistryName(), "inventory"));
	}

	public static void registerRecipies(IForgeRegistry<IRecipe> registry) {
		
	}
}
