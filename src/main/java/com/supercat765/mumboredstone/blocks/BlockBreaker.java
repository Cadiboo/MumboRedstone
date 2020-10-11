package com.supercat765.mumboredstone.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.supercat765.mumboredstone.MRConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBreaker extends BlockDirectional{
	

    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

	public BlockBreaker() {
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING,TRIGGERED});
    }
	
	public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
    
    @Override
	public boolean isFullBlock(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();

        if (flag && !flag1)
        {
        	
        	worldIn.scheduleUpdate(pos, this, 1);
        	
            //worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 3);
        }
        else if (!flag && flag1)
        {
        	worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        	
            //worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 3);
        }
    }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		
		boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();

        if(flag!=flag1){
        if (!flag1)
        {
        	worldIn.addBlockEvent(pos, this, 0, 0);
        }
        else
        {
        	worldIn.addBlockEvent(pos, this, 1, 0);
        }
        }
        else
        {
        	worldIn.addBlockEvent(pos, this, 1, 0);
        }
    }

	
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
		
		EnumFacing facing = state.getValue(this.FACING);
		BlockPos pos2 = pos.offset(facing);
		IBlockState bl=worldIn.getBlockState(pos2);
		
		if (!worldIn.isRemote)
        {
			if (id==0)
	        {
				if(worldIn.isAirBlock(pos2) || (!(bl.getMobilityFlag()==EnumPushReaction.BLOCK)
	    				&& MRConfig.canbreakerbreak(bl.getBlock().getRegistryName().toString()))){
					worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.2F, worldIn.rand.nextFloat() * 0.25F + 1.2F);
				}
	        }
			else
			{
				worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.2F, worldIn.rand.nextFloat() * 0.25F + 1.2F);
			}
        }
		
        if (id==0)
        {
    		if(worldIn.isAirBlock(pos2) || (!(bl.getMobilityFlag()==EnumPushReaction.BLOCK)
    				&& MRConfig.canbreakerbreak(bl.getBlock().getRegistryName().toString()))){
    			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 3);
    			
    			
    			if(!worldIn.isAirBlock(pos2) && !(bl.getMobilityFlag()==EnumPushReaction.BLOCK)
    					&& MRConfig.canbreakerbreak(bl.getBlock().getRegistryName().toString()))worldIn.destroyBlock(pos2, true);
    			if(!worldIn.isRemote){
    			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos2, pos2.add(1, 1, 1)));
    			if(!list.isEmpty()){
    				for(Entity e:list){
    					switch(facing){
    					case DOWN:
    	    				e.addVelocity(0, -MRConfig.BreakerEntityPush, 0);
    	    				e.move(MoverType.PISTON, 0, -MRConfig.BreakerEntityPush, 0);
    						break;
    					case EAST:
    	    				e.addVelocity(MRConfig.BreakerEntityPush, .1, 0);
    	    				e.move(MoverType.PISTON, MRConfig.BreakerEntityPush, .1, 0);
    						break;
    					case NORTH:
    	    				e.addVelocity(0, .1, -MRConfig.BreakerEntityPush);
    	    				e.move(MoverType.PISTON, 0, .1, -MRConfig.BreakerEntityPush);
    						break;
    					case SOUTH:
    	    				e.addVelocity(0, .1, MRConfig.BreakerEntityPush);
    	    				e.move(MoverType.PISTON, 0, .1, MRConfig.BreakerEntityPush);
    						break;
    					case UP:
    	    				e.addVelocity(0, MRConfig.BreakerEntityPush, 0);
    	    				e.move(MoverType.PISTON, 0, MRConfig.BreakerEntityPush, 0);
    						break;
    					case WEST:
    	    				e.addVelocity(-MRConfig.BreakerEntityPush, .1, 0);
    	    				e.move(MoverType.PISTON, -MRConfig.BreakerEntityPush, .1, 0);
    						break;
    					default:
    						break;
    					
    					}
    					e.velocityChanged=true;
    				}
    			}
    			}
    			
    		}
    		else
    		{
    			return false;
    		}
        }
        else
        {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 3);
        }
		
		return true;
    }
	
    public void updateTick2(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	
    	boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();

        if (!flag1)
        {
        	EnumFacing facing = state.getValue(this.FACING);
    		BlockPos pos2 = pos.offset(facing);
    		IBlockState bl=worldIn.getBlockState(pos2);
    		
    		if(worldIn.isAirBlock(pos2) || (!(bl.getMobilityFlag()==EnumPushReaction.BLOCK)
    				&& MRConfig.canbreakerbreak(bl.getBlock().getRegistryName().toString()))){
    			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 3);
    			
    			
    			if(!worldIn.isAirBlock(pos2) && !(bl.getMobilityFlag()==EnumPushReaction.BLOCK)
    					&& MRConfig.canbreakerbreak(bl.getBlock().getRegistryName().toString()))worldIn.addBlockEvent(pos, this, 0, 0);
    			if(!worldIn.isRemote){
    			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos2, pos2.add(1, 1, 1)));
    			if(!list.isEmpty()){
    				for(Entity e:list){
    					switch(facing){
    					case DOWN:
    	    				e.addVelocity(0, -MRConfig.BreakerEntityPush, 0);
    	    				e.move(MoverType.PISTON, 0, -MRConfig.BreakerEntityPush, 0);
    						break;
    					case EAST:
    	    				e.addVelocity(MRConfig.BreakerEntityPush, .1, 0);
    	    				e.move(MoverType.PISTON, MRConfig.BreakerEntityPush, .1, 0);
    						break;
    					case NORTH:
    	    				e.addVelocity(0, .1, -MRConfig.BreakerEntityPush);
    	    				e.move(MoverType.PISTON, 0, .1, -MRConfig.BreakerEntityPush);
    						break;
    					case SOUTH:
    	    				e.addVelocity(0, .1, MRConfig.BreakerEntityPush);
    	    				e.move(MoverType.PISTON, 0, .1, MRConfig.BreakerEntityPush);
    						break;
    					case UP:
    	    				e.addVelocity(0, MRConfig.BreakerEntityPush, 0);
    	    				e.move(MoverType.PISTON, 0, MRConfig.BreakerEntityPush, 0);
    						break;
    					case WEST:
    	    				e.addVelocity(-MRConfig.BreakerEntityPush, .1, 0);
    	    				e.move(MoverType.PISTON, -MRConfig.BreakerEntityPush, .1, 0);
    						break;
    					default:
    						break;
    					
    					}
    				}
    			}
    			}
    			worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.2F, worldIn.rand.nextFloat() * 0.25F + 1.2F);
    		}
        }
        else
        {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 3);
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.2F, worldIn.rand.nextFloat() * 0.25F + 1.2F);
        }
    }
    
    
    public boolean eventReceived2(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
    	EnumFacing facing = state.getValue(this.FACING);
		BlockPos pos2 = pos.offset(facing);
    	worldIn.destroyBlock(pos2, true);
    	
    	return true;
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
        	this.updateTick(worldIn, pos, state, worldIn.rand);
        }
    }
    
    @Override
	public int tickRate(World worldIn) {
		return MRConfig.BreakerRefresh;
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }
	
	public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(TRIGGERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }
}
