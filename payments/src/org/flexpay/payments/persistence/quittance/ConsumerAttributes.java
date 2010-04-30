package org.flexpay.payments.persistence.quittance;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ConsumerAttributes {

	public static final String ATTR_ERC_ACCOUNT = "ATTR_ERC_ACCOUNT";

	public static final List<String> PAYMENT_ATTRIBUTES = list(
			ATTR_ERC_ACCOUNT
	);
}
