package com.supercat765.mumboredstone.block;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Pretty much a copy of the lever block but without a facing direction
 */
public class WirelessTransmitterBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public WirelessTransmitterBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    /** Copied and modified from the Lamp */
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isRemote)
            return;
        if (state.get(POWERED) != worldIn.isBlockPowered(pos))
            worldIn.setBlockState(pos, state.func_235896_a_(POWERED), 3);
    }

    /** Copied and modified from the Lamp */
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(POWERED) != worldIn.isBlockPowered(pos))
            worldIn.setBlockState(pos, state.func_235896_a_(POWERED), 3);
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
        return MRTileEntityTypes.WIRELESS_TRANSMITTER.get().create();
    }
}
