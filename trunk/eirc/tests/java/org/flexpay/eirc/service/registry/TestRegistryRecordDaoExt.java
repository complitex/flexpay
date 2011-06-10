package org.flexpay.eirc.service.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestRegistryRecordDaoExt extends EircSpringBeanAwareTestCase {

	@Autowired
	protected RegistryRecordDaoExt recordDaoExt;
	@Autowired
	protected RegistryRecordDao recordDao;
	@Autowired
	private ClassToTypeRegistry classToTypeRegistry;
	@Autowired
	private RegistryRecordStatusService recordStatusService;

	private ImportErrorTypeFilter errorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
	private Page<RegistryRecord> pager = new Page<RegistryRecord>();

	private Stub<Registry> registry = new Stub<Registry>(45L);

	@Test
	public void testFilterNone() {
		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);
		recordStatusFilter.setSelectedId(-1L);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterProcessedWithError() {

		errorTypeFilter.setSelectedType(ImportErrorTypeFilter.TYPE_ALL);

		RegistryRecordStatus status = recordStatusService.findByCode(RegistryRecordStatus.PROCESSED_WITH_ERROR);
		assertNotNull("No PROCESSED_WITH_ERROR status found", status);
		recordStatusFilter.setSelected(stub(status));

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterStreetErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(Street.class));
		recordStatusFilter.setSelectedId(-1L);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testFilterBuildingErrors() {
		errorTypeFilter.setSelectedType(classToTypeRegistry.getType(BuildingAddress.class));
		recordStatusFilter.setSelectedId(-1L);

		List<RegistryRecord> records = recordDaoExt.filterRecords(
				registry.getId(), errorTypeFilter, recordStatusFilter, pager);

		assertNotNull("Records list is null", records);
	}

	@Test
	public void testSelectMultipleRecordsById() {
		recordDaoExt.findRecords(registry.getId(), CollectionUtils.list(1L, 2L));
	}

	@Test
	public void testGetMaxMinForProcessing() {
		recordDaoExt.getMinMaxIdsForProcessing(registry.getId());
	}

	@Test
	public void testGetRecordsForExport() {
		FetchRange range = new FetchRange();
		recordDao.listRecordsForExport(registry.getId(), range);
	}

	@Test
	public void testFindRecordsQuery() {

		final Long statusId = 4L;

		StopWatch watch = new StopWatch();
		watch.start();
		List<?> ids = jpaTemplate.executeFind(new JpaCallback<Object>() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createNativeQuery("select id from common_registry_records_tbl " +
											  "	use index (I_registry_status, I_registry_errortype) " +
											  "where registry_id=? and record_status_id=? " +
											  "limit 40, 20")
						.setParameter(1, registry.getId())
						.setParameter(2, statusId)
						.getResultList();
			}
		});
		watch.stop();
		assertFalse("No records found", ids.isEmpty());
		System.out.println("Listing status 4 took " + watch);
	}

	@Test
	public void testFindRecords2Query() {

		final Long statusId = 2L;
		final int typeApartment = classToTypeRegistry.getType(Apartment.class);

		StopWatch watch = new StopWatch();
		watch.start();
		List<?> ids = jpaTemplate.executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.createNativeQuery("select id from common_registry_records_tbl " +
											  "	use index (I_registry_status, I_registry_errortype) " +
											  "where registry_id=? and record_status_id=? and import_error_type=? " +
											  "limit 40, 20")
						.setParameter(1, registry.getId())
						.setParameter(2, statusId)
						.setParameter(3, typeApartment)
						.getResultList();
			}
		});
		watch.stop();
		assertFalse("No records found", ids.isEmpty());
		System.out.println("Listing status 2 and error apartment took " + watch);
	}
}
