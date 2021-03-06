package org.flexpay.common.util.selftest;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.InputStream;

public class TestGetResource {

	public void doSelfTesting() throws Exception {

		validateResourceAvailable("WEB-INF/common.properties");
		validateResourceAvailable("resources/common/style/fp.css");
	}

	private void validateResourceAvailable(String resource) {

		InputStream is = ApplicationConfig.getResourceAsStream(resource);
		try {
			if (is == null) {
				throw new RuntimeException("Cannot get required resource: " + resource);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
