package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.SpFileDao;
import org.flexpay.eirc.dao.RegistryRecordDaoExt;
import org.flexpay.eirc.dao.RegistryRecordDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.service.SpFileService;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Transactional (readOnly = true)
public class SpFileServiceImpl implements SpFileService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private SpFileDao spFileDao;
	private RegistryRecordDao registryRecordDao;
	private RegistryRecordDaoExt registryRecordDaoExt;

	/**
	 * Create SpFile
	 *
	 * @param spFile SpFile object
	 * @return created SpFile object
	 */
	@Transactional (readOnly = false)
	public SpFile create(SpFile spFile) throws FlexPayException {
		spFileDao.create(spFile);

		if (log.isDebugEnabled()) {
			log.debug("Created SpFile: " + spFile);
		}

		return spFile;
	}

	/**
	 * Read SpFile object by its unique id
	 *
	 * @param id SpFile key
	 * @return SpFile object, or <code>null</code> if object not found
	 */
	public SpFile read(Long id) {
		return spFileDao.read(id);
	}

	/**
	 * Update SpFile
	 *
	 * @param spFile SpFile to update for
	 * @return Updated SpFile object
	 * @throws FlexPayException if SpFile object is invalid
	 */
	@Transactional (readOnly = false)
	public SpFile update(SpFile spFile) throws FlexPayException {
		spFileDao.update(spFile);

		return spFile;
	}

	/**
	 * Get a list of available SpFiles
	 *
	 * @return List of SzFile
	 */
	public List<SpFile> getEntities() {
		return spFileDao.listSpFiles();
	}

	@Transactional (readOnly = false)
	public void delete(SpFile spFile) {
		spFileDao.delete(spFile);
	}

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
	public List<SpRegistry> getRegistries(SpFile spFile) {
		return spFileDao.listRegistries(spFile.getId());
	}

	/**
	 * Get registry records for processing
	 *
	 * @param registry Registry header
	 * @param pager Page
	 * @param minMaxIds cached minimum and maximum registry record keys
	 * @return list of records
	 */
	public List<RegistryRecord> getRecordsForProcessing(@NotNull Stub<SpRegistry> registry, Page<RegistryRecord> pager, Long[] minMaxIds) {

		// cache min-max rerecord ids
		if (minMaxIds[0] == null || minMaxIds[1] == null) {
			Long[] values = registryRecordDaoExt.getMinMaxIdsForProcessing(registry.getId());
			minMaxIds[0] = values[0];
			minMaxIds[1] = values[1];

			if (log.isInfoEnabled()) {
				log.info("Min and max are " + values[0] + ", " + values[1]);
			}
		}
		Long lowerBound = minMaxIds[0] + pager.getThisPageFirstElementNumber();
		Long upperBound = minMaxIds[0] + pager.getThisPageLastElementNumber();

		if (log.isInfoEnabled()) {
			log.info("Bounds: " + lowerBound + ", " + upperBound + ")");
		}

		return registryRecordDao.listRecordsForProcessing(registry.getId(), lowerBound, upperBound);
	}

	public void setSpFileDao(SpFileDao spFileDao) {
		this.spFileDao = spFileDao;
	}

	public void setRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	public void setRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}
}
