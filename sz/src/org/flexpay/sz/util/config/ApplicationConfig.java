package org.flexpay.sz.util.config;

import org.flexpay.sz.service.Security;

import java.io.File;

public class ApplicationConfig extends org.flexpay.eirc.util.config.ApplicationConfig {

	private String szDataRoot;
	private String szDefaultDbfFileEncoding;

	static {
		// ensure Security fields are initialised
		Security.touch();
	}


	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	public static File getSzDataRoot() {
		return getInstance().getSzDataRootInternal();
	}

	private File getSzDataRootInternal() {
		return new File(getDataRootInternal(), szDataRoot);
	}

	public void setSzDataRoot(String szDataRoot) {
		this.szDataRoot = szDataRoot;
		File szRoot = getSzDataRootInternal();
		if (!szRoot.exists()) {
			szRoot.mkdirs();
		}
	}

	public static String getSzDefaultDbfFileEncoding() {
		return getInstance().szDefaultDbfFileEncoding;
	}

	public void setSzDefaultDbfFileEncoding(String szDefaultDbfFileEncoding) {
		this.szDefaultDbfFileEncoding = szDefaultDbfFileEncoding;
	}

}
