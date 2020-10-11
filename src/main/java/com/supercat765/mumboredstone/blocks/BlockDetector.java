package com.supercat765.mumboredstone.blocks;

import java.util.Random;

import com.supercat765.mumboredstone.MRConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDetector extends BlockDirectional{

	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockDetector() {
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.REDSTONE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
		this.setTickRandomly(true);
	}

	private boolean isPowered(IBlockAccess worldIn, BlockPos pos, EnumFacing facing)
    {
		for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing != facing && (worldIn.getStrongPower(pos.offset(enumfacing), enumfacing)>0))
            {
                return true;
            }
        }

        return false;
	}
	
	private boolean findEntity(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(this.FACING).getOpposite();
		for(Entity e:worldIn.loadedEntityList){
			if(e.getDistance(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5)<MRConfig.DetectorRange){
				if((!e.isInvisible())||e.isGlowing()){
						double ex = e.posX - pos.getX()-.5;
						double ey = e.posY - pos.getY()-.5;
						double ez = e.posZ - pos.getZ()-.5;
						double ex2 = e.posX - pos.getX()-.5;
						double ey2 = e.posY + e.height - pos.getY()-.5;
						double ez2 = e.posZ - pos.getZ()-.5;
						double fx = facing.getFrontOffsetX();
						double fy = facing.getFrontOffsetY();
						double fz = facing.getFrontOffsetZ();
						double[] cross = new double[3];
						crossProduct(ex, ey, ez, fx, fy, fz, cross);
						double dot = dotProduct(ex, ey, ez, fx, fy, fz);
						double crossMag = Math.sqrt(cross[0]*cross[0] + cross[1]*cross[1] + cross[2]*cross[2]);
						double angle = Math.atan2(crossMag, dot);
						
						crossProduct(ex2, ey2, ez2, fx, fy, fz, cross);
						double dot2 = dotProduct(ex2, ey2, ez2, fx, fy, fz);
						double crossMag2 = Math.sqrt(cross[0]*cross[0] + cross[1]*cross[1] + cross[2]*cross[2]);
						double angle2 = Math.atan2(crossMag2, dot);
						
						if(angle>MRConfig.DetectorAngle||angle2>MRConfig.DetectorAngle){
							RayTraceResult result1 = worldIn.rayTraceBlocks(new Vec3d(e.posX, e.posY, e.posZ), new Vec3d(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5), true, true, false);
							RayTraceResult result2 = worldIn.rayTraceBlocks(new Vec3d(e.posX, e.posY+e.height/2, e.posZ), new Vec3d(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5), true, true, false);
							RayTraceResult result3 = worldIn.rayTraceBlocks(new Vec3d(e.posX, e.posY+e.height, e.posZ), new Vec3d(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5), true, true, false);
							//System.out.println(result1.getBlockPos());
							//System.out.println(result2.getBlockPos());
							//System.out.println(result3.getBlockPos());
							if(result1.getBlockPos().equals(pos) || result2.getBlockPos().equals(pos) || result3.getBlockPos().equals(pos)){
								//System.out.println("found");
								
								return true;
							}
						}
				}
			}
		}
		return false;
	}

	public static double dotProduct(double vector1X,double vector1Y,double vector1Z,double vector2X,double vector2Y,double vector2Z){

	    return vector1X*vector2X + vector1Y*vector2Y + vector1Z*vector2Z;

	}

	public static void crossProduct(double vector1X,double vector1Y,double vector1Z,double vector2X,double vector2Y,double vector2Z, double[] outputArray){

	    outputArray[0] = vector1Y*vector2Z - vector1Z*vector2Y;     
	    outputArray[1] = vector1Z*vector2X - vector1X*vector2Z;
	    outputArray[2] = vector1X*vector2Y - vector1Y*vector2X;

	}

	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
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
    
    
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 2);
            worldIn.scheduleUpdate(pos, this, MRConfig.DetectorRefresh);
        }
        else
    	if(!isPowered(worldIn, pos, state.getValue(FACING))){
    		if(findEntity(worldIn, pos, state)){
    			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 2);
    			worldIn.scheduleUpdate(pos, this, 2);
			}
    		else
    		{
    			worldIn.scheduleUpdate(pos, this, MRConfig.DetectorRefresh);
    		}
    	}
    	
        
        //if (!worldIn.isUpdateScheduled(pos, this))
        //{
        	//worldIn.scheduleUpdate(pos, this, MRConfig.DetectorRefresh);
        //}
    	
        this.updateNeighborsInFront(worldIn, pos, state);
    }
    
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Boolean)blockState.getValue(POWERED)).booleanValue() && blockState.getValue(FACING) == side ? 15 : 0;
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
		return MRConfig.DetectorRefresh;
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}

	/**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (((Boolean)state.getValue(POWERED)).booleanValue() && worldIn.isUpdateScheduled(pos, this))
        {
            this.updateNeighborsInFront(worldIn, pos, state.withProperty(POWERED, Boolean.valueOf(false)));
        }
    }
    
    protected void updateNeighborsInFront(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }
    
    
    
}
