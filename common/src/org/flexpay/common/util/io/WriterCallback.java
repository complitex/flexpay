package org.flexpay.common.util.io;

import java.io.IOException;
import java.io.Writer;

public interface WriterCallback {

	/**
	 * Do write to Writer
	 *
	 * @param w Writer
	 * @throws java.io.IOException if write fails
	 */
	void write(Writer w) throws IOException;
}
