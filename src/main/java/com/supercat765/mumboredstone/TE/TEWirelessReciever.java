package com.supercat765.mumboredstone.TE;

import javax.annotation.Nullable;

import com.supercat765.mumboredstone.MRConfig;
import com.supercat765.mumboredstone.blocks.BlockWireless;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TEWirelessReciever extends TileEntity implements ITickable{
	
	int Channel;
	
	public TEWirelessReciever(){
		Channel=0;
	}

	
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
        this.Channel = compound.getByte("channel");
    }
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		compound.setInteger("channel", Channel);
		
        return super.writeToNBT(compound);
    }
	
	public int getChannel(){
		return Channel;
	}
	
	public void setChannel(int at){
		Channel=at;
		this.world.markBlockRangeForRenderUpdate(pos, pos);
		this.world.notifyBlockUpdate(pos, getState(), getState(), 3);
		this.world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	@Override
	public void update() {
		boolean p = shouldBePowered();
		if(this.isPowered() && (!p))setPowered(false);
		else if((!this.isPowered()) && p)setPowered(true);
	}
	
	private boolean shouldBePowered() {
		for(TileEntity TE:this.getWorld().loadedTileEntityList){
			if(TE instanceof TEWirelessSender){
				TEWirelessSender WI = (TEWirelessSender)TE;
				if(this.getPos().distanceSq(WI.getPos())<MRConfig.WirelessRange){
					if(WI.getChannel()==this.getChannel()){
						if(WI.isPowered()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public void setPowered(boolean p){
		this.getWorld().setBlockState(getPos(), this.getWorld().getBlockState(getPos()).withProperty(BlockWireless.POWERED, p));
	}
	
	public boolean isPowered(){
		return this.getWorld().getBlockState(getPos()).getValue(BlockWireless.POWERED);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}
}
