package com.wildermods.autosplitter.config;

import com.wildermods.autosplitter.net.SplitterServer;
import com.wildermods.autosplitter.time.TimerSettings;
import com.wildermods.wilderforge.api.modLoadingV1.config.Config;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry.GUI.Localized;
import com.wildermods.wilderforge.api.modLoadingV1.config.ConfigEntry.Range;

@Config(modid = "autosplitter")
public class AutosplitterConfiguration {

	@ConfigEntry
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.server.host",
		tooltipLocalizer = "autosplitter.ui.configure.server.host.tooltip"
	)
	public String host = SplitterServer.DEFAULT_HOST;
	
	@ConfigEntry
	@Range(min = 0, max = 65535)
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.server.port",
		tooltipLocalizer = "autosplitter.ui.configure.server.port.tooltip"
	)
	public int port = SplitterServer.DEFAULT_PORT;
	
	@ConfigEntry
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.autosplit",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.autosplit.tooltip"
	)
	public boolean autosplit = true;
	
	@ConfigEntry
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.removeLoads",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.autosplit.removeLoads.tooltip"
	)
	public boolean removeLoads = true;
	
	@ConfigEntry
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.resetMainMenu",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.resetMainMenu.tooltip"
	)
	public boolean resetOnMainMenu = true;
	
	@ConfigEntry
	@Localized(
		nameLocalizer = "autosplitter.ui.configure.splitter.resetOnDefeat",
		tooltipLocalizer = "autosplitter.ui.configure.splitter.resetOnDefeat.tooltip"
	)
	public boolean resetOnDefeat = true;
	
	public TimerSettings deriveSettings() {
		return new TimerSettings(host, port, autosplit, removeLoads, resetOnMainMenu, resetOnDefeat);
	}
	
}
