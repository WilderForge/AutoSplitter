package com.wildermods.autosplitter;

import java.io.IOException;

import com.wildermods.autosplitter.event.PopUpEvent.PopUpAddEvent;
import com.wildermods.autosplitter.event.PopUpEvent.PopUpRemoveEvent;
import com.wildermods.autosplitter.mixins.WaitingForGameDialogAccessor;
import com.wildermods.wilderforge.api.eventV1.bus.SubscribeEvent;
import com.wildermods.wilderforge.api.mixins.v1.Cast;
import com.wildermods.wilderforge.api.modLoadingV1.Mod;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.logging.Logger;
import com.worldwalkergames.legacy.ui.menu.WaitingForGameDialog;
import com.worldwalkergames.ui.popup.IPopUp;

@Mod(modid = "AutoSplitter", version = "@AUTOSPLITTER_VERSION@")
public class Main {

	public static final Logger LOGGER = new Logger("Autosplitter");
	
	private static SplitTimer timer;
	private static boolean resetOnMainMenu = true;
	
	static {
		WilderForge.MAIN_BUS.register(Main.class);
		timer = new SplitTimer();
		try {
			timer.connect();
		} catch (IOException e) {
			LOGGER.catching(e);
		}
	}
	
	@SubscribeEvent
	public void onPopupAdd(PopUpAddEvent e) {
		if(e instanceof PopUpAddEvent.Pre) {
			IPopUp popup = e.getPopup();
			if(popup instanceof WaitingForGameDialog) {
				WaitingForGameDialogAccessor dialogAccessor = Cast.from(popup);
				switch(dialogAccessor.getContext()) {
					case campaignMission:
					case loadCampaign:
					case loadScenario:
					case newCampaign:
					case scenario:
					case legacyBrowser: //If in the future there are categories that span multiple campaigns, opening the legacy would be a legal maneuver, even though it would slow you down.
						timer.pause();
						break;
						
					case changedMods: //shouln't be changing mods in the middle of a run...
						LOGGER.error("Mods are changing!");
						break;
						
					default:
						timer.pause();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPopupRemove(PopUpRemoveEvent e) {
		if(e instanceof PopUpRemoveEvent.Pre) {
			IPopUp popup = e.getPopup();
			if(popup instanceof WaitingForGameDialog) {
				WaitingForGameDialogAccessor dialogAccessor = Cast.from(popup);
				switch(dialogAccessor.getContext()) {
					case campaignMission:
					case loadCampaign:
					case loadScenario:
					case newCampaign:
					case scenario:
					case legacyBrowser: //If in the future there are categories that span multiple campaigns, opening the legacy would be a legal maneuver, even though it would slow you down.
						timer.unpause();
						break;
						
					case changedMods: //shouln't be changing mods in the middle of a run...
						LOGGER.error("Mods finished changing!");
						break;
						
					default:
						timer.unpause();
				}
			}
		}
	}
	
}
