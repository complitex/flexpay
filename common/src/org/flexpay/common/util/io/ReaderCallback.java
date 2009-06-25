package org.flexpay.common.util.io;

import java.io.IOException;
import java.io.Reader;

public interface ReaderCallback {

	/**
	 * Do read Reader
	 *
	 * @param r Reader
	 * @throws java.io.IOException if read fails
	 */
	void read(Reader r) throws IOException;
}
