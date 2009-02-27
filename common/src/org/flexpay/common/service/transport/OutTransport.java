package org.flexpay.common.service.transport;

import org.flexpay.common.persistence.FPFile;

/**
 * Outgoing transport
 */
public interface OutTransport {

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 * @throws Exception if failure occurs
	 */
	void send(FPFile file) throws Exception;
}
