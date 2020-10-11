package com.supercat765.mumboredstone.items;

import com.supercat765.mumboredstone.MumboRedMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class FakeItem extends Item{

	public FakeItem()
    {
		setRegistryName(new ResourceLocation(MumboRedMod.MODID,"fake"));
		setUnlocalizedName("fake");
    }

	@Override
	public boolean getHasSubtypes() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==CreativeTabs.MISC){
			ItemStack item = new ItemStack(Items.KNOWLEDGE_BOOK);
			item.setStackDisplayName("Mumbo's Redstone Addition Recipies");
			NBTTagCompound nbt = item.getTagCompound();
			if(nbt==null)nbt=new NBTTagCompound();
			NBTTagList nbt2 = new NBTTagList();
			
			nbt2.appendTag(new NBTTagString("mumboredstone:bluestone"));
			nbt2.appendTag(new NBTTagString("mumboredstone:breaker"));
			nbt2.appendTag(new NBTTagString("mumboredstone:capacitor"));
			nbt2.appendTag(new NBTTagString("mumboredstone:detector"));
			nbt2.appendTag(new NBTTagString("mumboredstone:extendrepeater"));
			nbt2.appendTag(new NBTTagString("mumboredstone:placer"));
			nbt2.appendTag(new NBTTagString("mumboredstone:powerpiston-sticky"));
			nbt2.appendTag(new NBTTagString("mumboredstone:powerpiston"));
			nbt2.appendTag(new NBTTagString("mumboredstone:resister"));
			nbt2.appendTag(new NBTTagString("mumboredstone:rotator180"));
			nbt2.appendTag(new NBTTagString("mumboredstone:rotatorccw"));
			nbt2.appendTag(new NBTTagString("mumboredstone:rotatorcw"));
			nbt2.appendTag(new NBTTagString("mumboredstone:rotatorswap1"));
			nbt2.appendTag(new NBTTagString("mumboredstone:rotatorswap2"));
			nbt2.appendTag(new NBTTagString("mumboredstone:wireless_reciever"));
			nbt2.appendTag(new NBTTagString("mumboredstone:wireless_lever"));
			nbt2.appendTag(new NBTTagString("mumboredstone:wireless_button"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			//nbt2.appendTag(new NBTTagString("mumboredstone:"));
			nbt.setTag("Recipes", nbt2);
			item.setTagCompound(nbt);
			items.add(item);
			super.getSubItems(tab, items);
		}
	}
	
	
}
