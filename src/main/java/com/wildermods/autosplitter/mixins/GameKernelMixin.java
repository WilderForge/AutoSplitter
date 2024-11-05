package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.wildermods.autosplitter.Main;

@Mixin(targets = "com.worldwalkergames.legacy.game.mechanics.GameKernel")
public class GameKernelMixin {
	
	@Inject(
		method = "winCampaign",
		at = @At("HEAD")
	)
	public void onVictory(long chapterCompletionTime, CallbackInfo c) {
		Main.timer.split();
	}
	
	@Inject(
		method = "loseCampaign",
		at = @At("HEAD")
	)
	public void onDefeat(CallbackInfo c) {
		Main.timer.stop();
	}
	
}
