package com.wildermods.autosplitter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class PausingTimer implements Timed {

	private RealTimer parent;
	
	protected ArrayList<Pause> pauses = new ArrayList<Pause>();
	
	public PausingTimer(RealTimer parent) {
		this.parent = parent;
	}
	
	@Override
	public Instant getStart() {
		return parent.getStart();
	}

	@Override
	public Instant getEnd() {
		return parent.getEnd();
	}

	@Override
	public boolean hasStart() {
		return parent.hasStart();
	}

	@Override
	public boolean hasEnd() {
		return parent.hasEnd();
	}
	
	public Pause getLastPause() {
		if(pauses.isEmpty()) {
			return null;
		}
		return pauses.get(pauses.size() - 1);
	}
	
	public void start() {
		if(parent.hasStart()) {
			throw new IllegalStateException("Cannot start: Timer already started");
		}
		parent.start();
	}
	
	public void pause() {
		if(getStart() == null) {
			throw new IllegalStateException("Cannot pause: Timer hasn't started");
		}
		if(getEnd() != null) {
			throw new IllegalStateException("Cannot pause: Timer is stopped");
		}	
		if(getLastPause() == null || !getLastPause().stillPaused()) {
			Pause pause = new Pause();
			pauses.add(pause);
		}
		else {
			throw new IllegalStateException("Cannot pause: Already paused");
		}
	}
	
	public void stop() {
		if(getStart() == null) {
			throw new IllegalStateException("Cannot stop: Timer hasn't started");
		}
		if(getEnd() != null) {
			throw new IllegalStateException("Cannot stop: Timer already stopped");
		}
		if(getLastPause() != null && getLastPause().stillPaused()) {
			System.out.println("Stopping timer - Unpausing subtimer.");
			getLastPause().unpause();
		}
		parent.stop();
		System.out.println("Timer Stopped!");
	}
	
	public boolean isPaused() {
		return hasStart() && getLastPause() != null && !getLastPause().hasEnd();
	}
	
	public void unpause() {
		if(!hasStart()) {
			throw new IllegalStateException("Cannot unpause: Timer not started");
		}
		if(hasEnd()) {
			throw new IllegalStateException("Cannot unpause: Timer stopped");
		}
		if(!isPaused()) {
			throw new IllegalStateException("Cannot unpause: Timer not paused");
		}
		getLastPause().stop();
	}
	
	public void undo() {
		RealTimer lastTimer = getLastPause();
		if(lastTimer == null) {
			throw new IllegalStateException();
		}
		if(lastTimer.hasEnd()) {
			lastTimer.undo();
		}
		else {
			pauses.remove(lastTimer);
		}
	}
	
	@Override
	public Duration getDuration() {
		if(hasStart()) {
			Duration duration = Timed.super.getDuration();
			//System.out.println("------");
			for(Pause pause: pauses) {

				
				if(pause.stillPaused()) {
					//System.out.println(pause.getDuration() + " (still paused)");
					duration = duration.minus(pause.getDuration());
				}
				else {
					duration = duration.minus(pause.getDuration());
					//System.out.println(pause.getDuration() + " (pause complete)");
				}
			}
			return duration;
		}
		return Duration.ZERO;
	}

	@Override
	public String getFormattedText() {
		return TimeUtils.toReadableTimerDuration(getDuration(), parent.getDuration().toHours() > 0, true);
	}
	
}
