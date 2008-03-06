package org.flexpay.eirc.dao;

import java.util.List;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;

public interface SpFileDao extends GenericDao<SpFile, Long>{

	/**
	 * List files
	 *
	 * @return list of files
	 */
	List<SpFile> listSpFiles();

	/**
	 * List registries for file
	 *
	 * @param fileId SpFile id
	 * @return list of registries
	 */
	List<SpRegistry> listRegistries(Long fileId);

	/**
	 * List registry records 
	 *
	 * @param registryId Registry header id
	 * @return list of registrie records
	 */
	List<SpRegistryRecord> listRegistryRecords(Long registryId);
}
