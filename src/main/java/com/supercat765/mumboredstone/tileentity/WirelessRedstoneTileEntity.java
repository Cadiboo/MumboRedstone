package com.supercat765.mumboredstone.tileentity;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.LinkedList;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

/**
 * Wireless redstone between any blocks that are all in loaded chunks and on the same channel.
 */
public abstract class WirelessRedstoneTileEntity extends TileEntity {

    public static final String CHANNEL_KEY = "channel";
    public static final int DEFAULT_CHANNEL = 0;

    private static final Int2IntMap CHANNEL_STATES = new Int2IntArrayMap();
    /** TE's remove themselves from this list when they're removed from the world so we don't need to worry about WeakReferences. */
    static Int2ObjectMap<List<WirelessReceiverTileEntity>> CHANNEL_LISTENERS = new Int2ObjectArrayMap<>();

    private int channel;

    public WirelessRedstoneTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int newChannel) {
        channel = newChannel;
        // "markDirty" tells vanilla that the chunk containing the tile entity has
        // changed and means the game will save the chunk to disk later.
        markDirty();
    }

    public boolean isChannelPowered(int channel) {
        int poweredTransmitters = CHANNEL_STATES.getOrDefault(channel, 0);
        return poweredTransmitters > 0;
    }

    public static List<WirelessReceiverTileEntity> getListeners(int channel) {
        return CHANNEL_LISTENERS.computeIfAbsent(channel, $ -> new LinkedList<>());
    }

    public static void powerChannel(int channel, boolean channelPowered) {
        int poweredTransmitters = CHANNEL_STATES.getOrDefault(channel, 0);
        if (channelPowered)
            CHANNEL_STATES.put(channel, ++poweredTransmitters);
        else if (poweredTransmitters > 0)
            CHANNEL_STATES.put(channel, --poweredTransmitters);
        boolean finalIsChannelPowered = poweredTransmitters > 0;
        getListeners(channel).forEach(te -> te.onChannelPoweredChanged(finalIsChannelPowered));
    }

    protected void updatePowered(boolean isPowered) {
        world.setBlockState(pos, getBlockState().with(POWERED, isPowered));
        world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt = super.write(nbt);
        if (world == null || !world.isRemote)
            nbt.putInt(CHANNEL_KEY, channel);
        return nbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (world == null || !world.isRemote)
            setChannel(nbt.getInt(CHANNEL_KEY));
    }

    @Override
    public void remove() {
        super.remove();
        removeFromChannels();
    }


    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        removeFromChannels();
    }

    abstract void removeFromChannels();

}
