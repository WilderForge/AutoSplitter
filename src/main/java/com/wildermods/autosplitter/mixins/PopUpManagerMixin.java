package com.wildermods.autosplitter.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.wildermods.autosplitter.event.PopUpEvent.PopUpAddEvent;
import com.wildermods.autosplitter.event.PopUpEvent.PopUpRemoveEvent;
import com.wildermods.wilderforge.launch.WilderForge;
import com.worldwalkergames.ui.popup.IPopUp;
import com.worldwalkergames.ui.popup.PopUpManager;

@Mixin(PopUpManager.class)
public class PopUpManagerMixin {

	@Inject(
		method = "pushFront",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	public void onPushFront(IPopUp popup, boolean skipFadeIn, CallbackInfo c) {
		if(WilderForge.MAIN_BUS.fire(new PopUpAddEvent.PushFrontEvent.Pre(popup, skipFadeIn))) {
			c.cancel();
		}
	}
	
	@Inject(
		method = "pushBack",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	public void onPushBack(IPopUp popup, CallbackInfo c) {
		if(WilderForge.MAIN_BUS.fire(new PopUpAddEvent.PushBackEvent.Pre(popup))) {
			c.cancel();
		}
	}
	
	@Inject(
		method = "removePopUp",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	public void onRemovePopup(IPopUp popup, CallbackInfo c) {
		if(WilderForge.MAIN_BUS.fire(new PopUpRemoveEvent.Pre(popup))) {
			c.cancel();
		}
	}
	
}
