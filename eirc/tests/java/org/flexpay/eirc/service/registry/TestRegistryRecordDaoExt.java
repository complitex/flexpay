package org.flexpay.eirc.service.registry;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.dao.SpRegistryRecordDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public class TestRegistryRecordDaoExt extends SpringBeanAwareTestCase {

	@Autowired
	private SpRegistryRecordDaoExt recordDaoExt;
	@Autowired
	private SpRegistryRecordDao recordDao;
	private ClassToTypeRegistry classToTypeRegistry;

	private ImportErrorTypeFilter errorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
	private Page<SpRegistryRecord> pager = new Page<SpRegistryRecord>();

	private SpRegistry registry = new SpRegistry(9L);

	@Autowired
	public void setClassToTypeRegistry(@Qualifier("typeRegistryEirc") ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Test
	public void testFilterNone() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterLoadedWithError() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedStatus(SpRegistryRecordStatus.PROCESSED_WITH_ERROR);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterNoErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Street.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterBuildingErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Buildings.class));
		recordStatusFilter.setSelectedStatus(ImportErrorTypeFilter.TYPE_ALL);

		List<SpRegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testSelectMultipleRecordsById() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		recordDaoExt.findRecords(9L, ids);
	}
}
