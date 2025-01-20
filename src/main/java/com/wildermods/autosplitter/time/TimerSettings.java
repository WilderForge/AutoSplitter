package com.wildermods.autosplitter.time;

public record TimerSettings(String host, int port, boolean autosplit, boolean removeLoads, boolean splitOnChapterComplete, boolean splitOnVictoryPopup, boolean splitOnFinalChapterComplete, boolean resetOnMainMenu, boolean resetOnDefeat) {

}
