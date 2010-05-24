package org.flexpay.eirc.service;

import org.flexpay.common.util.SecurityUtil;

import java.util.List;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.eirc.service.Roles.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.SERVICE_READ;
import static org.flexpay.payments.service.Roles.SERVICE_TYPE_READ;

public abstract class Security extends org.flexpay.common.service.Security {

	/**
	 * Quittance finder process user
	 */
	private static final String USER_QUITTANCE_FINDER = "quittance-finder";

	/**
	 * Set of authorities names for quittance finder process user
	 */
	private static final List<String> USER_QUITTANCE_FINDER_AUTHORITIES = list(
			QUITTANCE_READ,
			QUITTANCE_PAYMENT_READ,
			ACCOUNT_READ,
			APARTMENT_READ,
            BUILDING_READ,
            BUILDING_ATTRIBUTE_TYPE_READ,
            STREET_TYPE_READ,
            STREET_READ,
            DISTRICT_READ,
            TOWN_TYPE_READ,
            TOWN_READ,
            REGION_READ,
            COUNTRY_READ,
			PERSON_READ,
			SERVICE_READ,
			SERVICE_TYPE_READ,
			SERVICE_ORGANIZATION_READ,
			SERVICE_PROVIDER_READ,
			BANK_READ,
			ORGANIZATION_READ
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
