package com.supercat765.mumboredstone.block;

import com.supercat765.mumboredstone.init.MRBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * This has been rather painful to get to work.
 * Pretty much every method I've needed to change has been private and/or static.
 * Therefore there are a lot of ATs.
 */
public class BluestoneWireBlock extends RedstoneWireBlock {

    /** Power can be from 0-15: 16 values. */
    public static final int POWER_VALUES = 16;
    public static final Vector3f[] POWER_COLORS = new Vector3f[POWER_VALUES];

    public BluestoneWireBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    /** Fixes single redstone & bluestone next to each other powering the redstone infinitely. */
    @Override
    public boolean canProvidePower(BlockState state) {
        return super.canProvidePower(state) && Blocks.REDSTONE_WIRE.canProvidePower(state);
    }

    /** Fixes single redstone & bluestone next to each other powering the redstone infinitely. */
    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if (!canProvidePower(blockState))
            return 0;
        return super.getStrongPower(blockState, blockAccess, pos, side);
    }

    /** Fixes single redstone & bluestone next to each other powering the redstone infinitely. */
    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if (!canProvidePower(blockState))
            return 0;
        return super.getWeakPower(blockState, blockAccess, pos, side);
    }

    /**
     * Fixes single redstone & bluestone next to each other powering the bluestone infinitely.
     * The super method in {@link RedstoneWireBlock} is private, it has been ATed to public.
     * Tweaked version of {@link RedstoneWireBlock#getStrongestSignal}.
     */
    @Override
    public int getStrongestSignal(World world, BlockPos pos) {
        this.canProvidePower = false;
        // Use custom method so we can filter out redstone wires
        int i = getRedstonePowerFromNeighbors(world, pos);
        this.canProvidePower = true;
        int j = 0;
        if (i < 15) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(direction);
                BlockState blockstate = world.getBlockState(blockpos);
                j = Math.max(j, this.getPower(blockstate));
                BlockPos blockpos1 = pos.up();
                if (blockstate.isNormalCube(world, blockpos) && !world.getBlockState(blockpos1).isNormalCube(world, blockpos1)) {
                    j = Math.max(j, this.getPower(world.getBlockState(blockpos.up())));
                } else if (!blockstate.isNormalCube(world, blockpos)) {
                    j = Math.max(j, this.getPower(world.getBlockState(blockpos.down())));
                }
            }
        }

        return Math.max(i, j - 1);
    }

    /**
     * Fixes single redstone & bluestone next to each other powering the bluestone infinitely.
     * Tweaked version of {@link World#getRedstonePowerFromNeighbors}.
     */
    protected int getRedstonePowerFromNeighbors(World world, BlockPos pos) {
        int i = 0;
        for (Direction direction : World.FACING_VALUES) {
            // Use custom method so we can filter out redstone wires
            int j = getRedstonePower(world, pos, direction);
            if (j >= 15) {
                return 15;
            }
            if (j > i) {
                i = j;
            }
        }
        return i;
    }

    /**
     * Fixes single redstone & bluestone next to each other powering the bluestone infinitely.
     * Tweaked version of {@link World#getRedstonePower}.
     */
    private int getRedstonePower(World world, BlockPos pos, Direction direction) {
        BlockPos pos1 = pos.offset(direction);
        BlockState blockstate = world.getBlockState(pos1);
        // Filter out redstone wires & ourselves
        if (blockstate.getBlock() == Blocks.REDSTONE_WIRE)
            return 0;
        if (blockstate.getBlock() == MRBlocks.BLUESTONE_WIRE.get())
            return 0;
        int i = blockstate.getWeakPower(world, pos1, direction);
        return blockstate.shouldCheckWeakPower(world, pos1, direction) ? Math.max(i, world.getStrongPower(pos1)) : i;
    }

    /**
     * Copied and tweaked from vanilla because we need to make it use our power colors, not the default redstone ones.
     * NOTE: The method this calls {@link #spawnPoweredParticle} has been ATed from private to public to prevent code duplication.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int power = stateIn.get(POWER);
        if (power != 0) {
            if (POWER_COLORS[0] == null) {
                Logger logger = LogManager.getLogger(this);
                logger.warn("Bluestone colors not initialised, did something go wrong with registering and/or loading the config?");
                return;
            }
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = stateIn.get(FACING_PROPERTY_MAP.get(direction));
                switch (redstoneside) {
                    case UP:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }

        }
    }

    /**
     * Makes bluestone not connect to redstone.
     * ATed this method in {@link RedstoneWireBlock} from private to public so that it can be overridden.
     * This is a perfect copy of the non-overridden method except that instead of calling the static method
     * {@link RedstoneWireBlock#canConnectTo}, it calls our static method {@link BluestoneWireBlock#canConnectTo}.
     * NOTE: The method this calls {@link #canPlaceOnTopOf} has been ATed from private to public to prevent code duplication.
     */
    @Override
    public RedstoneSide recalculateSide(IBlockReader reader, BlockPos pos, Direction direction, boolean nonNormalCubeAbove) {
        BlockPos blockpos = pos.offset(direction);
        BlockState blockstate = reader.getBlockState(blockpos);
        if (nonNormalCubeAbove) {
            boolean flag = this.canPlaceOnTopOf(reader, blockpos, blockstate);
            if (flag && canConnectTo(reader.getBlockState(blockpos.up()), reader, blockpos.up(), null)) {
                if (blockstate.isSolidSide(reader, blockpos, direction.getOpposite()))
                    return RedstoneSide.UP;
                return RedstoneSide.SIDE;
            }
        }
        return !canConnectTo(blockstate, reader, blockpos, direction) && (blockstate.isNormalCube(reader, blockpos) || !canConnectTo(reader.getBlockState(blockpos.down()), reader, blockpos.down(), null)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    /** Makes bluestone not connect to redstone. */
    protected static boolean canConnectTo(BlockState blockState, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        if (blockState.getBlock() == Blocks.REDSTONE_WIRE)
            return false;
        if (blockState.getBlock() == MRBlocks.BLUESTONE_WIRE.get())
            return true;
        return RedstoneWireBlock.canConnectTo(blockState, world, pos, side);
    }

    /** Makes redstone not connect to bluestone. */
    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        if (side == null)
            return false;
        BlockState beingCheckedBy = world.getBlockState(pos.offset(side.getOpposite()));
        return beingCheckedBy.getBlock() == MRBlocks.BLUESTONE_WIRE.get();
    }

}
