package org.flexpay.bti.service;

public abstract class Security extends org.flexpay.common.service.Security {

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		org.flexpay.ab.service.Security.touch();
	}
}
