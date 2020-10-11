package com.supercat765.mumboredstone.TE;

import javax.annotation.Nullable;

import com.supercat765.mumboredstone.blocks.BlockWireless;
import com.supercat765.mumboredstone.blocks.BlockWirelessOutput;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TEWirelessSender extends TileEntity implements ITickable{
	int Channel;
	
	public TEWirelessSender(){
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
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	public void setChennel(int at){
		Channel=at;
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
	
	public boolean isPowered(){
		return this.world.getBlockState(getPos()).getValue(BlockWireless.POWERED);
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

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	@Override
	public void update() {
		IBlockState state = this.getWorld().getBlockState(getPos());
		//if(state.getValue(BlockWireless.CHANNEL)!=this.getChannel()){
			this.getWorld().setBlockState(getPos(), state.withProperty(BlockWireless.CHANNEL,this.getChannel()));
		//}
	}
}
