package org.flexpay.payments.actions.outerrequest.request.response.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class ConsumerAttributes {

	public static final String ATTR_ERC_ACCOUNT = "ATTR_ERC_ACCOUNT";

	public static final String ATTR_NUMBER_TENANTS = "ATTR_NUMBER_TENANTS";
	public static final String ATTR_NUMBER_REGISTERED_TENANTS = "ATTR_NUMBER_REGISTERED_TENANTS";
	public static final String ATTR_TOTAL_SQUARE = "ATTR_TOTAL_SQUARE";
	public static final String ATTR_LIVE_SQUARE = "ATTR_LIVE_SQUARE";
	public static final String ATTR_HEATING_SQUARE = "ATTR_HEATING_SQUARE";

	public static final List<String> PAYMENT_ATTRIBUTES = list(
			ATTR_ERC_ACCOUNT
	);

	// Attribute name, default value
	public static final Map<String, Serializable> CALCULATION_ATTRIBUTES =
			new HashMap<String, Serializable>();
	static {
		CALCULATION_ATTRIBUTES.put(ATTR_NUMBER_TENANTS, 0);
		CALCULATION_ATTRIBUTES.put(ATTR_NUMBER_REGISTERED_TENANTS, 0);
		CALCULATION_ATTRIBUTES.put(ATTR_TOTAL_SQUARE, 0.0);
		CALCULATION_ATTRIBUTES.put(ATTR_LIVE_SQUARE, 0.0);
		CALCULATION_ATTRIBUTES.put(ATTR_HEATING_SQUARE, 0.0);
	};
}
