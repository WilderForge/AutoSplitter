package com.wildermods.autosplitter.config;

import java.util.function.Function;

import com.wildermods.autosplitter.Main;
import com.wildermods.autosplitter.net.SplitterServer;
import com.wildermods.autosplitter.time.TimerSettings;
import com.wildermods.wilderforge.api.modLoadingV1.config.Config;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry.GUI;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry.Range;
import com.wildermods.wilderforge.api.modLoadingV1.config.ModConfigurationEntryBuilder.ConfigurationUIContext;

@Config(modid = Main.MOD_ID)
public class AutosplitterConfiguration {
	public static transient final int EARLIEST_COMPATIBLE_SCHEMA = 0;
	public static transient final int CURRENT_SCHEMA = 0;
	
	@ConfigEntry
	@GUI.Excluded
	@Range(min = EARLIEST_COMPATIBLE_SCHEMA, max = CURRENT_SCHEMA) //we want to throw config exception if a newer schema version is loaded or incompatible schema is laoded
	public int schema = CURRENT_SCHEMA;
	
	@ConfigEntry
	@GUI.CustomBuilder(value = RuntimeSettingsBuilder.class)
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.autosplit",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.autosplit.tooltip"
	)
	public boolean autosplit = true;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.removeLoads",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.removeLoads.tooltip"
	)
	public boolean removeLoads = true;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.splitOnChapterComplete",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.splitOnChapterComplete.tooltip"
	)
	public boolean splitOnChapterComplete = true;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.splitOnVictoryPopup",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.splitOnVictoryPopup.tooltip"
	)
	public boolean splitOnVictoryPopup = true;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.resetMainMenu",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.resetMainMenu.tooltip"
	)
	public boolean resetOnMainMenu = true;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.resetOnDefeat",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.resetOnDefeat.tooltip"
	)
	public boolean resetOnDefeat = false;
	
	@ConfigEntry
	@GUI.CustomBuilder(value = RuntimeSettingsBuilder.class)
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.server.host",
		tooltipLocalizer = "autosplitter.ui.configure.server.host.tooltip"
	)
	public String host = SplitterServer.DEFAULT_HOST;
	
	@ConfigEntry
	@Range(min = 0, max = 65535)
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.server.port",
		tooltipLocalizer = "autosplitter.ui.configure.server.port.tooltip"
	)
	public int port = SplitterServer.DEFAULT_PORT;
	
	@ConfigEntry
	@GUI.Localized(
		nameLocalizer = "autosplitter.ui.configure.server.autoStart"
	)
	public boolean startServletOnStartup = true;
	
	public TimerSettings deriveSettings() {
		return new TimerSettings(host, port, autosplit, removeLoads, splitOnChapterComplete, splitOnVictoryPopup, resetOnMainMenu, resetOnDefeat);
	}
	
	private static class RuntimeSettingsBuilder implements Function<ConfigurationUIContext, AutosplitterModConfigurationEntryBuilder>{

		@Override
		public AutosplitterModConfigurationEntryBuilder apply(ConfigurationUIContext t) {
			return new AutosplitterModConfigurationEntryBuilder(t);
		}
		
	}
	
}
