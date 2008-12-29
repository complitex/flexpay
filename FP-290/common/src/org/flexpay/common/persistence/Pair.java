package org.flexpay.common.persistence;

public class Pair<A, B> {

	private final A first;
	private final B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Getter for property 'first'.
	 *
	 * @return Value for property 'first'.
	 */
	public A getFirst() {
		return first;
	}

	/**
	 * Getter for property 'second'.
	 *
	 * @return Value for property 'second'.
	 */
	public B getSecond() {
		return second;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	private static boolean equals(Object x, Object y) {
		return (x == null && y == null) || (x != null && x.equals(y));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (!(other instanceof Pair)) {
			return false;
		}
		Pair that = (Pair) other;
		return equals(first, that.getFirst()) && equals(second, that.getSecond());
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		if (first == null) {
			return (second == null) ? 0 : second.hashCode() + 1;
		} else if (second == null) {
			return first.hashCode() + 2;
		} else {
			return first.hashCode() * 17 + second.hashCode();
		}
	}
}