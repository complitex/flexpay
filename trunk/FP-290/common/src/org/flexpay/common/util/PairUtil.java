package org.flexpay.common.util;

import org.flexpay.common.persistence.Pair;

public class PairUtil {

	/**
	 * Create pair
	 *
	 * @param a First pair component
	 * @param b Second pair component
	 * @return pair consisting of <code>a</code> and <code>b</code>
	 */
	public static <A, B> Pair<A, B> pair(A a, B b) {
		return new Pair<A, B>(a, b);
	}
}
