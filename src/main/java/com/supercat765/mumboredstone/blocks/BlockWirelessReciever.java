package com.supercat765.mumboredstone.blocks;

import com.supercat765.mumboredstone.MRConfig;
import com.supercat765.mumboredstone.TE.TEWirelessReciever;
import com.supercat765.mumboredstone.TE.TEWirelessSender;

import net.minecraft.block.BlockLever;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWirelessReciever extends BlockWireless implements ITileEntityProvider{
	protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEWirelessReciever();
	}
	
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
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

	
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CHANNEL,POWERED});
    }
	
	public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(POWERED)?1:0);
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, (meta)>0);
    }
	
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Boolean)blockState.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return ((Boolean)blockState.getValue(POWERED)).booleanValue() ? 15 : 0;
    }
    
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
    	TileEntity TE = worldIn.getTileEntity(pos);
    	if(TE!=null && TE instanceof TEWirelessReciever)
    		return state.withProperty(CHANNEL, (int)((TEWirelessReciever)TE).getChannel());
    	else
    		return state.withProperty(CHANNEL, 0);
    }


	@Override
	public void cycleChannel(World worldIn, BlockPos pos) {
		
		TileEntity TE=worldIn.getTileEntity(pos);
		if(TE!=null && TE instanceof TEWirelessReciever){
			TEWirelessReciever WI = (TEWirelessReciever)TE;
			int at = WI.getChannel();
			if(at<MRConfig.SurvivalWirelessChannels){
				at=at+1;
				if(at==MRConfig.SurvivalWirelessChannels){
					at=0;
				}
				WI.setChannel(at);
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockState(pos, state.withProperty(CHANNEL,(int)at));
				worldIn.notifyBlockUpdate(pos, state, state.withProperty(CHANNEL,(int)at), 3);
			}
		}
	}

}
