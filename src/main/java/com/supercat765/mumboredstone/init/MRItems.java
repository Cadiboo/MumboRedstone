package com.supercat765.mumboredstone.init;

import com.supercat765.mumboredstone.MumboRedstone;
import net.minecraft.block.Block;
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
    public static final RegistryObject<Item> WIRELESS_BUTTON = REGISTRY.register(
        "wireless_button",
        () -> new BlockItem(MRBlocks.WIRELESS_BUTTON.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );
    public static final RegistryObject<Item> WIRELESS_LEVER = REGISTRY.register(
        "wireless_lever",
        () -> new BlockItem(MRBlocks.WIRELESS_LEVER.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );
    public static final RegistryObject<Item> WIRELESS_RECEIVER = REGISTRY.register(
        "wireless_receiver",
        () -> new BlockItem(MRBlocks.WIRELESS_RECEIVER.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );

}
