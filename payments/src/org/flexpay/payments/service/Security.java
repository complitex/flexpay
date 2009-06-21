package org.flexpay.payments.service;

import org.flexpay.common.util.CollectionUtils;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
				Roles.SERVICE_READ,
				Roles.SERVICE_TYPE_READ,
				Roles.SERVICE_ADD,
				Roles.SERVICE_CHANGE,
				Roles.SERVICE_DELETE,
				Roles.SERVICE_TYPE_ADD,
				Roles.SERVICE_TYPE_CHANGE,
				Roles.SERVICE_TYPE_DELETE				
		));
	}

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		org.flexpay.ab.service.Security.touch();
		org.flexpay.orgs.service.Security.touch();
	}
}
