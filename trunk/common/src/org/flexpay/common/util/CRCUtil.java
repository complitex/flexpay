package org.flexpay.common.util;

public class CRCUtil {

	/**
	 * Port of the following C implementation:
	 * <pre>
	 * WORD crc16(BYTE *buf, WORD num) {
	 *    int i;
	 *    WORD crc = 0xffff;
	 *    while(num--) {
	 *        crc ^= *buf++;
	 *        i = 8;
	 *        do {
	 *           if (crc & 1)
	 *             crc = ( crc >> 1 ) ^ 0x8408;
	 *           else
	 *             crc >>= 1;
	 *        } while(--i);
	 *    }
	 *    return crc;
	 * }
	 * </pre>
	 *
	 * @param buf bytes buffer
	 * @return crc16 code
	 */
	public static char crc16(byte[] buf) {

		char crc = 0xffff;
		for (byte b : buf) {
			crc ^= b;
			for (int j = 0; j < 8; ++j) {
				if ((crc & 1) != 0)
					crc = (char) ((crc >> 1) ^ 0x8408);
				else
					crc >>= 1;
			}
		}
		return crc;
	}
}
