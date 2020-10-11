package com.supercat765.mumboredstone.items;

import com.supercat765.mumboredstone.blocks.BlockRotator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ItemBlockRotator extends ItemBlock{

	public ItemBlockRotator(Block block) {
		super(block);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.isSneaking()){
			rotate(worldIn,pos.offset(facing),facing.getOpposite());
			
			return EnumActionResult.SUCCESS;
		}
		// TODO Auto-generated method stub
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	public boolean canItemEditBlocks()
    {
        return false;
    }
	
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		int rotation=((BlockRotator)((ItemBlockRotator)stack.getItem()).getBlock()).getDirection();
		//System.out.println("rotator =" + rotation);
		if(entity instanceof EntityLiving){
			EntityLookHelper look = ((EntityLiving)entity).getLookHelper();
			EntityMoveHelper move = ((EntityLiving)entity).getMoveHelper();
			PathNavigate path = ((EntityLiving)entity).getNavigator();
			path.clearPath();
			float f1 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
			((EntityLiving)entity).setAttackTarget(null);
			if(rotation==1){
				look.setLookPosition(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+90, entity.rotationPitch);
				move.setMoveTo(entity.posX-f2, entity.posY, entity.posZ-f1, .1);
			}
			if(rotation==2){
				look.setLookPosition(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+180, entity.rotationPitch);
				move.setMoveTo(entity.posX+f1, entity.posY, entity.posZ-f2, .1);
			}
			if(rotation==3){
				look.setLookPosition(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+270, entity.rotationPitch);
				move.setMoveTo(entity.posX+f2, entity.posY, entity.posZ+f1, .1);
			}
		}
		if(rotation==1){
			//entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+90, entity.rotationPitch);
			entity.rotationYaw = MathHelper.wrapDegrees(entity.rotationYaw+90);
		}
		if(rotation==2){
			//entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+180, entity.rotationPitch);
			entity.rotationYaw = MathHelper.wrapDegrees(entity.rotationYaw+180);
		}
		if(rotation==3){
			//entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw+270, entity.rotationPitch);
			entity.rotationYaw = MathHelper.wrapDegrees(entity.rotationYaw+270);
		}
		
		BlockPos pos = player.getPosition();
		player.world.playSound(pos .getX()+.5, pos.getY()+.5, pos.getZ()+.5, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, (float) .5, 1, false);
		player.getCooldownTracker().setCooldown(this, 10);
        return true;
        
        
    }
	
	private void rotate(World worldIn, BlockPos pos,EnumFacing dir) {
    	IBlockState thisstate = worldIn.getBlockState(pos);
		BlockPos rotatepos = pos.offset(dir);
		IBlockState rotateState = worldIn.getBlockState(rotatepos);
		
		int rotation=((BlockRotator)this.getBlock()).getDirection();
		
		int limit=15;
		
		//System.out.println(rotateState.getBlock() instanceof BlockRotator);
		//System.out.println(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(FACING));
		
		while(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(BlockRotator.FACING) && limit>0){
			rotation += ((BlockRotator)rotateState.getBlock()).getDirection();
			limit--;
			rotatepos = rotatepos.offset(dir);
			rotateState = worldIn.getBlockState(rotatepos);
			
			//System.out.println(rotateState.getBlock() instanceof BlockRotator);
			//System.out.println(rotateState.getBlock() instanceof BlockRotator && dir == rotateState.getValue(FACING));
		}
		
		rotation = rotation%4;
		
		switch(dir){
		case SOUTH:
		case UP:
		case EAST:
			rotation = 4-rotation;
			rotation = rotation%4;
			break;
		default:
			break;
		}
		
		if(rotateState.getBlock()==Blocks.PISTON_EXTENSION)return;
		if(rotateState.getBlock()==Blocks.PISTON_HEAD)return;
		if(rotateState.getBlock() instanceof BlockPistonBase && rotateState.getValue(BlockPistonBase.EXTENDED))return;
		
		if(rotateState.getProperties().containsKey(BlockDirectional.FACING)){
		
		Axis axis;
		switch(dir){
		case UP:
		case DOWN:
			axis = Axis.Y;
			break;
		case EAST:
		case WEST:
		default:
			axis = Axis.X;
			break;
		case NORTH:
		case SOUTH:
			axis = Axis.Z;
			break;
		}
		EnumFacing current = rotateState.getValue(BlockDirectional.FACING);
		for(int i=0;i<rotation;i++){
			current=current.rotateAround(axis);
		}
		rotateState=rotateState.withProperty(BlockDirectional.FACING,current);
		worldIn.setBlockState(rotatepos, rotateState,19);
		
		Block b = rotateState.getBlock();
		if(b==Blocks.DISPENSER || b==Blocks.DROPPER || b==Blocks.OBSERVER)
		{
			//worldIn.scheduleBlockUpdate(rotatepos, rotateState.getBlock(), 1, 1);
		}
		
		
		worldIn.playSound(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, (float) .5, 1, false);
		}
		else
		{
			IBlockState rotateState2 = rotateState;
			if(rotation==1){
				rotateState2=rotateState.withRotation(Rotation.CLOCKWISE_90);
			}
			if(rotation==3){
				rotateState2=rotateState.withRotation(Rotation.COUNTERCLOCKWISE_90);
			}
			if(rotation==2){
				rotateState2=rotateState.withRotation(Rotation.CLOCKWISE_180);
			}
			if(rotateState2!=rotateState){
				worldIn.setBlockState(rotatepos, rotateState2,19);
				Block b = rotateState.getBlock();
				if(b==Blocks.DISPENSER || b==Blocks.DROPPER || b==Blocks.OBSERVER)
				{
					//worldIn.scheduleBlockUpdate(rotatepos, rotateState.getBlock(), 1, 1);
				}
				worldIn.playSound(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, (float) .5, 1, false);
			}
		}
    }

}
