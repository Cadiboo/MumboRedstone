package com.supercat765.mumboredstone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BluestoneWireBlock extends RedstoneWireBlock {

    /** Power can be from 0-15: 16 values. */
    public static final int POWER_VALUES = 16;
    public static final Vector3f[] POWER_COLORS = new Vector3f[POWER_VALUES];

    public BluestoneWireBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    /** Copied and tweaked from vanilla because we need to make it use our power colors, not the default redstone ones. */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int power = stateIn.get(POWER);
        if (power != 0) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = stateIn.get(FACING_PROPERTY_MAP.get(direction));
                switch (redstoneside) {
                    case UP:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.spawnPoweredParticle(worldIn, rand, pos, POWER_COLORS[power], Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }

        }
    }

    /** Copied from vanilla because it's private, could use an AT instead but this is easier. */
    @OnlyIn(Dist.CLIENT)
    private void spawnPoweredParticle(World world, Random random, BlockPos pos, Vector3f color, Direction directionFrom, Direction directionTo, float minChance, float maxChance) {
        float chance = maxChance - minChance;
        if (random.nextFloat() < 0.2F * chance) {
            float multiplierFrom = 0.4375F;
            float multiplierTo = minChance + chance * random.nextFloat();
            double x = 0.5D + multiplierFrom * directionFrom.getXOffset() + multiplierTo * directionTo.getXOffset();
            double y = 0.5D + multiplierFrom * directionFrom.getYOffset() + multiplierTo * directionTo.getYOffset();
            double z = 0.5D + multiplierFrom * directionFrom.getZOffset() + multiplierTo * directionTo.getZOffset();
            world.addParticle(new RedstoneParticleData(color.getX(), color.getY(), color.getZ(), 1.0F), pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0.0D, 0.0D, 0.0D);
        }
    }

}
