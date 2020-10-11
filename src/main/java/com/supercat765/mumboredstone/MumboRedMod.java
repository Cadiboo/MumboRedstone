package com.supercat765.mumboredstone;

import com.supercat765.mumboredstone.TE.TEWirelessReciever;
import com.supercat765.mumboredstone.TE.TEWirelessSender;
import com.supercat765.mumboredstone.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = MumboRedMod.MODID, name = MumboRedMod.NAME, version = MumboRedMod.VERSION)
public class MumboRedMod {
	public static final String MODID = "mumboredstone";
    public static final String NAME = "Mumbo's Redstone Additions";
    public static final String VERSION = "0.3.3";
    
    
    @SidedProxy(clientSide="com.supercat765.mumboredstone.proxy.ClientProxy", serverSide="com.supercat765.mumboredstone.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void load(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	MRConfig.Load(config);
    	
    	MRItems.Load();
    	MRBlocks.Load();
    	
    	
    	
    	GameRegistry.registerTileEntity(TEWirelessSender.class,"tewirelesssender");
    	GameRegistry.registerTileEntity(TEWirelessReciever.class,"tewirelessreciever");
    	
    	MinecraftForge.EVENT_BUS.register(new RegisteryHandler());
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
    	proxy.registerColorHandlers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	MRItems.addColors();
    }
    
    
}
