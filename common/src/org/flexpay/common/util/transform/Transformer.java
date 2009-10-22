package org.flexpay.common.util.transform;

import org.jetbrains.annotations.NotNull;

/**
 * Base interface of transformers
 *
 * @param <S> Source transformation type
 * @param <T> Target transformation type
 */
public interface Transformer<S, T> {

	/**
	 * Do transformation
	 *
	 * @param s Source object
	 * @return Target type object
	 */
	@NotNull
	T transform(@NotNull S s);
}
