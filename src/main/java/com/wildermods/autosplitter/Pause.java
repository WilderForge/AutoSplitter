package com.wildermods.autosplitter;

public class Pause extends RealTimer {

	public Pause() {
		this(System.currentTimeMillis());
	}
	
	public Pause(long start) {
		super(start);
	}
	
	public Pause(long start, long end) {
		super(start, end);
	}
	
	public void unpause() {
		stop();
	}
	
	public boolean stillPaused() {
		return !this.hasEnd();
	}
	
}