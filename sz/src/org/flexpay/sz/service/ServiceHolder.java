package org.flexpay.sz.service;

public class ServiceHolder {

	private static SzFileService szFileService;

	public void setSzFileService(SzFileService szFileService) {
		this.szFileService = szFileService;
	}

	public static SzFileService getSzFileService() {
		return szFileService;
	}

}
