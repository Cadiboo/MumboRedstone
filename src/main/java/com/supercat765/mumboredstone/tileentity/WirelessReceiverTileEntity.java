package com.supercat765.mumboredstone.tileentity;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class WirelessReceiverTileEntity extends WirelessRedstoneTileEntity implements ITickableTileEntity {

    public WirelessReceiverTileEntity() {
        this(MRTileEntityTypes.WIRELESS_RECEIVER.get());
    }

    public WirelessReceiverTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    /** Loaded by {@link #onLoad()} and then overwritten by {@link WirelessRedstoneTileEntity#read} if this TE is being read from disk. */
    private boolean isChannelPowered = false;

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState blockState = getBlockState();
        if (!blockState.hasProperty(POWERED))
            return;
        boolean currentlyPowered = blockState.get(POWERED);
        if (!isChannelPowered && currentlyPowered)
            updatePowered(false);
        else if (isChannelPowered && !currentlyPowered)
            updatePowered(true);
    }

    /** Called when this TE is added to the world, either by placing or being loaded from disk. */
    @Override
    public void onLoad() {
        if (!world.isRemote)
            getListeners(getChannel()).add(this);
    }

    @Override
    public void setChannel(int newChannel) {
        getListeners(getChannel()).remove(this);
        super.setChannel(newChannel);
        getListeners(getChannel()).add(this);
        isChannelPowered = CHANNEL_STATES.getOrDefault(newChannel, false);
    }

    public void onChannelPoweredChanged(boolean newPowered) {
        isChannelPowered = newPowered;
    }

    @Override
    public void removeFromChannels() {
        getListeners(getChannel()).remove(this);
    }

}
