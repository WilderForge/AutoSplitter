package com.wildermods.autosplitter.livesplit;

public enum Commands {
	start,
	split,
	splitOrStart,
	reset,
	undoSplit,
	skipSplit,
	togglePauseOrStart,
	pause,
	resume,
	undoAllPauses,
	switchToPreviousComparison,
	switchToNextComparision,
	setCurrentComparision,
	initializeGameTime,
	setGameTime,
	pauseGameTime,
	resumeGameTime,
	setLoadingTimes,
	setCustomVariable,
	getCurrentTime,
	getSegmentTime,
	getComparisionTime,
	getCurrentRunSplitTime,
	getCurrentState,
	ping;
	
	public Command get() {
		return new Command(this.name());
	}
}
