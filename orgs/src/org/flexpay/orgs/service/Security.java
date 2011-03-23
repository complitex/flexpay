package org.flexpay.orgs.service;

import org.flexpay.common.util.CollectionUtils;

import static org.flexpay.orgs.service.Roles.*;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
				PAYMENT_POINT_READ,
				PAYMENT_POINT_ADD,
				PAYMENT_POINT_CHANGE,
				PAYMENT_POINT_DELETE,
				PAYMENT_COLLECTOR_READ,
				PAYMENT_COLLECTOR_ADD,
				PAYMENT_COLLECTOR_CHANGE,
				PAYMENT_COLLECTOR_DELETE,
				SERVICE_ORGANIZATION_READ,
				SERVICE_ORGANIZATION_ADD,
				SERVICE_ORGANIZATION_CHANGE,
				SERVICE_ORGANIZATION_DELETE,
				SERVICE_PROVIDER_READ,
				SERVICE_PROVIDER_ADD,
				SERVICE_PROVIDER_CHANGE,
				SERVICE_PROVIDER_DELETE,
				BANK_READ,
				BANK_ADD,
				BANK_CHANGE,
				BANK_DELETE,
				ORGANIZATION_READ,
				ORGANIZATION_ADD,
				ORGANIZATION_CHANGE,
				ORGANIZATION_DELETE,
				CASHBOX_READ,
				CASHBOX_ADD,
				CASHBOX_CHANGE,
				CASHBOX_DELETE
		));
	}

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		// do nothing
	}

}
