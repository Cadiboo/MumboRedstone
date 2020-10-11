package com.supercat765.mumboredstone.blocks;

import com.supercat765.mumboredstone.MRConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BehaviorPlaceBlock extends BehaviorDefaultDispenseItem {

	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(enumfacing);
		Block b;	
        if(stack.getItem() instanceof IPlantable){
            b = ((IPlantable)stack.getItem()).getPlant(source.getWorld(), pos).getBlock();
        }
        else
        {
        	 b = Block.getBlockFromItem(stack.getItem());
        }
        
        
        
        if(!source.getWorld().isAirBlock(pos) && isplant(b)){
        	pos=pos.up();
        }
        
        if(canplace(source.getWorld(),pos,b)){
        	ItemStack itemstack = stack.splitStack(1);
        	IBlockState state = b.getStateFromMeta(itemstack.getItemDamage());
        	if(state.getProperties().containsKey(BlockHorizontal.FACING)){
        		if(!(enumfacing==EnumFacing.UP) && !(enumfacing==EnumFacing.DOWN)){
        			state=state.withProperty(BlockHorizontal.FACING,enumfacing);
        		}
        	}
        	if(state.getProperties().containsKey(BlockDispenser.FACING)){
        		state=state.withProperty(BlockDispenser.FACING,enumfacing);
        	}
			source.getWorld().setBlockState(pos, state );
        	if(is2tall(b)){
        		source.getWorld().setBlockState(pos.up(), b.getStateFromMeta(itemstack.getItemDamage()).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER));
        	}
        }
        
        
        return stack;
	}

	private boolean is2tall(Block b) {
		return b instanceof BlockDoublePlant;
	}

	private boolean canplace(World world, BlockPos pos, Block b) {
		if(world.isAirBlock(pos) && b.canPlaceBlockAt(world, pos)){
			if(is2tall(b)){
				return world.isAirBlock(pos.up());
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	private boolean isplant(Block b) {
		return b instanceof BlockBush || b == Blocks.CACTUS;
	}

}
