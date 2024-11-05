package com.wildermods.autosplitter;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import com.wildermods.autosplitter.event.PopUpEvent.PopUpAddEvent;
import com.wildermods.autosplitter.event.PopUpEvent.PopUpRemoveEvent;
import com.wildermods.autosplitter.mixins.WaitingForGameDialogAccessor;
import com.wildermods.autosplitter.time.SplitTimer;
import com.wildermods.wilderforge.api.eventV1.bus.SubscribeEvent;
import com.wildermods.wilderforge.api.mechanicsV1.ChapterSetEvent;
import com.wildermods.wilderforge.api.mixins.v1.Cast;
import com.wildermods.wilderforge.api.modLoadingV1.Mod;
import com.wildermods.wilderforge.api.modLoadingV1.event.PreInitializationEvent;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.logging.Debug;
import com.wildermods.wilderforge.launch.logging.FullThreadInfo;
import com.wildermods.wilderforge.launch.logging.Logger;
import com.worldwalkergames.legacy.ui.menu.WaitingForGameDialog;
import com.worldwalkergames.ui.popup.IPopUp;

@Mod(modid = "AutoSplitter", version = "@AUTOSPLITTER_VERSION@")
public class Main {

	public static final Logger LOGGER = new Logger("Autosplitter");
	
	public static SplitTimer timer;
	private static Thread MAIN_THREAD;
	
	public static void main(String[] args) throws Exception {
		MAIN_THREAD = Thread.currentThread();
		onPreInitialization(new PreInitializationEvent());
	}
	
	public static Thread getMainThread() {
		return MAIN_THREAD;
	}
	
	@SubscribeEvent
	public static void onPreInitialization(PreInitializationEvent e) throws Exception {
		MAIN_THREAD = Thread.currentThread();
		WilderForge.MAIN_BUS.register(Main.class);
		timer = new SplitTimer();
		WilderForge.MAIN_BUS.register(timer);
	}
	
	@SubscribeEvent
	public static void onPopupAdd(PopUpAddEvent e) {
		if(e instanceof PopUpAddEvent.Pre) {
			IPopUp popup = e.getPopup();
			if(popup instanceof WaitingForGameDialog) {
				WaitingForGameDialogAccessor dialogAccessor = Cast.from(popup);
				switch(dialogAccessor.getContext()) {
					case newCampaign:
						timer.start(true);
						break;
				
					case campaignMission:
					case loadScenario:
					case loadCampaign:
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
	public static void onPopupRemove(PopUpRemoveEvent.Post e) {
		Main.LOGGER.fatal("POPUP_REMOVE_EVENT_FIRED");
		if(e instanceof PopUpRemoveEvent.Post) {
			IPopUp popup = e.getPopup();
			if(popup instanceof WaitingForGameDialog) {
				if(WilderForge.getViewDependencies().popUpManager.getPopupByClass(WaitingForGameDialog.class) != null) {
					Main.LOGGER.fatal("Unpausing skipped. There are additional WaitingForGameDialog popups in the popUpManager");
				}
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
	
	@SubscribeEvent
	public static void onChapterChange(ChapterSetEvent e) {
		Debug.trace("CHAPTER CHANGED FROM " + e.getPreviousChapter() + " to " + e.getNewChapter());
		if(e.getPreviousChapter() >= e.getNewChapter() || e.getPreviousChapter() == 0 && e.getNewChapter() == 1) {
			return;
		}
		timer.split();
	}
	
	public static String getThreadDump() {
		StringBuilder text = new StringBuilder();
		ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		text.append("---- THREAD DUMP ----\n\n");
		for (ThreadInfo dump : threads.dumpAllThreads(true, true, Integer.MAX_VALUE)) {
			String trimmedInfo = FullThreadInfo.from(dump).toString();
			text.append(trimmedInfo.toString());
		}
		return text.toString();
	}
	
}
