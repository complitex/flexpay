package org.flexpay.sz.service;

public abstract class Security extends org.flexpay.common.service.Security {

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		org.flexpay.eirc.service.Security.touch();
	}
}
