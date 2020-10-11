package com.supercat765.mumboredstone.block;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * Outputs a wireless signal on it's channel.
 */
public class WirelessButtonBlock extends WoodButtonBlock {
    public WirelessButtonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MRTileEntityTypes.WIRELESS_REDSTONE_SENDER.get().create();
    }
}
