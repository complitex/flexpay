package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.SpFileDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.SpFileService;
import org.springframework.transaction.annotation.Transactional;
import org.jetbrains.annotations.NonNls;

import java.util.List;

@Transactional (readOnly = true)
public class SpFileServiceImpl implements SpFileService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private SpFileDao spFileDao;

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
	 * @return list of records
	 */
	public List<SpRegistryRecord> getRecordsForProcessing(SpRegistry registry, Page<SpRegistryRecord> pager) {
		return spFileDao.listRecordsForProcessing(registry.getId(), pager);
	}

	public void setSpFileDao(SpFileDao spFileDao) {
		this.spFileDao = spFileDao;
	}
}
