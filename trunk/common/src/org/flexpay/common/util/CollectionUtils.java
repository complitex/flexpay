package org.flexpay.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CollectionUtils {

	@NotNull
	public static <T> T[] ar(T... ts) {
		return ts;
	}

	@NotNull
	public static <T> Set<T> set(T... ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

	@NotNull
	public static <T> Set<T> set(List<T> ts) {
		return new HashSet<T>(ts);
	}

	/**
	 * Create a instance of HashMap
	 *
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> Map<K, V> map() {
		return new HashMap<K,V>();
	}

	@NotNull
	public static <K, V> Map<K, V> map(@NotNull K[] keys, @NotNull V[] values) {
		Map<K, V> map = map();
		int n = 0;
		for (K k : keys) {
			V v = n < values.length ? values[n] : null;
			map.put(k, v);
			++n;
		}

		return map;
	}
}
