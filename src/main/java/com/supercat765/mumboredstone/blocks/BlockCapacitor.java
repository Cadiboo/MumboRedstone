package com.supercat765.mumboredstone.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCapacitor extends BlockNonDirCircuit{
	
	public BlockCapacitor() {
		super();
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}
	
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }
	
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }
	
	public void check(World worldIn, BlockPos pos, IBlockState state) {
		int surounding = getInput(worldIn,pos);
		int outputing = ((Integer)state.getValue(POWER)).intValue();
		if(surounding!=outputing && surounding>0){
			worldIn.scheduleUpdate(pos, this, this.getDelay(state));
		}
	}

	private int getDelay(IBlockState state) {
		return 4;
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		int surounding = getInput(worldIn,pos);
		int outputing = ((Integer)state.getValue(POWER)).intValue();
		if(surounding!=outputing && surounding>0){
			if(surounding>outputing && surounding-outputing>1){
				surounding=(surounding+outputing)/2;
			}
			worldIn.setBlockState(pos, state.withProperty(POWER, surounding), 2);
			worldIn.notifyNeighborsOfStateChange(pos, this, surounding==1);
			worldIn.scheduleUpdate(pos, this, this.getDelay(state));
		}
    }

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (side==EnumFacing.UP||side==EnumFacing.DOWN)
        {
            return 0;
        }
        else
        {
            return getActiveSignal(blockAccess,pos,blockState);
        }
    }
	
	protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue()-1;
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, meta+1);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue()-1;
    }

}
