package org.flexpay.common.util.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamCallback {

	/**
	 * Do read InputStream
	 *
	 * @param is InputStream
	 * @throws IOException if read fails
	 */
	void read(InputStream is) throws IOException;
}
