package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;

import java.util.List;

/**
 * ImportError persistence layer
 */
public interface ImportErrorDao extends GenericDao<ImportError, Long> {

	/**
	 * List import errors
	 *
	 * @param pager Page
	 * @param type Import objects type
	 * @return list of errors
	 */
	List<ImportError> listErrors(Page<ImportError> pager, int type);

	/**
	 * List import errors for data source
	 *
	 * @param pager Page
	 * @param dataSourceId DataSourceDescription Id
	 * @param type Import objects type
	 * @return list of errors
	 */
	List<ImportError> listDataSourceErrors(Page<ImportError> pager, Long dataSourceId, int type);

	/**
	 * List data source decriptions having import errors of specified type
	 *
	 * @param type Import object type
	 * @return list of data source descriptions
	 */
	List<DataSourceDescription> listDescriptions(int type);
}
