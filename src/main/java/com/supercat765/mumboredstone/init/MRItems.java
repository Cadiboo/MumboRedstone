package com.supercat765.mumboredstone.init;

import com.supercat765.mumboredstone.MumboRedstone;
import com.supercat765.mumboredstone.item.WirelessConfigurationToolItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MRItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MumboRedstone.MOD_ID);

    public static final RegistryObject<Item> BLUESTONE = REGISTRY.register(
        "bluestone",
        () -> new BlockItem(MRBlocks.BLUESTONE_WIRE.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );
    // TODO: Created by smelting redstone/bluestone blocks in a blast furnace
    public static final RegistryObject<Item> CRYSTAL_OSCILLATOR = REGISTRY.register(
        "crystal_oscillator",
        () -> new Item(new Item.Properties().group(ItemGroup.REDSTONE))
    );
    // TODO: Crafted with a stick, iron and a crystal oscillator
    public static final RegistryObject<Item> WIRELESS_CONFIGURATION_TOOL = REGISTRY.register(
        "wireless_configuration_tool",
        () -> new WirelessConfigurationToolItem(new Item.Properties().group(ItemGroup.REDSTONE))
    );
    // TODO: Crafted with a crystal oscillator and an extended repeater (also TODO)
    public static final RegistryObject<Item> WIRELESS_TRANSMITTER = REGISTRY.register(
        "wireless_transmitter",
        () -> new BlockItem(MRBlocks.WIRELESS_TRANSMITTER.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );
    // TODO: Crafted with a crystal oscillator and an observer
    public static final RegistryObject<Item> WIRELESS_RECEIVER = REGISTRY.register(
        "wireless_receiver",
        () -> new BlockItem(MRBlocks.WIRELESS_RECEIVER.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );

}
