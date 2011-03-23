package org.flexpay.tc.util.config;

import org.flexpay.tc.service.Security;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

import static org.flexpay.common.util.config.ApplicationConfig.getDataRoot;

public class ApplicationConfig {

	private String tcDataRoot;

	private int maximumFloors;
	private int maximumPporches;
	private int maximumApartments;

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	static {
		// ensure Security fields are initialised
		Security.touch();
	}


	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	public static int getMaximumFloors() {
		return getInstance().maximumFloors;
	}

	@Required
	public void setMaximumFloors(int maximumFloors) {
		this.maximumFloors = maximumFloors;
	}

	public static int getMaximumPporches() {
		return getInstance().maximumPporches;
	}

	@Required
	public void setMaximumPporches(int maximumPporches) {
		this.maximumPporches = maximumPporches;
	}

	public static int getMaximumApartments() {
		return getInstance().maximumApartments;
	}

	@Required
	public void setMaximumApartments(int maximumApartments) {
		this.maximumApartments = maximumApartments;
	}

	public static File getTcDataRoot() {
		return getTcDataRootInternal();
	}

	private static File getTcDataRootInternal() {
		return new File(getDataRoot(), getInstance().tcDataRoot);
	}

	@Required
	public void setTcDataRoot(String tcDataRoot) {
		this.tcDataRoot = tcDataRoot;
		File tcRoot = getTcDataRootInternal();
		if (!tcRoot.exists()) {
			//noinspection ResultOfMethodCallIgnored
			tcRoot.mkdirs();
		}
	}

}
