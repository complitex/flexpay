package org.flexpay.ab.service;

import org.flexpay.common.util.CollectionUtils;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
				Roles.BUILDING_READ,
				Roles.BUILDING_ADD,
				Roles.BUILDING_CHANGE,
				Roles.BUILDING_DELETE,
				Roles.STREET_READ,
				Roles.STREET_ADD,
				Roles.STREET_CHANGE,
				Roles.STREET_DELETE,
				Roles.STREET_TYPE_READ,
				Roles.STREET_TYPE_ADD,
				Roles.STREET_TYPE_CHANGE,
				Roles.STREET_TYPE_DELETE,
				Roles.BUILDING_ATTRIBUTE_TYPE_READ,
				Roles.BUILDING_ATTRIBUTE_TYPE_ADD,
				Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE,
				Roles.BUILDING_ATTRIBUTE_TYPE_DELETE,
				Roles.DISTRICT_READ,
				Roles.DISTRICT_ADD,
				Roles.DISTRICT_CHANGE,
				Roles.DISTRICT_DELETE,
				Roles.TOWN_TYPE_READ,
				Roles.TOWN_TYPE_ADD,
				Roles.TOWN_TYPE_CHANGE,
				Roles.TOWN_TYPE_DELETE,
				Roles.TOWN_READ,
				Roles.TOWN_ADD,
				Roles.TOWN_CHANGE,
				Roles.TOWN_DELETE
		));
	}

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		// do nothing
	}
}
