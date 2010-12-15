package org.flexpay.payments.service;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;

import java.util.List;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.orgs.service.Roles.CASHBOX_READ;
import static org.flexpay.orgs.service.Roles.PAYMENT_POINT_READ;
import static org.flexpay.orgs.service.Roles.SERVICE_PROVIDER_READ;
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
     * Set of authorities names for quittance finder process user
     */
    private static final List<String> USER_OUTER_REQUEST_AUTHORITIES = list(
            OPERATION_READ,
            OPERATION_ADD,
            OPERATION_CHANGE,
            OPERATION_DELETE,
            APARTMENT_READ,
            BUILDING_READ,
            STREET_READ,
            CASHBOX_READ,
            PAYMENT_POINT_READ,
            PROCESS_READ,
            SERVICE_READ,
            SERVICE_PROVIDER_READ,
            DOCUMENT_TYPE_READ,
            DOCUMENT_ADD,
            DOCUMENT_CHANGE,
            PERSON_READ
    );

    /**
     * Do syncer user authentication
     */
    public static void authenticateOuterRequest(String login) {
        SecurityUtil.authenticate(login, USER_OUTER_REQUEST_AUTHORITIES);
    }

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		org.flexpay.ab.service.Security.touch();
		org.flexpay.orgs.service.Security.touch();
	}

}
