package org.flexpay.eirc.service.registry;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
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
	@Autowired
	private ClassToTypeRegistry classToTypeRegistry;

	private ImportErrorTypeFilter errorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
	private Page<RegistryRecord> pager = new Page<RegistryRecord>();

	private Registry registry = new Registry(9L);

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

	@Test
	public void testGetRecordsForExport() {
		FetchRange range = new FetchRange();
		recordDao.listRecordsForExport(1L, range);
	}
}
