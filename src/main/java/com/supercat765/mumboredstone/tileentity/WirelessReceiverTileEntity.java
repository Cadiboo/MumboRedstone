package com.supercat765.mumboredstone.tileentity;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class WirelessReceiverTileEntity extends WirelessRedstoneTileEntity implements ITickableTileEntity {

    public WirelessReceiverTileEntity() {
        this(MRTileEntityTypes.WIRELESS_REDSTONE_RECEIVER.get());
    }

    public WirelessReceiverTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        getListeners(getChannel()).add(this);
    }

    /** Loaded by {@link WirelessRedstoneTileEntity#read} */
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
            world.setBlockState(pos, blockState.with(POWERED, false));
        else if (isChannelPowered && !currentlyPowered)
            world.setBlockState(pos, blockState.with(POWERED, true));
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
    public void remove() {
        super.remove();
        getListeners(getChannel()).remove(this);
    }

}
