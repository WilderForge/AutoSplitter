package com.wildermods.autosplitter.time;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents an object with a measurable duration, defined by a start and end time.
 * This interface provides methods to access and format the duration, taking into
 * account possible pauses or other adjustments that might affect the elapsed time.
 */
public interface Timed {

	/**
	 * Retrieves the starting time of this Timed object.
	 *
	 * @return the {@link Instant} marking the start time, or {@code null} if the
	 * start time has not been set or the object has not started.
	 */
	public Instant getStart();

	/**
	 * Retrieves the ending time of this Timed object.
	 *
	 * @return the {@link Instant} marking the end time, or {@code null} if the
	 * end time has not been set or the object has not ended.
	 */
	public Instant getEnd();

	/**
	 * Checks if this Timed object has a defined start time.
	 *
	 * @return {@code true} if the start time is set, {@code false} otherwise.
	 */
	public boolean hasStart();

	/**
	 * Checks if this Timed object has a defined end time.
	 *
	 * @return {@code true} if the end time is set, {@code false} otherwise.
	 */
	public boolean hasEnd();

	/**
	 * Provides a formatted string representing the duration of this Timed object.
	 * The duration accounts for any adjustments, such as pauses, between the start
	 * and end times.
	 *
	 * @return a human-readable string representing the adjusted duration.
	 */
	public default String getFormattedDuration() {
		return TimeUtils.toReadableTimerDuration(getDuration(), false, true);
	}

	/**
	 * Provides a formatted string representing the total duration of this Timed object.
	 * The total duration disregards any adjustments, such as pauses, and only considers
	 * the time elapsed between the start and end times.
	 *
	 * @return a human-readable string representing the total unadjusted duration.
	 */
	public default String getFormattedTotalDuration() {
		return TimeUtils.toReadableTimerDuration(getTotalDuration(), false, true);
	}

	/**
	 * Calculates the adjusted duration of this Timed object, taking into account
	 * any pauses or other factors that may affect the elapsed time.
	 *
	 * <p>If the object has not started, the method returns {@link Duration#ZERO}.
	 * If the object has started but not ended, the duration is calculated from the
	 * start time to the current moment ({@link Instant#now()}).</p>
	 *
	 * @return the adjusted duration as a {@link Duration}.
	 */
	public default Duration getDuration() {
		return getTotalDuration();
	}

	/**
	 * Calculates the total unadjusted duration of this Timed object, ignoring any
	 * factors like pauses that might affect the elapsed time.
	 *
	 * <p>If the object has not started, the method returns {@link Duration#ZERO}.
	 * If the object has started but not ended, the duration is calculated from the
	 * start time to the current moment ({@link Instant#now()}).
	 *
	 * @return the total unadjusted duration as a {@link Duration}.
	 */
	public default Duration getTotalDuration() {
		if (hasStart()) {
			if (hasEnd()) {
				return Duration.between(getStart(), getEnd());
			}
			return TimeUtils.since(getStart());
		}
		return Duration.ZERO;
	}
}