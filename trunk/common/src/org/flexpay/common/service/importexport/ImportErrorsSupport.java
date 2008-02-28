package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.ImportError;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper to work with import errors objects
 */
public class ImportErrorsSupport implements ApplicationContextAware {

	private ApplicationContext context;

	/**
	 * Set the ApplicationContext that this object runs in.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.context = applicationContext;
	}

	/**
	 * Find RawDataSource for ImportError object
	 *
	 * @param importError ImportError object
	 * @return RawDataSource if available
	 * @throws RuntimeException if no Spring bean exists for this error
	 */
	public RawDataSource getDataSource(ImportError importError) {
		return (RawDataSource) context.getBean(importError.getDataSourceBean(), RawDataSource.class);
	}

	/**
	 * Set data source bean name for ImportError
	 * TODO: cache bean names retrieval
	 *
	 * @param importError ImportError
	 * @param source	  RawDataSource
	 * @return <code>importError</code>
	 * @throws RuntimeException if no bean exists
	 */
	public ImportError setDataSourceBean(ImportError importError, RawDataSource source) {
		String[] beanNames = context.getBeanNamesForType(RawDataSource.class);
		for (String beanName : beanNames) {
			if (context.getBean(beanName) == source) {
				importError.setDataSourceBean(beanName);
				return importError;
			}
		}

		throw new RuntimeException("RawDataSource is invalid instance: " + source);
	}
}
