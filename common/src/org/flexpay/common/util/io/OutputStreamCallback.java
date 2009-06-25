package org.flexpay.common.util.io;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamCallback {

	/**
	 * Do write to OutputStream
	 *
	 * @param os OutputStream
	 * @throws java.io.IOException if write fails
	 */
	void write(OutputStream os) throws IOException;
}
