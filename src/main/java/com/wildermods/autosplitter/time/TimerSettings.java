package com.wildermods.autosplitter.time;

import com.wildermods.autosplitter.net.SplitterServer;

public record TimerSettings(String host, int port, boolean resetOnMainMenu, boolean resetOnDefeat) {
	
	public static TimerSettings defaultSettings() {
		return new TimerSettings(SplitterServer.DEFAULT_HOST, SplitterServer.DEFAULT_PORT, true, true);
	}
	
}
