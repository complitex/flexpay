package org.flexpay.eirc.process.quittance.report.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class SumUtil {

	public static BigDecimal addNonNegative(@NotNull BigDecimal current, @NotNull BigDecimal val) {

		if (val.compareTo(BigDecimal.ZERO) < 0) {
			return current;
		}

		return current.add(val);
	}

	/**
	 * Add negative value if current is less or equals to {@link BigDecimal#ZERO}, for
	 * positive values if current value is less than {@link BigDecimal#ZERO} result is value
	 * itself
	 *
	 * @param current Current value
	 * @param val	 Value to add
	 * @return sum of current and value
	 */
	public static BigDecimal addNegative(@NotNull BigDecimal current, @NotNull BigDecimal val) {
		if (current.compareTo(BigDecimal.ZERO) <= 0) {
			if (val.compareTo(BigDecimal.ZERO) <= 0) {
				return current.add(val);
			}
			return val;
		}
		if (val.compareTo(BigDecimal.ZERO) <= 0) {
			return current;
		}
		return current.add(val);
	}

}
