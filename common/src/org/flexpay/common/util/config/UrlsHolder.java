package org.flexpay.common.util.config;

import java.net.URL;

/**
 * Urls holder
 */
public class UrlsHolder {

	private URL[] urls;

	public UrlsHolder(URL[] urls) {
		this.urls = urls;
	}

	/**
	 * Getter for property 'urls'.
	 *
	 * @return Value for property 'urls'.
	 */
	public URL[] getUrls() {
		return urls;
	}
}
