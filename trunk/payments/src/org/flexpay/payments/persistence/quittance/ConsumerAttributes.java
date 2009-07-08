package org.flexpay.payments.persistence.quittance;

import org.flexpay.common.util.CollectionUtils;

public class ConsumerAttributes {

	public static final String ATTR_EIRC_ACCOUNT = "ATTR_EIRC_ACCOUNT";

	public static final List<String> PAYMENT_ATTRIBUTES = CollectionUtils.list(
			ATTR_EIRC_ACCOUNT
	);
}
