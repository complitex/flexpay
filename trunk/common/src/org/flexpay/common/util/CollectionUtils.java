package org.flexpay.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

import java.util.*;

public class CollectionUtils {

	@NotNull
	public static <T> T[] ar(@NonNls T... ts) {
		return ts;
	}

	@NotNull
	public static <T> List<T> list(@NonNls @NotNull T... ts) {
		return new ArrayList<T>(ts.length);
	}

	@NotNull
	public static <T> List<T> list(@NotNull Collection<T> ts) {
		return new ArrayList<T>(ts);
	}

	@NotNull
	public static <T> Set<T> set(@NonNls T... ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

	@NotNull
	public static <T> Set<T> set(Collection<T> ts) {
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

	/**
	 * Create a instance of TreeMap
	 *
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> Map<K, V> treeMap() {
		return new TreeMap<K,V>();
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
