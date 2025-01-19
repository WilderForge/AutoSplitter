package com.wildermods.autosplitter.time;

import com.wildermods.autosplitter.config.AutosplitterConfiguration;
import com.wildermods.wilderforge.launch.coremods.Configuration;
import com.wildermods.wilderforge.launch.coremods.Coremods;

public record TimerSettings(String host, int port, boolean autosplit, boolean removeLoads, boolean resetOnMainMenu, boolean resetOnDefeat) {
	
	public static TimerSettings defaultSettings() {
		AutosplitterConfiguration defaultConfig = (AutosplitterConfiguration) Configuration.getDefaultConfig(Coremods.getCoremod("autosplitter"));
		return defaultConfig.deriveSettings();
	}
	
}
