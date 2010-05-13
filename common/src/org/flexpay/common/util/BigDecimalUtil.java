package org.flexpay.common.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static boolean isZero(final BigDecimal d) {
		if (d == null) {
            return true;
        }
		final BigDecimal abs = d.abs();
		return abs.equals(abs.negate());
	}

    public static boolean isNotZero(final BigDecimal d) {
        return !isZero(d);
    }

}
