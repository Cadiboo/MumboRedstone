package com.supercat765.mumboredstone;

import com.supercat765.mumboredstone.items.*;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class MRItems {

	public static Item BlueStone;

	public static void Load() {
		if(MRConfig.LOAD_BLUESTONE){
		BlueStone = new ItemBluestone().setRegistryName(new ResourceLocation(MumboRedMod.MODID,"bluestone")).setUnlocalizedName("bluestone");
		}
	}

	public static void addRecipies() {
		// TODO Auto-generated method stub
		
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		if(MRConfig.LOAD_BLUESTONE){
		ModelLoader.setCustomModelResourceLocation(BlueStone, 0, new ModelResourceLocation(BlueStone.getRegistryName(), "inventory"));
		}
	}

	public static void addColors() {
		// TODO Auto-generated method stub
		
	}

	public static void register(IForgeRegistry<Item> registry) {
		if(MRConfig.LOAD_BLUESTONE){
		registry.register(BlueStone);
		}
		
		registry.register(new FakeItem());
	}
}
