package com.wildermods.autosplitter;

import java.time.Duration;
import java.time.Instant;

public interface Timed {

	public Instant getStart();
	
	public Instant getEnd();
	
	public boolean hasStart();
	
	public boolean hasEnd();
	
	public default String getFormattedText() {
		return TimeUtils.toReadableTimerDuration(getDuration(), false, true);
	}
	
	public default Duration getDuration() {
		if(hasStart()) {
			if(hasEnd()) {
				return Duration.between(getStart(), getEnd());
			}
			return TimeUtils.since(getStart());
		}
		return Duration.ZERO;
	}
	
}
