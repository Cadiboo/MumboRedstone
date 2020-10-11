package com.supercat765.mumboredstone.init;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.tileentity.WirelessReceiverTileEntity;
import com.supercat765.mumboredstone.tileentity.WirelessSenderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MRTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MumboRedstone.MOD_ID);

    public static final RegistryObject<TileEntityType<WirelessSenderTileEntity>> WIRELESS_REDSTONE_SENDER = REGISTRY.register(
        "wireless_sender",
        () -> createType(WirelessSenderTileEntity::new, MRBlocks.WIRELESS_BUTTON.get(), MRBlocks.WIRELESS_LEVER.get())
    );
    public static final RegistryObject<TileEntityType<WirelessReceiverTileEntity>> WIRELESS_REDSTONE_RECEIVER = REGISTRY.register(
        "wireless_receiver",
        () -> createType(WirelessReceiverTileEntity::new, MRBlocks.WIRELESS_RECEIVER.get())
    );

    private static <T extends TileEntity> TileEntityType<T> createType(Supplier<T> factory, Block... validBlocks) {
        return TileEntityType.Builder.create(factory, validBlocks)
            .build(null); // We don't have a data fixer for our TE
    }

}
