package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.worldwalkergames.legacy.ui.menu.WaitingForGameDialog;

@Mixin(WaitingForGameDialog.class)
public interface WaitingForGameDialogAccessor {

	@Accessor
	public WaitingForGameDialog.Context getContext();
	
}
