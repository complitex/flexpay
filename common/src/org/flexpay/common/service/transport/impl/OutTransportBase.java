package org.flexpay.common.service.transport.impl;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.transport.OutTransport;

/**
 * OutTransport that retries to send file several times, default is 5
 */
public abstract class OutTransportBase implements OutTransport {

	private int nRetries = 5;
	private long timeout = 1000;

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 */
	public void send(FPFile file) throws Exception {

		Exception last = null;
		for (int nTry = 0; nTry < nRetries; ++nTry) {
			try {
				doSend(file);
			} catch (Exception ex) {
				last = ex;
				Thread.sleep(timeout);
			}
		}

		throw new Exception("Failed sending file after " + nRetries + " attempts, give up", last);
	}

	/**
	 * Do actual transporting
	 *
	 * @param file FPFile to send
	 * @throws Exception if failure occurs while transporting
	 */
	protected abstract void doSend(FPFile file) throws Exception;

	/**
	 * Set number of retries, default is 5
	 *
	 * @param nRetries Number of retries
	 */
	public void setNRetries(int nRetries) {
		this.nRetries = nRetries;
	}

	/**
	 * Set retry timeout in millis, default is 1000ms
	 *
	 * @param timeout Wait timeout in millis
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
