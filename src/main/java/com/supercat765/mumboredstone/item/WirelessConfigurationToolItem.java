package com.supercat765.mumboredstone.item;

import com.supercat765.mumboredstone.tileentity.WirelessRedstoneTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Used for changing the channels of the wireless transmitter & receiver.
 */
public class WirelessConfigurationToolItem extends Item {
    public WirelessConfigurationToolItem(Properties properties) {
        super(properties);
    }

    // TODO: on right click of transmitter/receiver bring up GUI
    // GUI limits survival players to positive frequencies?


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote)
            return ActionResultType.SUCCESS;
        TileEntity tileEntity = world.getTileEntity(context.getPos());
        if (!(tileEntity instanceof WirelessRedstoneTileEntity))
            return ActionResultType.SUCCESS;
        WirelessRedstoneTileEntity wireless = (WirelessRedstoneTileEntity) tileEntity;
        // TODO
        wireless.setChannel(wireless.getChannel() + 1);
        return ActionResultType.SUCCESS;
    }
}
