package com.wildermods.autosplitter;

import java.time.Instant;

public class RealTimer implements Timed {

	private Instant start;
	private Instant end;
	
	
	public RealTimer() {
		
	}
	
	public RealTimer(long start) {
		this.start = Instant.ofEpochMilli(start);
	}
	
	public RealTimer(long start, long end) {
		this(start);
		if(end > 0) {
			this.end = Instant.ofEpochMilli(end);
		}
	}
	
	public void start() {
		if(!hasStart()) {
			this.start = Instant.now();
		}
	}
	
	public void stop() {
		if(!hasEnd()) {
			this.end = Instant.now();
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