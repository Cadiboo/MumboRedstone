package com.supercat765.mumboredstone.init;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.block.BluestoneWireBlock;
import com.supercat765.mumboredstone.block.WirelessButtonBlock;
import com.supercat765.mumboredstone.block.WirelessLeverBlock;
import com.supercat765.mumboredstone.block.WirelessReceiverBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MRBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MumboRedstone.MOD_ID);

    public static final RegistryObject<BluestoneWireBlock> BLUESTONE_WIRE = REGISTRY.register(
        "bluestone_wire",
        // Properties copied from vanilla Blocks.REDSTONE_WIRE
        () -> new BluestoneWireBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance())
    );
    public static final RegistryObject<WirelessButtonBlock> WIRELESS_BUTTON = REGISTRY.register(
        "wireless_button",
        // Properties copied from vanilla Blocks.OAK_BUTTON
        () -> new WirelessButtonBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD))
    );
    public static final RegistryObject<WirelessLeverBlock> WIRELESS_LEVER = REGISTRY.register(
        "wireless_lever",
        // Properties copied from vanilla Blocks.LEVER
        () -> new WirelessLeverBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD))
    );
    public static final RegistryObject<WirelessReceiverBlock> WIRELESS_RECEIVER = REGISTRY.register(
        "wireless_receiver",
        // Properties copied from vanilla Blocks.REPEATER
        () -> new WirelessReceiverBlock(AbstractBlock.Properties.create(Material.MISCELLANEOUS).zeroHardnessAndResistance().sound(SoundType.WOOD))
    );

}
