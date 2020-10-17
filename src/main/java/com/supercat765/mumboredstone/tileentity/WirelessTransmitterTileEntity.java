package com.supercat765.mumboredstone.tileentity;

import com.supercat765.mumboredstone.init.MRTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class WirelessTransmitterTileEntity extends WirelessRedstoneTileEntity implements ITickableTileEntity {

    private boolean wasPowered = false;

    public WirelessTransmitterTileEntity() {
        this(MRTileEntityTypes.WIRELESS_TRANSMITTER.get());
    }

    public WirelessTransmitterTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState blockState = getBlockState();
        if (!blockState.hasProperty(POWERED))
            return;
        boolean currentlyPowered = blockState.get(POWERED);
        if (currentlyPowered && !wasPowered)
            powerChannel(getChannel(), true);
        else if (!currentlyPowered && wasPowered)
            powerChannel(getChannel(), false);
        wasPowered = currentlyPowered;
    }

    @Override
    public void onRemovedFromChannel() {
        if (wasPowered)
            powerChannel(getChannel(), false);
        wasPowered = false;
    }

}
