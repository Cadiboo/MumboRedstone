package com.supercat765.mumboredstone;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class RegisteryHandler {
	@SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
    	MRBlocks.register(event.getRegistry());
    }
	@SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	MRBlocks.registerItem(event.getRegistry());
    	MRItems.register(event.getRegistry());
    }
	
	@SubscribeEvent
    public void registerRecipies(RegistryEvent.Register<IRecipe> event) {
    	MRBlocks.registerRecipies(event.getRegistry());
    }
	
	@SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
    	MRBlocks.registerItemModels();
    	MRItems.registerItemModels();
    }
}
