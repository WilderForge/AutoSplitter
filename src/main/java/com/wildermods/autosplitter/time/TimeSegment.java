package com.wildermods.autosplitter.time;

import java.time.Duration;
import java.time.Instant;

public class TimeSegment implements Timed {

	private Instant start;
	private Instant end;
	
	public TimeSegment() {
		
	}
	
	public TimeSegment(Instant start) {
		this.start = start;
	}
	
	public TimeSegment(Instant start, Instant end) {
		this.start = start;
		this.end = end;
	}
	
	public TimeSegment(Instant start, Duration duration) {
		this(start, start.plus(duration));
	}
	
	public TimeSegment(Timed derive) {
		this.start = derive.getStart();
		this.end = derive.getEnd();
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
	
}
