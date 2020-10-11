package com.supercat765.mumboredstone;

import com.supercat765.mumboredstone.config.MRConfig;
import com.supercat765.mumboredstone.init.MRBlocks;
import com.supercat765.mumboredstone.init.MRItems;
import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MumboRedstone.MOD_ID)
public class MumboRedstone {
    public static final String MOD_ID = "mumboredstone";

    public MumboRedstone() {
        ModLoadingContext context = ModLoadingContext.get();
        MRConfig.register(context);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MRBlocks.REGISTRY.register(modBus);
        MRItems.REGISTRY.register(modBus);
        MRTileEntityTypes.REGISTRY.register(modBus);
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(MRBlocks.BLUESTONE_WIRE.get(), RenderType.getCutout());
    }

}
