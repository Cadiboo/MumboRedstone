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

    private void updatePowered(boolean isPowered) {
        world.setBlockState(pos, getBlockState().with(POWERED, isPowered));
        world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
    }

    /** Called when this TE is added to the world, either by placing or being loaded from disk. */
    @Override
    public void onLoad() {
        if (!world.isRemote)
            onAddedToChannel();
    }

    @Override
    public void setChannel(int newChannel, boolean markDirty) {
        super.setChannel(newChannel, markDirty);
        onAddedToChannel();
    }

    private void onAddedToChannel() {
        getListeners(getChannel()).add(this);
        isChannelPowered = isChannelPowered(getChannel());
    }

    public void onChannelPoweredChanged(boolean newPowered) {
        isChannelPowered = newPowered;
    }

    @Override
    public void onRemovedFromChannel() {
        getListeners(getChannel()).remove(this);
    }

}
