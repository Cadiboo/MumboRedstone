package com.supercat765.mumboredstone;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.config.Configuration;

public class MRConfig {
	
	public static boolean LOAD_ROTATORS=true;
	public static boolean LOAD_POWERPISTON=true;
	public static boolean LOAD_EXTENDERREPEATER=true;
	public static boolean LOAD_DETECTOR=true;
	public static boolean LOAD_CAPACITOR=true;
	public static boolean LOAD_RESISTOR=true;
	public static boolean LOAD_BLUESTONE=true;
	public static boolean LOAD_WIRELESS=true;
	public static boolean LOAD_BREAKER=true;
	public static boolean LOAD_PLACER=true;
	
	public static double DetectorRange = 10;
	public static double DetectorAngle = 3*(Math.PI)/4;
	public static int DetectorRefresh = 2;
	
	public static int ExtenderFactor = 4;
	
	public static int[] BluestoneColor = {19,59,219,6,13,40};
	
	public static int PowerPistonStrength = 24;
	
	public static int SurvivalWirelessChannels = 3;
	public static double WirelessRange = 15;
	public static int Resistor_strength = 4;
	
	public static float BreakerEntityPush = .5f;
	public static int BreakerRefresh = 15;
	public static String[] BreakerDisable = {};

	public static boolean AllowSaplingPlacement=true;
	public static boolean AllowCropPlacement=true;
	public static String[] PlacementDisable = {};
	
	public static void Load(Configuration config){
		config.load();
		
		LOAD_ROTATORS = config.getBoolean("LOAD_ROTATORS", "Enable/Disable", LOAD_ROTATORS, "effects all rotators");
		LOAD_POWERPISTON = config.getBoolean("LOAD_POWERPISTON", "Enable/Disable", LOAD_POWERPISTON, "effects both sticky and non");
		LOAD_EXTENDERREPEATER = config.getBoolean("LOAD_EXTENDERREPEATER", "Enable/Disable", LOAD_EXTENDERREPEATER, "");
		LOAD_DETECTOR = config.getBoolean("LOAD_DETECTOR", "Enable/Disable", LOAD_DETECTOR, "");
		LOAD_CAPACITOR = config.getBoolean("LOAD_CAPACITOR", "Enable/Disable", LOAD_CAPACITOR, "");
		LOAD_RESISTOR = config.getBoolean("LOAD_RESISTOR", "Enable/Disable", LOAD_RESISTOR, "");
		LOAD_BLUESTONE = config.getBoolean("LOAD_BLUESTONE", "Enable/Disable", LOAD_BLUESTONE, "");
		LOAD_WIRELESS = config.getBoolean("LOAD_WIRELESS", "Enable/Disable", LOAD_WIRELESS, "effects reciever, lever and button");
		LOAD_BREAKER = config.getBoolean("LOAD_BREAKER", "Enable/Disable", LOAD_BREAKER, "");
		LOAD_PLACER = config.getBoolean("LOAD_PLACER", "Enable/Disable", LOAD_PLACER, "");
		
		
		DetectorRange = config.getFloat("Range", "Entity Detector", (float) DetectorRange, 1, 30, "");
		DetectorAngle = config.getFloat("Angle", "Entity Detector", (float) DetectorAngle, 0, (float) Math.PI, "");
		DetectorRefresh = config.getInt("Refresh", "Entity Detector", DetectorRefresh, 2, 1000, "");
		
		
		Resistor_strength = config.getInt("Resistance", "Resistor", Resistor_strength, 2, 15, "");
		
		
		ExtenderFactor = config.getInt("signal length multiplier", "signal Extender", ExtenderFactor, 2, 10, "");
		
		PowerPistonStrength = config.getInt("Power Piston Strength", "Pistons", PowerPistonStrength, 1, 64, "");

		SurvivalWirelessChannels = config.getInt("Survival Channels", "Wireless", SurvivalWirelessChannels, 1, 4, "the number of channels available via R click");
		WirelessRange = config.getFloat("Range", "Wireless", (float) WirelessRange, 1, 30, "");
		
		WirelessRange=WirelessRange*WirelessRange;
		
		BluestoneColor = config.get("Bluestone", "Color", BluestoneColor, "on color (R,G,B), off color (R,G,B)").getIntList();
		
		BreakerEntityPush = config.getFloat("BreakerEntityPush", "BlockBreaker", BreakerEntityPush, 0, 3, "The speed of entities launched by the block breaker");
		BreakerRefresh = config.getInt("Refresh", "BlockBreaker", BreakerRefresh, 2, 1000, "speed of the block breaker");
		BreakerDisable=config.get("BlockBreaker", "BreakerDisable", BreakerDisable,"Additional Blocks not to break").getStringList();
		
		AllowSaplingPlacement = config.getBoolean("AllowSaplingPlacement", "BlockPlacer", AllowSaplingPlacement, "");
		AllowCropPlacement = config.getBoolean("AllowCropPlacement", "BlockPlacer", AllowCropPlacement, "");
		PlacementDisable=config.get("BlockPlacer", "PlacementDisable", PlacementDisable,"Additional Blocks not to place").getStringList();
		
		config.save();
	}
	
	public static boolean canplacerplace(String id){
		return arraycontains(id,PlacementDisable);
	}
	
	public static boolean canbreakerbreak(String id){
		return arraycontains(id,BreakerDisable);
	}

	private static boolean arraycontains(String id, String[] arr) {
		for(String id2:arr){
			if(id.equals(id2)) return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
