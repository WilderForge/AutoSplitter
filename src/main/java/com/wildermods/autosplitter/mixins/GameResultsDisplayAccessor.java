package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.worldwalkergames.legacy.ui.credits.GameResultsDisplay;

@Mixin(GameResultsDisplay.class)
public interface GameResultsDisplayAccessor {

	public @Accessor boolean isVictory();
	
}
