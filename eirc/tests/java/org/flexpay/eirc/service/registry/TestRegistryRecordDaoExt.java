package org.flexpay.eirc.service.registry;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Buildings;

import java.util.List;

public class TestRegistryRecordDaoExt extends SpringBeanAwareTestCase {

	private SpRegistryRecordDaoExt recordDaoExt;
	private ClassToTypeRegistry classToTypeRegistry;

	private ImportErrorTypeFilter errorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
	private Page<SpRegistryRecord> pager = new Page<SpRegistryRecord>();

	private SpRegistry registry = new SpRegistry(9L);

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {

		testFilterNone();
		testFilterNoErrors();
		testFilterBuildingErrors();
		testFilterLoadedWithError();
	}

	private void testFilterNone() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	private void testFilterLoadedWithError() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(SpRegistryRecordStatus.LOADED_WITH_ERROR);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	private void testFilterNoErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Street.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	private void testFilterBuildingErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Buildings.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	protected void prepareTestInstance() throws Exception {
		recordDaoExt = (SpRegistryRecordDaoExt) applicationContext.getBean("spRegistryRecordDaoExt");
		classToTypeRegistry = (ClassToTypeRegistry) applicationContext.getBean("typeRegistryEirc");
	}
}