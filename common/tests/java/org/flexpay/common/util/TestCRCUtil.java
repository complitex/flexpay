package org.flexpay.common.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TestCRCUtil {

	@Test
	public void testCrc16() {
		assertEquals("Crc16 calc failure 1", 25284, CRCUtil.crc16(new byte[]{1, 2, 3}));
		assertEquals("Crc16 calc failure 2", 62574, CRCUtil.crc16(new byte[]{3, 2, 1}));
		assertEquals("Crc16 calc failure 3", 37630, CRCUtil.crc16(new byte[]{1, 2, 3, 4, 5, 6, 7}));
		assertEquals("Crc16 calc failure 4", 7694, CRCUtil.crc16(new byte[]{1}));
	}
}
