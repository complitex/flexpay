package org.flexpay.ab.util.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.common.persistence.Stub;

public class RetriveStreetTask implements StandaloneTask {

	private Logger log = LoggerFactory.getLogger(getClass());
	private StreetService streetService;

	/**
	 * Execute task
	 */
	public void execute() {
//		Street street = streetDao.readFull(6L);
		Street street = streetService.readFull(new Stub<Street>(6L));
		log.info("===============================================\n{}", street.getTypeTemporals());
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
