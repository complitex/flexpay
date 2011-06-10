package org.flexpay.common.service.impl;

import org.flexpay.common.dao.registry.RegistryRecordStatusDao;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class RegistryRecordStatusServiceImpl implements RegistryRecordStatusService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryRecordStatusDao registryRecordStatusDao;

	@Transactional (readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Override
	public RegistryRecordStatus findByCode(int code) {

		log.debug("Finding status by code: {}", code);
		List<RegistryRecordStatus> statuses = listAllStatuses();
		for (RegistryRecordStatus status : statuses) {
			if (status.getCode() == code) {
				return status;
			}
		}

		return null;
	}

	/**
	 * Find all registry statuses
	 *
	 * @return list of statuses
	 */
	@NotNull
	@Transactional (readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<RegistryRecordStatus> listAllStatuses() {
		log.debug("Finding all statuses");
		return registryRecordStatusDao.findAll();
	}

    @Required
	public void setSpRegistryRecordStatusDao(RegistryRecordStatusDao registryRecordStatusDao) {
		this.registryRecordStatusDao = registryRecordStatusDao;
	}
}
