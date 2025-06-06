package com.wildermods.autosplitter;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import com.wildermods.autosplitter.config.AutosplitterConfiguration;
import com.wildermods.wilderforge.api.uiV1.PopUpEvent.PopUpAddEvent;
import com.wildermods.wilderforge.api.uiV1.PopUpEvent.PopUpRemoveEvent;
import com.wildermods.autosplitter.mixins.GameResultsDisplayAccessor;
import com.wildermods.autosplitter.mixins.SaveLoadDialogAccessor;
import com.wildermods.autosplitter.mixins.WaitingForGameDialogAccessor;
import com.wildermods.autosplitter.time.SplitTimer;
import com.wildermods.wilderforge.api.mechanicsV1.ChapterSetEvent;
import com.wildermods.wilderforge.api.mixins.v1.Cast;
import com.wildermods.wilderforge.api.modLoadingV1.Mod;
import com.wildermods.wilderforge.api.modLoadingV1.event.PostInitializationEvent;
import com.wildermods.wilderforge.api.modLoadingV1.event.PreInitializationEvent;
import com.wildermods.wilderforge.api.uiV1.TopLevelScreenChangeEvent;
import com.wildermods.wilderforge.launch.WilderForge;
import com.wildermods.wilderforge.launch.coremods.Configuration;
import com.wildermods.wilderforge.launch.coremods.Coremods;
import com.wildermods.wilderforge.launch.logging.Debug;
import com.wildermods.wilderforge.launch.logging.FullThreadInfo;
import com.wildermods.provider.util.logging.Logger;
import com.worldwalkergames.legacy.ui.credits.GameResultsScreen;
import com.worldwalkergames.legacy.ui.menu.RootMenuScreen;
import com.worldwalkergames.legacy.ui.menu.SaveLoadDialog;
import com.worldwalkergames.legacy.ui.menu.WaitingForGameDialog;
import com.worldwalkergames.ui.popup.IPopUp;

import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod(modid = Main.MOD_ID, version = "@AUTOSPLITTER_VERSION@")
public class Main {

	public static final String MOD_ID = "autosplitter";
	
	public static final Logger LOGGER = new Logger("Autosplitter");
	
	public static SplitTimer timer;
	private static Thread MAIN_THREAD;
	
	public static AutosplitterConfiguration getDefaultConfig() {
		return Cast.from(Configuration.getDefaultConfig(Coremods.getCoremod(MOD_ID)));
	}
	
	public static AutosplitterConfiguration getConfig() {
		return Cast.from(Configuration.getConfig(Coremods.getCoremod(MOD_ID)));
	}
	
	public static Thread getMainThread() {
		return MAIN_THREAD;
	}
	
	@SubscribeEvent
	public static void onPreInitialization(PreInitializationEvent e) throws Exception {
		MAIN_THREAD = Thread.currentThread();
	}
	
	@SubscribeEvent
	public static void onPostInitialization(PostInitializationEvent e) throws Exception {
		if(getConfig().startServletOnStartup) {
			timer = new SplitTimer(getConfig().deriveSettings());
			WilderForge.MAIN_BUS.register(timer);
		}
	}
	
	@SubscribeEvent
	public static void onPopupAdd(PopUpAddEvent e) {
		if(e instanceof PopUpAddEvent.Pre) {
			IPopUp popup = e.getPopup();
			if(popup instanceof WaitingForGameDialog) {
				WaitingForGameDialogAccessor dialogAccessor = Cast.from(popup);
				switch(dialogAccessor.getContext()) {
					case newCampaign:
						if(getConfig().autosplit) {
							timer.start(true);
						}
						break;
				
					case campaignMission:
					case loadScenario:
					case loadCampaign:
					case scenario:
					case legacyBrowser: //If in the future there are categories that span multiple campaigns, opening the legacy would be a legal maneuver, even though it would slow you down.
						if(getConfig().removeLoads) {
							timer.pause();
						}
						break;
						
					case changedMods: //shouln't be changing mods in the middle of a run...
						LOGGER.error("Mods are changing!");
						break;
						
					default:
						if(getConfig().removeLoads) {
							timer.pause();
						}
						break;
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
						if(getConfig().removeLoads) {
							timer.unpause();
						}
						break;
						
					case changedMods: //shouln't be changing mods in the middle of a run...
						LOGGER.error("Mods finished changing!");
						break;
						
					default:
						if(getConfig().removeLoads) {
							timer.unpause();
						}
						break;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onChapterChange(ChapterSetEvent e) {
		Debug.trace("CHAPTER CHANGED FROM " + e.getPreviousChapter() + " to " + e.getNewChapter());
		if(e.getPreviousChapter() >= e.getNewChapter() || e.getPreviousChapter() == 0 && e.getNewChapter() == 1) {
			//sometimes the chapter changes back to itself
			return;
		}
		if(getConfig().autosplit && getConfig().splitOnChapterComplete) {
			if(e.getNewChapter() <= e.getGameSettings().lastChapter.num) {
				timer.split();
			}
		}
	}
	
	@SubscribeEvent
	public static void onScreenChange(TopLevelScreenChangeEvent.Post e) {
		if(e.getNewScreen() instanceof GameResultsScreen) {
			if(getConfig().autosplit) {
				GameResultsScreen screen = Cast.from(e.getNewScreen());
				GameResultsDisplayAccessor display = Cast.from(screen.creditsDisplay);
				
				if(display.isVictory()) {
					if(getConfig().splitOnVictoryPopup) {
						timer.split();
					}
				}
				else {
					if(getConfig().resetOnDefeat) {
						timer.reset();
					}
				}
			}
		}
		
		if(e.getNewScreen() instanceof RootMenuScreen) {
			SaveLoadDialogAccessor saveLoadDialog = Cast.from(WilderForge.getViewDependencies().popUpManager.getPopupByClass(SaveLoadDialog.class));
			boolean waitingForGame = (saveLoadDialog != null && saveLoadDialog.isWaitingForGame());
			
			LOGGER.info("SaveLoadDialog: " + saveLoadDialog, "RootMenuCheck");
			LOGGER.info("waitingForGame: " + waitingForGame, "RootMenuCheck");
			
			/*
			 * Fixes #5 (https://github.com/WilderForge/AutoSplitter/issues/5)
			 * 
			 * If a new game is being loaded in the middle of a run, RootMenuScreen is briefly opened.
			 * 
			 * We can't reset the timer when this happens. A timer that has been reset is not started.
			 * Attempting to do so will throw TimerNotStarted.
			 * 
			 * When the loading dialog goes away, autosplitter will attempt to unpause the timer. but we
			 * cannot unpause a timer that hasn't started. So we have to check if there is one and if it's
			 * waiting for the game to load. If there is a new game being loaded, we can't reset.
			 */
			if(!waitingForGame && getConfig().resetOnMainMenu) {
				timer.reset();
			}
		}
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
