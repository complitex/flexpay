package org.flexpay.sz.service;

public class ServiceHolder {

	private static SzFileService szFileService;

	public static void setSzFileService(SzFileService szFService) {
		szFileService = szFService;
	}

	public static SzFileService getSzFileService() {
		return szFileService;
	}

}
