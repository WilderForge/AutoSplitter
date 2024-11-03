package com.wildermods.autosplitter;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import static com.wildermods.autosplitter.TimeUtils.TimeUnit.*;

public final class TimeUtils {
	public static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US).withZone(ZoneId.of("UTC"));
	public static final SimpleDateFormat RIICONNECT24_FORMATTER = new SimpleDateFormat("dd MMM YYYY HH:mm:ss XX");
	public static final Duration FOREVER = ChronoUnit.FOREVER.getDuration();
	public static final Instant PLAYER_EPOCH = Instant.parse("2020-11-01T07:00:00Z"); //The second 2:00 am EST that occurs due do daylight savings
	public static String readableDuration(Duration duration, boolean... units) {
		
		if(units.length == 0) {
			units = new boolean[] {true, true, true, true, true, true, true, true};
		}
		else if (units.length != 8) {
			units = Arrays.copyOf(units, 8);
		}
		if(units.length != 8) {
			throw new IllegalArgumentException(Arrays.toString(units));
		}
		
		int j = -1;
		for(int i = 0; i < units.length; i++) {
			if(units[i]) {
				j = i;
			}
			if(i == units.length && j == -1) {
				throw new IllegalArgumentException("Units must contain at least one true value");
			}
		}
		
		String smallestUnit;
		switch(j) {
			case 0:
				smallestUnit = "years";
				break;
			case 1:
				smallestUnit = "months";
				break;
			case 2:
				smallestUnit = "weeks";
				break;
			case 3:
				smallestUnit = "days";
				break;
			case 4:
				smallestUnit = "hours";
				break;
			case 5:
				smallestUnit = "minutes";
				break;
			case 6:
				smallestUnit = "seconds";
				break;
			case 7:
				smallestUnit = "milliseconds";
				break;
			default:
				throw new AssertionError();
		}
		
		String time = " ";
		int k = 0;
		
		if(duration.equals(ChronoUnit.FOREVER.getDuration())) {
			return "forever";
		}
		
		if(duration.isNegative()) {
			time = "-";
			duration = duration.abs();
		}
		if(units[k++] && duration.toDays() / 365 > 0) {
			time += duration.toDays() / 365 + " years#";
			duration = duration.minus(Duration.ofDays((duration.toDays() / 365) * 365));
		}
		if(units[k++] && duration.toDays() / 30 > 0) {
			time += duration.toDays() / 30 + " months#";
			duration = duration.minus(Duration.ofDays((duration.toDays() / 30) * 30));
		}
		if (units[k++] && duration.toDays() / 7 > 0) {
			time += duration.toDays() / 7 + " weeks#";
			duration = duration.minus(Duration.ofDays((duration.toDays() / 7) * 7));
		}
		if(units[k++] && duration.toDays() > 0) {
			time += duration.toDays() + " days#";
			duration = duration.minus(Duration.ofDays(duration.toDays()));
		}
		if(units[k++] && duration.toHours() > 0) {
			time += duration.toHours() + " hours#";
			duration = duration.minus(Duration.ofHours(duration.toHours()));
		}
		if(units[k++] && duration.toMinutes() > 0) {
			time += duration.toMinutes() + " minutes#";
			duration = duration.minus(Duration.ofMinutes(duration.toMinutes()));
		}
		if(units[k++] && duration.getSeconds() > 0) {
			time += duration.getSeconds() + " seconds#";
			duration = duration.minus(Duration.ofSeconds(duration.getSeconds()));
		}
		if(units[k++] && duration.toMillisPart() > 0) {
			time += duration.toMillisPart() + " milliseconds";
			duration = duration.minus(Duration.ofSeconds(duration.getSeconds()));
		}
		
		time = time.replaceAll("#", " ");
		time = time.replaceAll(" 1 years", " 1 year");
		time = time.replaceAll(" 1 months", " 1 month");
		time = time.replaceAll(" 1 weeks", " 1 week");
		time = time.replaceAll(" 1 days", " 1 day");
		time = time.replaceAll(" 1 hours", " 1 hour");
		time = time.replaceAll(" 1 minutes", " 1 minute");
		time = time.replaceAll(" 1 seconds", " 1 second");
		time = time.replaceAll(" 1 milliseconds", " 1 millisecond");
		time = time.trim();
		if(time.isEmpty()) {
			return "0 " + smallestUnit;
		}
		return time;
	}
	
	public static String toReadableTimerDuration(Duration duration, boolean forceHours, boolean forceMilli) {
		String ret = "";
		if(duration.toHours() > 0 || forceHours) {
			ret = duration.toHours() + ":";
		}
		
		if(duration.toMinutesPart() < 10) {
			ret = ret + "0";
		}
		ret = ret + duration.toMinutesPart();
		ret = ret + ":";
		
		if(duration.toSecondsPart() < 10) {
			ret = ret + "0";
		}
		ret = ret + duration.toSecondsPart();

		
		if(forceMilli || (duration.toHours() == 0 && !forceHours)) {
			ret = ret + ":";
			if(duration.toMillisPart() < 100) {
				if(duration.toMillisPart() < 10) {
					ret = ret + "0";
				}
				ret = ret + "0";
			}
			ret = ret + duration.toMillisPart();
		}

		return ret;
	}
	
	public static String fullReadableDuration(Duration duration) {
		return readableDuration(duration, true, true, true, true, true, true, true, true);
	}
	
	public static Instant fromNow(Duration duration) {
		try {
			return Instant.now().plus(duration);
		}
		catch(ArithmeticException e) {
			return Instant.MAX;
		}
	}
	
	public static Duration since(Instant instant) {
		return Duration.between(instant, Instant.now());
	}
	
	@Deprecated
	public static Duration computeDuration(int amount, String timeUnit) {
		Duration duration = null;
		if(MILLISECONDS.matches(timeUnit)) {
			duration = Duration.ofMillis(amount);
		}
		else if(SECONDS.matches(timeUnit)) {
			duration = Duration.ofSeconds(amount);
		}
		else if(MINUTES.matches(timeUnit)) {
			duration = Duration.ofMinutes(amount);
		}
		else if (HOURS.matches(timeUnit)) {
			duration = Duration.ofHours(amount);
		}
		else if (DAYS.matches(timeUnit)) {
			duration = Duration.ofDays(amount);
		}
		else if (WEEKS.matches(timeUnit)) {
			duration = Duration.ofDays(amount * 7);
		}
		else if (MONTHS.matches(timeUnit)) {
			duration = Duration.ofDays(amount * 30);
		}
		else if (YEARS.matches(timeUnit)) {
			duration = Duration.ofDays(amount * 365);
		}
		return duration;
	}
	
	@Deprecated
	public static Duration computeDuration(int amount, TimeUnit timeUnit) {
		Duration duration = null;
		if(timeUnit == MILLISECONDS) {
			duration = Duration.ofMillis(amount);
		}
		else if(timeUnit == SECONDS) {
			duration = Duration.ofSeconds(amount);
		}
		else if(timeUnit == MINUTES) {
			duration = Duration.ofMinutes(amount);
		}
		else if (timeUnit == HOURS) {
			duration = Duration.ofHours(amount);
		}
		else if (timeUnit == DAYS) {
			duration = Duration.ofDays(amount);
		}
		else if (timeUnit == WEEKS) {
			duration = Duration.ofDays(amount * 7);
		}
		else if (timeUnit == MONTHS) {
			duration = Duration.ofDays(amount * 30);
		}
		else if (timeUnit == YEARS) {
			duration = Duration.ofDays(amount * 365);
		}
		return duration;
	}
	
	public static Instant parseInstant(String instant) {
		if(instant == null) {
			return Instant.MIN;
		}
		return Instant.parse(instant);
	}
	
	public static String parsePlayerInstant(Instant instant) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
		String ret = date.format(instant.toEpochMilli());
		if(instant.compareTo(PLAYER_EPOCH) <= 0) {
			ret = "Before " + ret;
		}
		return ret;
	}
	
	public static String getDBDate(TemporalAccessor temporal) {
		return DB_DATE_FORMATTER.format(temporal);
	}
	
	public static String getRC24Date(Date date) {
		return RIICONNECT24_FORMATTER.format(date);
	}
	
	public static Month getMonth(LocalDate date) {
		return date.getMonth();
	}
	
	public static enum TimeUnit {
		MILLISECONDS("ms", "millisecond", "milliseconds"),
		SECONDS("s", "sec", "secs", "second", "seconds"),
		MINUTES("m", "min", "mins", "minute", "minutes"),
		HOURS("h", "hr", "hrs", "hour", "hours"),
		DAYS("d", "day", "days"),
		WEEKS("w", "week", "weeks"),
		MONTHS("mo", "month", "months"),
		YEARS("y", "yr", "yrs", "year", "years")
		;
		
		private final HashSet<String> aliases = new HashSet<String>();
		
		private TimeUnit(String... aliases) {
			this.aliases.addAll(Arrays.asList(aliases));
		}
		
		public boolean matches(String timeUnit) {
			for(String s : aliases) {
				if(s.equalsIgnoreCase(timeUnit)) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isValidTimeUnit(String timeUnit) {
			for(TimeUnit unit : values()) {
				if(unit.matches(timeUnit)) {
					return true;
				}
			}
			return false;
		}
		
		public static TimeUnit getTimeUnit(String timeUnit) {
			for(TimeUnit unit : values()) {
				if(unit.matches(timeUnit)) {
					return unit;
				}
			}
			throw new IllegalArgumentException(timeUnit);
		}
	}
	
	public static class DurationBuilder {
		private Duration duration;
		private Duration min;
		private Duration max;
		
		public DurationBuilder(Duration min, Duration max) {
			this.min = min;
			this.max = max;
			this.duration = Duration.ZERO;
		}
		
		public DurationBuilder add(int i, String timeUnit) {
			return add(i, TimeUnit.getTimeUnit(timeUnit));
		}
		
		public DurationBuilder add(int i, TimeUnit timeUnit) {
			duration = duration.plus(computeDuration(i, timeUnit));
			return this;
		}
		
		public boolean canAccept(int i, String timeUnit) {
			if(i > 0 && TimeUnit.isValidTimeUnit(timeUnit)) {
				return true;
			}
			return false;
		}
		
		public boolean canAccept(int i, TimeUnit timeUnit) {
			return i > 0;
		}
		
		public boolean isValid() {
			return duration.compareTo(min) >= 0 && duration.compareTo(max) <= 0;
		}
		
		public Duration getDuration() {
			return duration;
		}
	}
}
