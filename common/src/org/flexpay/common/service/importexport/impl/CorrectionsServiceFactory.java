package org.flexpay.common.service.importexport.impl;

public class CorrectionsServiceFactory {

	private CorrectionsServiceImpl correctionsService;

	public CorrectionsServiceImpl getInstance() {
		if (correctionsService == null) {
			correctionsService = new CorrectionsServiceImpl();
		}

		return correctionsService;
	}
}
