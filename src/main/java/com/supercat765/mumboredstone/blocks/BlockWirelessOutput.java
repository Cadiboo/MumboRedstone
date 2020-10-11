package com.supercat765.mumboredstone.blocks;

import com.supercat765.mumboredstone.MRConfig;
import com.supercat765.mumboredstone.TE.TEWirelessReciever;
import com.supercat765.mumboredstone.TE.TEWirelessSender;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWirelessOutput extends BlockWireless implements ITileEntityProvider{

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEWirelessSender();
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
	
	@Override
	public void cycleChannel(World worldIn, BlockPos pos) {
		TileEntity TE=worldIn.getTileEntity(pos);
		if(TE!=null && TE instanceof TEWirelessSender){
			TEWirelessSender WI = (TEWirelessSender)TE;
			int at = WI.getChannel();
			if(at<MRConfig.SurvivalWirelessChannels){
				at=at+1;
				if(at==MRConfig.SurvivalWirelessChannels){
					at=0;
				}
				
				WI.setChennel(at);
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockState(pos, state.withProperty(CHANNEL,(int)at));
				worldIn.notifyBlockUpdate(pos, state, state.withProperty(CHANNEL,(int)at), 3);
			}
		}
	}

}
