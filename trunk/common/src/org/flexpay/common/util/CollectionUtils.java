package org.flexpay.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.*;

public class CollectionUtils {

	@NotNull
	public static <T> T[] ar(@NonNls T... ts) {
		return ts;
	}

	@NotNull
	public static <T> List<T> list(@NonNls @NotNull T... ts) {
		return list(Arrays.asList(ts));
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
	public static <K, V> Map<K, V> treeMap(@NotNull K[] keys, @NotNull V[] values) {
		Map<K, V> map = treeMap();
		int n = 0;
		for (K k : keys) {
			V v = n < values.length ? values[n] : null;
			map.put(k, v);
			++n;
		}

		return map;
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

	@NotNull
	public static <T> SortedSet<T> treeSet() {
		return new TreeSet<T>();
	}

	@NotNull
	public static <T> SortedSet<T> treeSet(@NotNull Collection<T> values) {
		return new TreeSet<T>(values);
	}

	/**
	 * Check if maps values are equals by a specified set of keys
	 *
	 * @param keys Set of keys to check equality against
	 * @param p1 First map
	 * @param p2 Second map
	 * @param <K> Key parameter type
	 * @param <V> Value parameter type
	 * @return <code>true</code> if maps has equals values for requested set of keys 
	 */
	public static <K, V> boolean isSame(Collection<K> keys, Map<K, V> p1, Map<K, V> p2) {
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		for (K k : keys) {
			equalsBuilder.append(p1.get(k), p2.get(k));
		}

		return equalsBuilder.isEquals();
	}
}
