package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.history.impl.SoapOutHistoryTransport;
import org.flexpay.common.service.transport.OutTransport;

/**
 * Web services out transport config
 */
public class WSOutTransportConfig extends OutTransportConfig {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Create OutTransport from config
	 *
	 * @return Transport instance
	 */
	public OutTransport createTransport() {

		SoapOutHistoryTransport transport = new SoapOutHistoryTransport();
		transport.setUrl(url);

		return transport;
	}
}
