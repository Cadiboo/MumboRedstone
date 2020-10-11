package com.supercat765.mumboredstone.proxy;

import com.supercat765.mumboredstone.ModColorHandler;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
/*    */ {
/*    */   public void registerRenderers()
/*    */   {
/* 17 */     
/*    */   }
/*    */   
/*    */   public void registerColorHandlers()
/*    */   {
/* 22 */     ModColorHandler.registerColorHandlers();
/*    */   }
/*    */ }