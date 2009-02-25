package org.flexpay.common.service.imp;

public class ServiceFactory {

	private DiffServiceImpl diffService;

	public DiffServiceImpl getDiffService() {
		if (diffService == null) {
			diffService = new DiffServiceImpl();
		}
		return diffService;
	}
}
