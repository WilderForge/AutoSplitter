package com.wildermods.autosplitter.time;

import java.time.Duration;
import java.time.Instant;

public class Pause extends RealTimeSegment implements Pausable {

	public Pause() {
		this(Instant.now());
	}
	
	public Pause(Instant start) {
		super(start);
	}
	
	public Pause(Instant start, Instant end) {
		super(start, end);
	}
	
	public Pause(Instant start, Duration duration) {
		super(start, start.plus(duration));
	}
	
	@Override
	public void pause() {
		start();
	}
	
	public void unpause() {
		stop();
	}
	
	public boolean isPaused() {
		return !this.hasEnd();
	}
	
}