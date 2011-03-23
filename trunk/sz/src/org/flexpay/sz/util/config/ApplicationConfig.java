package org.flexpay.sz.util.config;

import org.flexpay.sz.service.Security;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

import static org.flexpay.common.util.config.ApplicationConfig.getDataRoot;

public class ApplicationConfig {

	private String szDataRoot;
	private String szDefaultDbfFileEncoding;

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();
	
	static {
		// ensure Security fields are initialised
		Security.touch();
	}


	protected static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	private File getSzDataRootInternal() {
		return new File(getDataRoot(), szDataRoot);
	}

	@Required
	public void setSzDataRoot(String szDataRoot) {
		this.szDataRoot = szDataRoot;
		File szRoot = getSzDataRootInternal();
		if (!szRoot.exists()) {
			//noinspection ResultOfMethodCallIgnored
			szRoot.mkdirs();
		}
	}

	public static String getSzDefaultDbfFileEncoding() {
		return getInstance().szDefaultDbfFileEncoding;
	}

	@Required
	public void setSzDefaultDbfFileEncoding(String szDefaultDbfFileEncoding) {
		this.szDefaultDbfFileEncoding = szDefaultDbfFileEncoding;
	}

}
