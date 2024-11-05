package com.wildermods.autosplitter.exception;

public class TimerException extends IllegalStateException {

	public TimerException(String message) {
		super(message);
	}
	
	public TimerException(Throwable cause) {
		super(cause);
	}
	
	public TimerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Thrown to indicate that the timer is expected to be
	 * started, but it was not started.
	 */
	public static class TimerNotStarted extends TimerException {
		
		public TimerNotStarted(String message) {
			super(message);
		}
		
	}
	
	/*
	 * Thrown to indicate that the timer is expected to not
	 * be started, but it was in the started state
	 */
	public static class TimerStarted extends TimerException {
		
		public TimerStarted(String message) {
			super(message);
		}
		
	}
	
	/**
	 * Thrown to indicate that the timer is not expected to
	 * be stopped, but the timer was in the stopped state.
	 */
	public static class TimerStopped extends TimerException {
		
		public TimerStopped(String message) {
			super(message);
		}
		
	}
	
	/**
	 * Thrown to indicate that the timer is not expected
	 * to be paused, but the timer was in the paused state.
	 */
	public static class TimerPaused extends TimerException {
		
		public TimerPaused(String message) {
			super(message);
		}
		
	}
	
	/**
	 * Thrown to indicate that the timer is not expected to
	 * be paused, but the timer was not in a paused state.
	 */
	public static class TimerNotPaused extends TimerException {
		public TimerNotPaused(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown to indicate that the timer is expected to have a
	 * pause, but no pauses were found.
	 */
	public static class NoPauses extends TimerException {
		public NoPauses(String message) {
			super(message);
		}
	}
	
	
	
}
