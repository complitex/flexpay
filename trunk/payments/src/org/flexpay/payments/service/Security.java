package org.flexpay.payments.service;

import org.flexpay.common.util.CollectionUtils;

import static org.flexpay.payments.service.Roles.*;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
				SERVICE_READ,
				SERVICE_TYPE_READ,
				SERVICE_ADD,
				SERVICE_CHANGE,
				SERVICE_DELETE,
				SERVICE_TYPE_ADD,
				SERVICE_TYPE_CHANGE,
				SERVICE_TYPE_DELETE
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
