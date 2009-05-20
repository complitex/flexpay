package org.flexpay.bti.persistence.apartment;

import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class ApartmentAttributeConfig {

	private static final Map<String, Boolean> temporalFlags = CollectionUtils.map();

	// Количество проживающих
	public static final String ATTR_NUMBER_OF_HABITANTS = "ATTR_NUMBER_OF_HABITANTS";
	// Площадь общая
	public static final String ATTR_TOTAL_SQUARE = "ATTR_TOTAL_SQUARE";
	// Площадь жилая
	public static final String ATTR_LIVE_SQUARE = "ATTR_LIVE_SQUARE";

}
