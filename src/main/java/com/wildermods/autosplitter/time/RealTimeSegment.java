package com.wildermods.autosplitter.time;

import java.time.Duration;
import java.time.Instant;

import com.wildermods.autosplitter.exception.TimerException.TimerStarted;
import com.wildermods.autosplitter.exception.TimerException.TimerStopped;

public class RealTimeSegment extends TimeSegment {

	private Instant start;
	private Instant end;
	
	
	public RealTimeSegment() {
		
	}
	
	public RealTimeSegment(Instant start) {
		this.start = start;
	}
	
	public RealTimeSegment(Instant start, Instant end) {
		this(start);
		this.end = end;
	}
	
	public RealTimeSegment(Instant start, Duration duration) {
		this(start, start.plus(duration));
	}
	
	public RealTimeSegment(Timed derive) {
		super(derive);
	}
	
	public void start() {
		if(!hasStart()) {
			this.start = Instant.now();
		}
		else {
			throw new TimerStarted("Cannot Start - Segment already started!");
		}
	}
	
	public void stop() {
		if(!hasEnd()) {
			this.end = Instant.now();
		}
		else {
			throw new TimerStopped("Cannot Stop - Segment already stopped!");
		}
	}
	
	@Override
	public Instant getStart() {
		return start;
	}

	@Override
	public Instant getEnd() {
		return end;
	}

	@Override
	public boolean hasStart() {
		return start != null;
	}

	@Override
	public boolean hasEnd() {
		return end != null;
	}
	
	public void undo() {
		if(hasEnd()) {
			this.end = null;
		}
	}

}