package com.supercat765.mumboredstone.tileentity;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Wireless redstone between any blocks that are all in loaded chunks and on the same channel.
 */
@Mod.EventBusSubscriber
public abstract class WirelessRedstoneTileEntity extends TileEntity {

    public static final String CHANNEL_KEY = "channel";
    public static final int DEFAULT_CHANNEL = 0;

    private static final Int2IntMap CHANNEL_STATES = new Int2IntArrayMap();
    /** TE's remove themselves from this list when they're removed from the world so we don't need to worry about WeakReferences. */
    private static final Int2ObjectMap<List<WirelessReceiverTileEntity>> CHANNEL_LISTENERS = new Int2ObjectArrayMap<>();

    private int channel;

    public WirelessRedstoneTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public int getChannel() {
        return channel;
    }

    /**
     * @param markDirty True if the TE is not being initialised, used to prevent deadlock on world load when the TE tries to save itself while reading iteself.
     */
    public void setChannel(int newChannel, boolean markDirty) {
        onRemovedFromChannel();
        channel = newChannel;
        // "markDirty" tells vanilla that the chunk containing the tile entity has
        // changed and means the game will save the chunk to disk later.
        if (markDirty)
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

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt = super.write(nbt);
        nbt.putInt(CHANNEL_KEY, getChannel());
        return nbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (world == null || !world.isRemote)
            setChannel(nbt.getInt(CHANNEL_KEY), false);
    }

    @Override
    public void remove() {
        super.remove();
        onRemovedFromChannel();
    }


    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        onRemovedFromChannel();
    }

    abstract void onRemovedFromChannel();

    /**
     * Clients will start and stop multiple integrated servers during their lifetime, need to clean up after them.
     */
    @SubscribeEvent
    public static void onServerStoppingEvent(FMLServerStoppingEvent event) {
        CHANNEL_STATES.clear();
        CHANNEL_LISTENERS.clear();
    }

}
