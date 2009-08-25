package org.flexpay.common.persistence;

/**
 * Range class taken from http://martinfowler.com/ap2/range.html
 *
 * @param <T> Range type 
 */
public abstract class Range<T extends Comparable<T>> {

	private T start;
	private T end;

	public Range(T start, T end) {
		this.start = start;
		this.end = end;
	}

	public T getStart() {
		return start;
	}

	public T getEnd() {
		return end;
	}

	public boolean includes(T t) {
		return start.compareTo(t) <= 0 && t.compareTo(end) <= 0;
	}
}
