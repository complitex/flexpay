package org.flexpay.common.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestCollectionUtils {

	@Test
	public void testMap() {
		Long[] arr1 = {1L, 2L, 3L};
		Integer[] arr2 = null;
		Map<Long, Integer> map = CollectionUtils.map(arr1, arr2);

		assertEquals("Invalid map size", 3, map.size());
		assertNull("Incorrect", map.get(1L));
		assertNull("Incorrect", map.get(2L));
		assertNull("Incorrect", map.get(3L));
	}
}
