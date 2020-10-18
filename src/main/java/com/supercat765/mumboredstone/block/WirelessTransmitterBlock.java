package com.supercat765.mumboredstone.block;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Pretty much a copy of the lever block but without a facing direction
 */
public class WirelessTransmitterBlock extends WirelessRedstoneBlock {

    public WirelessTransmitterBlock(Properties properties) {
        super(properties);
    }

    /** Copied and modified from the Lamp */
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        updateState(state, worldIn, pos);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        updateState(state, worldIn, pos);
    }

    private void updateState(BlockState state, World worldIn, BlockPos pos) {
        if (worldIn.isRemote)
            return;
        boolean isPowered = state.get(POWERED);
        if (isPowered != worldIn.isBlockPowered(pos))
            worldIn.setBlockState(pos, state.with(POWERED, !isPowered), 3);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MRTileEntityTypes.WIRELESS_TRANSMITTER.get().create();
    }
}
