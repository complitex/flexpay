package org.flexpay.ab.service;

import org.flexpay.common.util.CollectionUtils;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
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
