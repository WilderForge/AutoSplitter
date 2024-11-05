package com.wildermods.autosplitter.time;

import java.time.Instant;

public class SplitSegment extends TimeSegment {

	public static final SplitSegment NULL_SEGMENT = new NullSplitSegment();
	
	private final String name;
	private String blurb = "";
	private final boolean sub;
	
	public SplitSegment() {
		this(new TimeSegment(), "Unnamed Segment", "", false);
	}
	
	public SplitSegment(Timed derive, String name, String blurb, boolean sub) {
		super(derive);
		this.name = name;
		this.blurb = blurb;
		this.sub = sub;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public String getBlurb() {
		return blurb;
	}
	
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}
	
	public boolean isSubSplit() {
		return sub;
	}
	
	private static class NullSplitSegment extends SplitSegment {
		public NullSplitSegment() {
			super(new TimeSegment(Instant.EPOCH, Instant.EPOCH), "Null Segment", "", false);
		}
	}
	
}
