package org.flexpay.orgs.service;

import org.flexpay.common.util.CollectionUtils;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(CollectionUtils.list(
				Roles.PAYMENT_POINT_READ,
				Roles.PAYMENTS_COLLECTOR_READ,
				Roles.SERVICE_ORGANIZATION_READ,
				Roles.SERVICE_PROVIDER_READ,
				Roles.BANK_READ,
				Roles.ORGANIZATION_READ,
				Roles.PAYMENT_POINT_ADD,
				Roles.PAYMENT_POINT_CHANGE,
				Roles.PAYMENT_POINT_DELETE,
				Roles.PAYMENTS_COLLECTOR_ADD,
				Roles.PAYMENTS_COLLECTOR_CHANGE,
				Roles.PAYMENTS_COLLECTOR_DELETE,
				Roles.SERVICE_ORGANIZATION_ADD,
				Roles.SERVICE_ORGANIZATION_CHANGE,
				Roles.SERVICE_ORGANIZATION_DELETE,
				Roles.SERVICE_PROVIDER_ADD,
				Roles.SERVICE_PROVIDER_CHANGE,
				Roles.SERVICE_PROVIDER_DELETE,
				Roles.BANK_ADD,
				Roles.BANK_CHANGE,
				Roles.BANK_DELETE,
				Roles.ORGANIZATION_ADD,
				Roles.ORGANIZATION_CHANGE,
				Roles.ORGANIZATION_DELETE,
				Roles.CASHBOX_READ,
				Roles.CASHBOX_ADD,
				Roles.CASHBOX_CHANGE,
				Roles.CASHBOX_DELETE
		));
	}

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		// do nothing
	}
}
