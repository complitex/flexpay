package org.flexpay.common.util;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.util.transform.Transformer;
import org.hibernate.envers.tools.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CollectionUtils {

	@NotNull
	public static <T> T[] ar(T... ts) {
		return ts;
	}

	@NotNull
	public static <T> List<T> list() {
		return new ArrayList<T>();
	}

	@NotNull
	public static <T> List<T> list(@NotNull T... ts) {
		return list(Arrays.asList(ts));
	}

	@NotNull
	public static <T> List<T> list(@NotNull Collection<T> ts) {
		return new ArrayList<T>(ts);
	}

	@NotNull
	public static <T> List<T> listSlice(@NotNull List<T> ts, int first, int last) {
		List<T> list = list();
		for (int n = first; n < last && n < ts.size(); ++n) {
			list.add(ts.get(n));
		}

		return list;
	}

	@NotNull
	public static <T> Set<T> set() {
		return new HashSet<T>();
	}

	@NotNull
	public static <T> Set<T> set(T... ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

	@NotNull
	public static <T> Set<T> set(Collection<T> ts) {
		return new HashSet<T>(ts);
	}

	@NotNull
	public static <T> Set<T> set(Collection<T> ts, Collection<T> ts2) {
		Set<T> set = new HashSet<T>(ts);
		set.addAll(ts2);
		return set;
	}

	/**
	 * Create an instance of HashMap
	 *
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> Map<K, V> map() {
		return new HashMap<K, V>();
	}

	/**
	 * Create an instance of HashMap from single key-value pair
	 *
	 * @param k   Single entry key
	 * @param v   Single entry value
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> Map<K, V> map(K k, V v) {
		Map<K, V> map = new HashMap<K, V>();
		map.put(k, v);
		return map;
	}

	@NotNull
	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> map = new HashMap<K, V>();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	@NotNull
	public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> map = new HashMap<K, V>();
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	/**
	 * Create an instance of HashMap from existing map
	 *
	 * @param values Initial map data
	 * @param <K>    key type
	 * @param <V>    value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> Map<K, V> map(Map<K, V> values) {
		return new HashMap<K, V>(values);
	}

	/**
	 * Create an instance of TreeMap
	 *
	 * @param <K> key type
	 * @param <V> value type
	 * @return Map
	 */
	@NotNull
	public static <K, V> SortedMap<K, V> treeMap() {
		return new TreeMap<K, V>();
	}

	@NotNull
	public static <K, V> SortedMap<K, V> treeMap(@NotNull K[] keys, @NotNull V[] values) {
		SortedMap<K, V> map = treeMap();
		int n = 0;
		for (K k : keys) {
			V v = n < values.length ? values[n] : null;
			map.put(k, v);
			++n;
		}

		return map;
	}

	@NotNull
	public static <K, V> SortedMap<K, V> treeMap(@NotNull Pair<K, V>... pairs) {
		SortedMap<K, V> map = treeMap();
		for (Pair<K, V> pair : pairs) {
			map.put(pair.getFirst(), pair.getSecond());
		}

		return map;
	}

	@NotNull
	public static <K, V> Map<K, V> map(@NotNull Pair<K, V>... pairs) {
		Map<K, V> map = map();
		for (Pair<K, V> pair : pairs) {
			map.put(pair.getFirst(), pair.getSecond());
		}

		return map;
	}

	@NotNull
	public static <K, V> Map<K, V> map(@NotNull K[] keys, V[] values) {
		Map<K, V> map = map();
		int n = 0;
		for (K k : keys) {
			V v = values != null && n < values.length ? values[n] : null;
			map.put(k, v);
			++n;
		}

		return map;
	}

	/**
	 * Helper interface that can get key for map by it's value
	 *
	 * @param <K> Key type
	 * @param <V> Value type
	 */
	public static interface KeyExtractor<K, V> {
		K key(V v);
	}

	@NotNull
	public static <K, V> Map<K, V> map(Collection<V> values, KeyExtractor<K, V> extractor) {
		Map<K, V> map = map();
		for (V v : values) {
			map.put(extractor.key(v), v);
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

	@NotNull
	public static <T> SortedSet<T> treeSet(@NotNull Collection<T> values, Comparator<T> comparator) {
		SortedSet<T> result = new TreeSet<T>(comparator);
		result.addAll(values);
		return result;
	}

	/**
	 * Check if maps values are equals by a specified set of keys
	 *
	 * @param keys Set of keys to check equality against
	 * @param p1   First map
	 * @param p2   Second map
	 * @param <K>  Key parameter type
	 * @param <V>  Value parameter type
	 * @return <code>true</code> if maps has equals values for requested set of keys
	 */
	public static <K, V> boolean isSame(Collection<K> keys, Map<K, V> p1, Map<K, V> p2) {
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		for (K k : keys) {
			equalsBuilder.append(p1.get(k), p2.get(k));
		}

		return equalsBuilder.isEquals();
	}

	/**
	 * Create ArrayStack
	 *
	 * @param objects Objects to put to stack
	 * @return ArrayStack
	 */
	public static ArrayStack arrayStack(@NotNull Object... objects) {
		ArrayStack stack = new ArrayStack();
		for (Object o : objects) {
			stack.push(o);
		}

		return stack;
	}

	public static <S, T> Collection<T> transform(Collection<S> ss, Transformer<S, T> transformer) {

		Collection<T> result = list();
		for (S s : ss) {
			result.add(transformer.transform(s));
		}

		return result;
	}

	public static <T> void copyAttributes(Collection<T> fromTs, Collection<T> toTs, AttributeCopier<T> ac) {

		Iterator<T> fromIt = fromTs.iterator();
		Iterator<T> toIt = toTs.iterator();
		T fromObj = null;
		while (toIt.hasNext()) {
			T toObj = toIt.next();
			if (fromObj == null) {
				fromObj = fromIt.hasNext() ? fromIt.next() : null;
			}
			if (toObj.equals(fromObj)) {
				ac.copy(fromObj, toObj);
				fromObj = null;
			}
		}
	}
}
