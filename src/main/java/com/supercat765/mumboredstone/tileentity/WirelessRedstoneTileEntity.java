package com.supercat765.mumboredstone.tileentity;

import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.LinkedList;
import java.util.List;

/**
 * Wireless redstone between any blocks that are all in loaded chunks and on the same channel.
 * TODO: I ripped out the previous 4-channel system that changed on right click, need to add another way to change the channel (maybe a GUI?)
 */
public abstract class WirelessRedstoneTileEntity extends TileEntity {

    public static final String CHANNEL_KEY = "channel";

    protected static Int2BooleanMap CHANNEL_STATES = new Int2BooleanArrayMap();
    /** TE's remove themselves from this list when they're removed from the world so we don't need to worry about WeakReferences. */
    static Int2ObjectMap<List<WirelessReceiverTileEntity>> CHANNEL_LISTENERS = new Int2ObjectArrayMap<>();

    private int channel;

    public WirelessRedstoneTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public int getChannel() {
        return channel;
    }

    public static List<WirelessReceiverTileEntity> getListeners(int channel) {
        return CHANNEL_LISTENERS.computeIfAbsent(channel, $ -> new LinkedList<>());
    }

    public static void notifyListeners(int channel, boolean channelPowered) {
        if (CHANNEL_STATES.get(channel) == channelPowered)
            return;
        CHANNEL_STATES.put(channel, channelPowered);
        getListeners(channel).forEach(te -> te.onChannelPoweredChanged(channelPowered));
    }

    public void setChannel(int newChannel) {
        channel = newChannel;
        // "markDirty" tells vanilla that the chunk containing the tile entity has
        // changed and means the game will save the chunk to disk later.
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt = super.write(nbt);
        if (!world.isRemote)
            nbt.putInt(CHANNEL_KEY, channel);
        return nbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (!world.isRemote)
            setChannel(nbt.getInt(CHANNEL_KEY));
    }

}
