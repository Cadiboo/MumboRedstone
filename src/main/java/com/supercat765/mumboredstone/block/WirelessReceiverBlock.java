package com.supercat765.mumboredstone.block;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Outputs a redstone signal from it's wireless channel.
 */
public class WirelessReceiverBlock extends WirelessRedstoneBlock {

    public WirelessReceiverBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return hasSolidSideOnTop(worldIn, pos.down());
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getRedstonePower(blockState);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getRedstonePower(blockState);
    }

    private int getRedstonePower(BlockState blockState) {
        return blockState.get(POWERED) ? 15 : 0;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!state.isValidPosition(worldIn, pos)) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            spawnDrops(state, worldIn, pos, tileentity);
            worldIn.removeBlock(pos, false);
        }
    }

    /** Copied and modified from the Lever */
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.isIn(newState.getBlock())) {
            if (state.get(POWERED))
                worldIn.notifyNeighborsOfStateChange(pos, this);
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MRTileEntityTypes.WIRELESS_RECEIVER.get().create();
    }
}
