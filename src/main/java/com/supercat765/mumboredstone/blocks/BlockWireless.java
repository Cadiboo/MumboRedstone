package com.supercat765.mumboredstone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWireless extends Block{
	
	public static final PropertyInteger CHANNEL = PropertyInteger.create("channel", 0, 3);
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	
	public BlockWireless() {
		super(Material.CIRCUITS);
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
    		if(playerIn.isSneaking()){
    			//System.out.println("Dunce");
    			worldIn.markBlockRangeForRenderUpdate(pos, pos);
    			cycleChannel(worldIn,pos);
    			return true;
    		}
        }
		return true;
    }

	public abstract void cycleChannel(World worldIn, BlockPos pos);
	
}
