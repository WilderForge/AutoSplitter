package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.worldwalkergames.legacy.ui.menu.SaveLoadDialog;

@Mixin(SaveLoadDialog.class)
public interface SaveLoadDialogAccessor {

	public @Accessor("isWaitingForGame") boolean isWaitingForGame();
	
}
