package org.flexpay.common.service.importexport;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;

import java.util.List;

public interface ImportErrorService {

	/**
	 * Get a list of ImportErrors
	 *
	 * @param pager Page
	 * @param clazz Objects type
	 * @return list of import errors
	 */
	List<ImportError> getImportErrors(Page<ImportError> pager, Class<? extends DomainObject> clazz);

	/**
	 * List import errors for data source
	 *
	 * @param pager Page
	 * @param ds DataSourceDescription Id
	 * @param clazz Import objects type
	 * @return list of errors
	 */
	List<ImportError> listDataSourceErrorsByType(Page<ImportError> pager, DataSourceDescription ds, Class<? extends DomainObject> clazz);

	/**
	 * List data source decriptions having import errors of specified type
	 *
	 * @param clazz Objects type
	 * @return list of data source descriptions
	 */
	List<DataSourceDescription> listDescriptions(Class<? extends DomainObject> clazz);

	/**
	 * Register new import error
	 *
	 * @param importError ImportError
	 */
	void addError(ImportError importError);

}
