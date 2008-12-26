package org.flexpay.eirc.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.RegistryDao;
import org.flexpay.eirc.dao.RegistryFileDaoExt;
import org.flexpay.eirc.dao.RegistryRecordDao;
import org.flexpay.eirc.dao.RegistryRecordDaoExt;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.RegistryFileService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class RegistryFileServiceImpl implements RegistryFileService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryDao registryDao;
	private RegistryFileDaoExt registryFileDaoExt;
	private RegistryRecordDao registryRecordDao;
	private RegistryRecordDaoExt registryRecordDaoExt;

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
	public List<SpRegistry> getRegistries(FPFile spFile) {
		return registryDao.listRegistries(spFile.getId());
	}

	/**
	 * Get registry records for processing
	 *
	 * @param registry  Registry header
	 * @param pager	 Page
	 * @param minMaxIds cached minimum and maximum registry record keys
	 * @return list of records
	 */
	public List<RegistryRecord> getRecordsForProcessing(@NotNull Stub<SpRegistry> registry, Page<RegistryRecord> pager, Long[] minMaxIds) {

		// cache min-max rerecord ids
		if (minMaxIds[0] == null || minMaxIds[1] == null) {
			Long[] values = registryRecordDaoExt.getMinMaxIdsForProcessing(registry.getId());
			minMaxIds[0] = values[0];
			minMaxIds[1] = values[1];

			log.info("Min and max are {}, {}", values[0], values[1]);
		}
		Long lowerBound = minMaxIds[0] + pager.getThisPageFirstElementNumber();
		Long upperBound = minMaxIds[0] + pager.getThisPageLastElementNumber();

		log.info("Bounds: [{}, {}]", lowerBound, upperBound);

		return registryRecordDao.listRecordsForProcessing(registry.getId(), lowerBound, upperBound);
	}

	/**
	 * Check if RegistryFile was loaded
	 *
	 * @param stub File stub
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	public boolean isLoaded(@NotNull Stub<FPFile> stub) {
		return registryFileDaoExt.isLoaded(stub.getId());
	}

	@Required
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	@Required
	public void setRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	@Required
	public void setRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}

	@Required
	public void setRegistryFileDaoExt(RegistryFileDaoExt registryFileDaoExt) {
		this.registryFileDaoExt = registryFileDaoExt;
	}

}
