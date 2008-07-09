package org.flexpay.ab.util.standalone;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.util.standalone.StandaloneTask;

public class RetriveStreetTask implements StandaloneTask {

	private Logger log = Logger.getLogger(getClass());
	private StreetService streetService;

	/**
	 * Execute task
	 */
	public void execute() {
//		Street street = streetDao.readFull(6L);
		Street street = streetService.read(6L);
		log.info("===============================================\n" + street.getTypeTemporals());
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
