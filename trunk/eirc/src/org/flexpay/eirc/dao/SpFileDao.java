package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;

import java.util.List;

public interface SpFileDao extends GenericDao<SpFile, Long> {

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
}
