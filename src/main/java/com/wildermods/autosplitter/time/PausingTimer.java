package com.wildermods.autosplitter.time;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.wildermods.autosplitter.exception.TimerException.TimerNotStarted;
import com.wildermods.autosplitter.exception.TimerException.TimerPaused;
import com.wildermods.autosplitter.exception.TimerException.TimerStarted;
import com.wildermods.autosplitter.exception.TimerException.TimerStopped;
import com.wildermods.autosplitter.exception.TimerException.TimerNotPaused;

public class PausingTimer implements Timed {

	private RealTimeSegment parent;
	
	protected ArrayList<Pause> pauses = new ArrayList<Pause>();
	
	public PausingTimer(RealTimeSegment parent) {
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
	
	public void start() throws TimerStarted {
		if(parent.hasStart()) {
			throw new TimerStarted("Cannot start: Timer already started");
		}
		parent.start();
	}
	
	public void pause() {
		if(getStart() == null) {
			throw new TimerNotStarted("Cannot pause: Timer hasn't started");
		}
		if(getEnd() != null) {
			throw new TimerStopped("Cannot pause: Timer is stopped");
		}	
		if(getLastPause() == null || !getLastPause().isPaused()) {
			Pause pause = new Pause();
			pauses.add(pause);
		}
		else {
			throw new TimerPaused("Cannot pause: Already paused");
		}
	}
	
	public void stop() {
		if(getStart() == null) {
			throw new TimerNotStarted("Cannot stop: Timer hasn't started");
		}
		if(getEnd() != null) {
			throw new TimerStopped("Cannot stop: Timer already stopped");
		}
		if(getLastPause() != null && getLastPause().isPaused()) {
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
			throw new TimerNotStarted("Cannot unpause: Timer not started");
		}
		if(hasEnd()) {
			throw new TimerStopped("Cannot unpause: Timer stopped");
		}
		if(!isPaused()) {
			throw new TimerNotPaused("Cannot unpause: Timer not paused");
		}
		getLastPause().stop();
	}
	
	public void undoPause() {
		RealTimeSegment lastTimer = getLastPause();
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

				
				if(pause.isPaused()) {
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
	public String getFormattedDuration() {
		return TimeUtils.toReadableTimerDuration(getDuration(), parent.getDuration().toHours() > 0, true);
	}
	
}
