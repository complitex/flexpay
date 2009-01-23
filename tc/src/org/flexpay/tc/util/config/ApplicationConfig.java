package org.flexpay.tc.util.config;

import java.io.File;

public class ApplicationConfig extends org.flexpay.ab.util.config.ApplicationConfig {

	private String tcDataRoot;

	private int maximumFloors;
	private int maximumPporches;
	private int maximumAppartments;

	protected static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}

	public static int getMaximumFloors() {
		return getInstance().maximumFloors;
	}

	public void setMaximumFloors(String maximumFloors) {
		this.maximumFloors = Integer.valueOf(maximumFloors);
	}

	public static int getMaximumPporches() {
		return getInstance().maximumPporches;
	}

	public void setMaximumPporches(String maximumPporches) {
		this.maximumPporches = Integer.valueOf(maximumPporches);
	}

	public static int getMaximumAppartments() {
		return getInstance().maximumAppartments;
	}

	public void setMaximumAppartments(String maximumAppartments) {
		this.maximumAppartments = Integer.valueOf(maximumAppartments);
	}

	public static File getTcDataRoot() {
		return getInstance().getTcDataRootInternal();
	}

	private File getTcDataRootInternal() {
		return new File(getDataRootInternal(), tcDataRoot);
	}

	public void setTcDataRoot(String tcDataRoot) {
		this.tcDataRoot = tcDataRoot;
		File tcRoot = getTcDataRootInternal();
		if (!tcRoot.exists()) {
			tcRoot.mkdirs();
		}
	}

}
