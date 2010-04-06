package org.flexpay.common.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class KeyStoreUtil {

	private static KeyStore keyStore;

	public static KeyStore loadKeyStore() throws FlexPayException {

		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream storeInputStream = ApplicationConfig.getResourceAsStream(ApplicationConfig.getKeystorePath());
			char[] storePassword = (ApplicationConfig.getKeystorePassword()).toCharArray();
			keyStore.load(storeInputStream, storePassword);

			return keyStore;
		} catch (Exception e) {
			throw new FlexPayException("Error loading keystore", e);
		}
	}

	public static void saveKeyStore() throws FlexPayException {

		try {
			File file = ApplicationConfig.getResourceAsFile(ApplicationConfig.getKeystorePath());
			char[] storePassword = (ApplicationConfig.getKeystorePassword()).toCharArray();
			keyStore.store(new FileOutputStream(file), storePassword);
		} catch (Exception e) {
			throw new FlexPayException("Error saving keystore", e);
		}
	}
}
