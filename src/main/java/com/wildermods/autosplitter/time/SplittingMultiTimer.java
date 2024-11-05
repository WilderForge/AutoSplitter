package com.wildermods.autosplitter.time;

import java.util.Arrays;
import java.util.LinkedList;

import static com.wildermods.autosplitter.time.SplitSegment.NULL_SEGMENT;

public class SplittingMultiTimer extends PausingTimer {
	
	private int index;
	private SplitSegment currentSegment = NULL_SEGMENT;
	private LinkedList<SplitSegment> segments = new LinkedList<>();
	
	public SplittingMultiTimer(RealTimeSegment parent, SplitSegment... segments) {
		super(parent);
	}

	public void split() {
		SplitSegment nextSegment = getNextSegment();
		if(nextSegment == NULL_SEGMENT) {
			stop();
			return;
		}
		index++;
		currentSegment = nextSegment;
	}
	
	public void splitAndAddSegment(SplitSegment segment) { //for games with dynamic splits/subsplits
		addSegments(segment);
		split();
	}
	
	public void addSegments(SplitSegment... segments) {
		if(currentSegment == NULL_SEGMENT && segments.length > 0) {
			index = 0;
			currentSegment = segments[0];
		}
		this.segments.addAll(Arrays.asList(segments));
	}
	
	public SplitSegment getNextSegment() {
		if(currentSegment == NULL_SEGMENT) {
			return NULL_SEGMENT;
		}
		if(segments.peekLast() == currentSegment) {
			return NULL_SEGMENT;
		}
		return segments.get(index + 1);
	}
	
}
