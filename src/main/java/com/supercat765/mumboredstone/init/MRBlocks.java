package com.supercat765.mumboredstone.init;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.block.BluestoneWireBlock;
import com.supercat765.mumboredstone.block.WirelessReceiverBlock;
import com.supercat765.mumboredstone.block.WirelessTransmitterBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MRBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MumboRedstone.MOD_ID);

    public static final RegistryObject<BluestoneWireBlock> BLUESTONE_WIRE = REGISTRY.register(
        "bluestone_wire",
        () -> new BluestoneWireBlock(AbstractBlock.Properties.from(Blocks.REDSTONE_WIRE))
    );
    public static final RegistryObject<WirelessTransmitterBlock> WIRELESS_TRANSMITTER = REGISTRY.register(
        "wireless_transmitter",
        () -> new WirelessTransmitterBlock(AbstractBlock.Properties.from(Blocks.DISPENSER))
    );
    public static final RegistryObject<WirelessReceiverBlock> WIRELESS_RECEIVER = REGISTRY.register(
        "wireless_receiver",
        () -> new WirelessReceiverBlock(AbstractBlock.Properties.from(Blocks.REPEATER))
    );

}
