package org.flexpay.eirc.service.registry;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.RegistryRecordDao;
import org.flexpay.eirc.dao.RegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.RegistryRecordStatus;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestRegistryRecordDaoExt extends SpringBeanAwareTestCase {

	@Autowired
	protected RegistryRecordDaoExt recordDaoExt;
	@Autowired
	protected RegistryRecordDao recordDao;
	private ClassToTypeRegistry classToTypeRegistry;

	private ImportErrorTypeFilter errorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
	private Page<RegistryRecord> pager = new Page<RegistryRecord>();

	private SpRegistry registry = new SpRegistry(9L);

	@Autowired
	public void setClassToTypeRegistry(@Qualifier ("typeRegistryEirc")ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Test
	public void testFilterNone() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterLoadedWithError() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(RegistryRecordStatus.PROCESSED_WITH_ERROR);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterNoErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Street.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterBuildingErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(BuildingAddress.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testSelectMultipleRecordsById() {
		recordDaoExt.findRecords(9L, CollectionUtils.list(1L, 2L));
	}

	@Test
	public void testGetMaxMinForProcessing() {
		recordDaoExt.getMinMaxIdsForProcessing(9L);
	}
}
