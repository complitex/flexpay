package org.flexpay.common.service.importexport.impl;

import org.flexpay.common.dao.ImportErrorDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.ImportErrorService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class ImportErrorServiceImpl implements ImportErrorService {

	private ImportErrorDao errorDao;
	private ClassToTypeRegistry registry;

	/**
	 * Get a list of ImportErrors
	 *
	 * @param pager Page
	 * @param clazz Objects type
	 * @return list of import errors
	 */
    @Override
	public List<ImportError> getImportErrors(Page<ImportError> pager, Class<? extends DomainObject> clazz) {
		return errorDao.listErrors(pager, registry.getType(clazz));
	}

	/**
	 * List import errors for data source
	 *
	 * @param pager Page
	 * @param ds	DataSourceDescription Id
	 * @param clazz Import objects type
	 * @return list of errors
	 */
    @Override
	public List<ImportError> listDataSourceErrorsByType(Page<ImportError> pager, DataSourceDescription ds, Class<? extends DomainObject> clazz) {
		return errorDao.listDataSourceErrors(pager, ds.getId(), registry.getType(clazz));
	}

	/**
	 * List data source decriptions having import errors of specified type
	 *
	 * @param clazz Objects type
	 * @return list of data source descriptions
	 */
    @Override
	public List<DataSourceDescription> listDescriptions(Class<? extends DomainObject> clazz) {
		return errorDao.listDescriptions(registry.getType(clazz));
	}

	/**
	 * Register new import error
	 *
	 * @param importError ImportError
	 */
	@Transactional(readOnly = false)
    @Override
	public void addError(ImportError importError) {
		errorDao.create(importError);
	}

	@Required
	public void setErrorDao(ImportErrorDao errorDao) {
		this.errorDao = errorDao;
	}

	@Required
	public void setRegistry(ClassToTypeRegistry registry) {
		this.registry = registry;
	}
}
