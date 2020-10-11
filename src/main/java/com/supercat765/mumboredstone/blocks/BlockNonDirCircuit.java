package com.supercat765.mumboredstone.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockNonDirCircuit extends Block{

	protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
	
	public static final PropertyInteger POWER = PropertyInteger.create("power", 1, 15);
	
	public BlockNonDirCircuit() {
		super(Material.CIRCUITS);
		// TODO Auto-generated constructor stub
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            this.check(worldIn, pos, state);
        }
    }
	
	public int getInput(World worldIn, BlockPos pos) {
		int out = 0;
		for (EnumFacing enumfacing : EnumFacing.HORIZONTALS)
        {
			int dir=worldIn.getRedstonePower(pos.offset(enumfacing), enumfacing);
			IBlockState block=worldIn.getBlockState(pos.offset(enumfacing));
			if(block.getBlock() == Blocks.REDSTONE_WIRE){
				dir=((Integer)block.getValue(BlockRedstoneWire.POWER)).intValue();
			}
			if(block.getBlock() instanceof BlockRedstoneDiode && block.getValue(BlockRedstoneDiode.FACING)==enumfacing.getOpposite()){
				dir=worldIn.getBlockState(pos).getValue(POWER)-1;
			}
            if (dir>out)
            {
            	out=dir;
            }
        }
		return out;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote)
        {
            this.check(worldIn, pos, state);
        }
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!worldIn.isRemote)
        {
            this.check(worldIn, pos, state);
        }
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	public abstract void check(World worldIn, BlockPos pos, IBlockState state);
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return REDSTONE_DIODE_AABB;
    }
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	protected boolean isAlternateInput(IBlockState state)
    {
        return true;
    }
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid();
    }

    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return true;
    }
}
