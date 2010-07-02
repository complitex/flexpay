package org.flexpay.common.util;

import java.math.BigDecimal;

public class BigDecimalFormat {

	// Returns the BigDecimal value n with trailing
	// zeroes removed.
	public static BigDecimal trim(BigDecimal n) {
		try {
			for (;;) {
				n = n.setScale(n.scale() - 1);
			}
		} catch (ArithmeticException e) {
			// no more trailing zeroes so exit.
		}
		return n;
	}

	// Returns the BigDecimal value n with exactly
	// 'prec' decimal places.

	// Zeroes are padded to the right of the decimal
	// point if necessary.
	public static BigDecimal format(BigDecimal n, int prec) {
		return n.setScale(prec, BigDecimal.ROUND_HALF_UP);
	}
}
