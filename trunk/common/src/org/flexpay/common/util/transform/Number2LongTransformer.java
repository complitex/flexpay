package org.flexpay.common.util.transform;

import org.jetbrains.annotations.NotNull;

public class Number2LongTransformer implements Transformer<Number, Long> {

	/**
	 * Do transformation
	 *
	 * @param number Source object
	 * @return Target type object
	 */
	@NotNull
	@Override
	public Long transform(@NotNull Number number) {
		return number.longValue();
	}
}
