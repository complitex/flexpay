package org.flexpay.ab.service;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.common.util.CollectionUtils.list;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;

import java.util.List;

public abstract class SyncSecurity {

	/**
	 * History sync process user
	 */
	private static final String USER_HISTORY_SYNCER = "cn-syncer";

	/**
	 * Set of authorities names for syncer process user
	 */
	protected static final List<String> USER_HISTORY_SYNCER_AUTHORITIES = list(
			org.flexpay.common.service.Roles.BASIC,
			PERSON_READ,
			APARTMENT_READ,
			BUILDING_ATTRIBUTE_TYPE_READ,
			BUILDING_READ,
			STREET_READ,
			DISTRICT_READ,
			TOWN_READ,
			REGION_READ,
			COUNTRY_READ,
			TOWN_TYPE_READ,
			STREET_TYPE_READ,
			STREET_TYPE_ADD,
			STREET_TYPE_CHANGE,
			IDENTITY_TYPE_READ,
			APARTMENT_ADD,
			BUILDING_ADD,
			BUILDING_CHANGE,
			BUILDING_DELETE,
			DISTRICT_ADD,
			DISTRICT_CHANGE,
			STREET_ADD,
			STREET_CHANGE,
			TOWN_ADD,
			TOWN_CHANGE,
			PERSON_ADD,
			PERSON_CHANGE,
			COUNTRY_ADD,
			COUNTRY_CHANGE,
			PROCESS_READ,
			TOWN_TYPE_ADD,
			TOWN_TYPE_DELETE,
			TOWN_TYPE_CHANGE
	);

	/**
	 * Do syncer user authentication
	 */
	public static void authenticateSyncer() {
		SecurityUtil.authenticate(USER_HISTORY_SYNCER, USER_HISTORY_SYNCER_AUTHORITIES);
	}

}
