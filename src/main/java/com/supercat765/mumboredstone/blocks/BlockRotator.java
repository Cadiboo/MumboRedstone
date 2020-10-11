package com.supercat765.mumboredstone.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRotator extends BlockDirectional {
	
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
	
	int direction;
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public BlockRotator(int i) {
		super(Material.ANVIL);
		direction=i;
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, Boolean.valueOf(false)));
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();

        if (flag && !flag1)
        {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
        }
        else if (!flag && flag1)
        {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.rotate(worldIn, pos);
        }
    }
    
    public int tickRate(World worldIn)
    {
        return 4;
    }

    
    
    @Override
	public boolean causesSuffocation(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	@Override
	public boolean isFullBlock(IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
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
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void rotate(World worldIn, BlockPos pos) {
    	IBlockState thisstate = worldIn.getBlockState(pos);
		EnumFacing dir = thisstate.getValue(FACING);
		BlockPos rotatepos = pos.offset(dir);
		IBlockState rotateState = worldIn.getBlockState(rotatepos);
		
		int rotation=this.getDirection();
		
		int limit=15;
		
		//System.out.println(rotateState.getBlock() instanceof BlockRotator);
		//System.out.println(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(FACING));
		
		while(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(FACING) && limit>0){
			rotation += ((BlockRotator)rotateState.getBlock()).getDirection();
			limit--;
			rotatepos = rotatepos.offset(dir);
			rotateState = worldIn.getBlockState(rotatepos);
			
			//System.out.println(rotateState.getBlock() instanceof BlockRotator);
			//System.out.println(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(FACING));
		}
		
		rotation = rotation%4;
		
		switch(dir){
		case SOUTH:
		case UP:
		case EAST:
			rotation = 4-rotation;
			rotation = rotation%4;
			break;
		default:
			break;
		}
		
		if(rotateState.getBlock()==Blocks.PISTON_EXTENSION)return;
		if(rotateState.getBlock()==Blocks.PISTON_HEAD)return;
		if(rotateState.getBlock() instanceof BlockPistonBase && rotateState.getValue(BlockPistonBase.EXTENDED))return;
		
		if(rotateState.getProperties().containsKey(FACING)){
		
		Axis axis;
		switch(dir){
		case UP:
		case DOWN:
			axis = Axis.Y;
			break;
		case EAST:
		case WEST:
		default:
			axis = Axis.X;
			break;
		case NORTH:
		case SOUTH:
			axis = Axis.Z;
			break;
		}
		EnumFacing current = rotateState.getValue(FACING);
		for(int i=0;i<rotation;i++){
			current=current.rotateAround(axis);
		}
		rotateState=rotateState.withProperty(FACING,current);
		worldIn.setBlockState(rotatepos, rotateState);
		
		Block b = rotateState.getBlock();
		if(b==Blocks.DISPENSER || b==Blocks.DROPPER || b==Blocks.OBSERVER)
		{
			worldIn.scheduleBlockUpdate(rotatepos, rotateState.getBlock(), 1, 1);
		}
		
		
		worldIn.playSound(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, (float) .5, 1, false);
		}
		else
		{
			IBlockState rotateState2 = rotateState;
			if(rotation==1){
				rotateState2=rotateState.withRotation(Rotation.CLOCKWISE_90);
			}
			if(rotation==3){
				rotateState2=rotateState.withRotation(Rotation.COUNTERCLOCKWISE_90);
			}
			if(rotation==2){
				rotateState2=rotateState.withRotation(Rotation.CLOCKWISE_180);
			}
			if(rotateState2!=rotateState){
				worldIn.setBlockState(rotatepos, rotateState2);
				Block b = rotateState.getBlock();
				if(b==Blocks.DISPENSER || b==Blocks.DROPPER || b==Blocks.OBSERVER)
				{
					worldIn.scheduleBlockUpdate(rotatepos, rotateState.getBlock(), 1, 1);
				}
				worldIn.playSound(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, (float) .5, 1, false);
			}
		}
		
		List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(rotatepos, rotatepos.add(1, 1, 1)));
		if(!list.isEmpty()){
			for(Entity e:list){
				if(e instanceof EntityLiving){
					EntityLookHelper look = ((EntityLiving)e).getLookHelper();
					EntityMoveHelper move = ((EntityLiving)e).getMoveHelper();
					PathNavigate path = ((EntityLiving)e).getNavigator();
					path.clearPath();
					float f1 = MathHelper.sin(e.rotationYaw * 0.017453292F);
		            float f2 = MathHelper.cos(e.rotationYaw * 0.017453292F);
					((EntityLiving)e).setAttackTarget(null);
					if(rotation==1){
						look.setLookPosition(e.posX, e.posY, e.posZ, e.rotationYaw+90, e.rotationPitch);
						move.setMoveTo(e.posX-f2, e.posY, e.posZ-f1, .1);
					}
					if(rotation==2){
						look.setLookPosition(e.posX, e.posY, e.posZ, e.rotationYaw+180, e.rotationPitch);
						move.setMoveTo(e.posX+f1, e.posY, e.posZ-f2, .1);
					}
					if(rotation==3){
						look.setLookPosition(e.posX, e.posY, e.posZ, e.rotationYaw+270, e.rotationPitch);
						move.setMoveTo(e.posX+f2, e.posY, e.posZ+f1, .1);
					}
				}
				else
				{
					if(rotation==1){
						e.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw+90, e.rotationPitch);
					}
					if(rotation==2){
						e.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw+180, e.rotationPitch);
					}
					if(rotation==3){
						e.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw+270, e.rotationPitch);
					}
				}
			}
		}
    }
    
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]{FACING,TRIGGERED});
    }
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite()).withProperty(TRIGGERED, Boolean.valueOf(false));
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
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

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }
}
