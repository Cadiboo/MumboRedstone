package com.supercat765.mumboredstone;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockRegister {

	private Block block;
	private boolean needsItem;
	private Item item;
	
	public BlockRegister(Block bl, boolean b) {
		block=bl;
		needsItem=b;
	}
	
	public BlockRegister(Block bl, Item itemin) {
		block=bl;
		needsItem=true;
		item=itemin;
	}
	
	public boolean hascustomItem(){
		return item!=null;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public boolean isNeedsItem() {
		return needsItem;
	}

	public void setNeedsItem(boolean needsItem) {
		this.needsItem = needsItem;
	}

}
