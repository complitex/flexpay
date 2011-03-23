package org.flexpay.common.service.transport.impl;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.transport.OutTransport;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

public class OutTransportChain implements OutTransport {

	private List<OutTransport> transports = CollectionUtils.list();

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 * @throws Exception if failure occurs
	 */
	public void send(FPFile file) throws Exception {

		for (OutTransport transport : transports) {
			transport.send(file);
		}
	}

	public void setTransports(List<OutTransport> transports) {
		this.transports = transports;
	}
}
