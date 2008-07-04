package org.flexpay.common.util;

import java.util.*;

public class CollectionUtils {

	public static <T> T[] ar(T... ts) {
		return ts;
	}

	public static <T> Set<T> set(T... ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

	/**
	 * Create a instance of HashMap
	 *
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	public static <K, V> Map<K, V> map() {
		return new HashMap<K,V>();
	}

	public static <K, V> Map<K, V> map(K[] keys, V[] values) {
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
