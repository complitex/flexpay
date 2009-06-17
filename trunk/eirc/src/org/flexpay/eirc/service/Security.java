package org.flexpay.eirc.service;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;

import java.util.List;

public abstract class Security extends org.flexpay.common.service.Security {

	/**
	 * Quittance finder process user
	 */
	private static final String USER_QUITTANCE_FINDER = "history-syncer";

	/**
	 * Set of authorities names for quittance finder process user
	 */
	private static final List<String> USER_QUITTANCE_FINDER_AUTHORITIES = CollectionUtils.list(
			org.flexpay.common.service.Roles.BASIC,
			Roles.QUITTANCE_READ,
			Roles.QUITTANCE_PAYMENT_READ,
			Roles.ACCOUNT_READ,
			org.flexpay.ab.service.Roles.APARTMENT_READ,
			org.flexpay.ab.service.Roles.PERSON_READ
	);

	public static void authenticateQuittanceFinder() {
		SecurityUtil.authenticate(USER_QUITTANCE_FINDER, USER_QUITTANCE_FINDER_AUTHORITIES);
	}

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		org.flexpay.bti.service.Security.touch();
		org.flexpay.payments.service.Security.touch();
	}
}
