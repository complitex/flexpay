package org.flexpay.common.service.importexport.imp;

public class CorrectionsServiceFactory {

	private CorrectionsServiceImpl correctionsService;

	public CorrectionsServiceImpl getInstance() {
		if (correctionsService == null) {
			correctionsService = new CorrectionsServiceImpl();
		}

		return correctionsService;
	}
}
