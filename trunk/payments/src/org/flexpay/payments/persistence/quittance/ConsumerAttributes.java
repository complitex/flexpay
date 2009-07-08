package org.flexpay.payments.persistence.quittance;

import org.flexpay.common.util.CollectionUtils;

import java.util.List;

public class ConsumerAttributes {

	public static final String ATTR_ERC_ACCOUNT = "ATTR_ERC_ACCOUNT";

	public static final List<String> PAYMENT_ATTRIBUTES = CollectionUtils.list(
			ATTR_ERC_ACCOUNT
	);
}
