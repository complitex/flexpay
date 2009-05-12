package org.flexpay.payments.util;

public class ServiceFullIndexUtil {

	private static final String DELIMITER = "z";

	public static String getServiceIdFromIndex(String serviceFullIndex) {

		return serviceFullIndex.substring(serviceFullIndex.indexOf(DELIMITER) + 1);
	}

	public static String getServiceFullIndex(String quittanceId, String serviceId) {

		return quittanceId + DELIMITER + serviceId;
	}
}
