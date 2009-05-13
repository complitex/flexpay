package org.flexpay.eirc.service.exchange;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.eirc.actions.TestSpFileAction;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.service.EircRegistryService;
import org.flexpay.payments.service.SPService;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

import java.util.List;

public class TestServiceProviderFileProcessor extends TestSpFileAction {

	@Autowired
	protected RegistryProcessor registryProcessor;
	@Autowired
	protected ExchangeHelper exchangeHelper;
	@Autowired
	@Qualifier ("spService")
	protected SPService spService;
	@Autowired
	protected RegistryRecordService registryRecordService;
	@Autowired
	protected EircRegistryService eircRegistryService;

	@Test
	@NotTransactional
	public void testProcessOpenSubAccountsRegistrySmall() throws Throwable {
		FPFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open_2_small.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@NotTransactional
	public void testProcessNotKnownServiceCode() throws Throwable {
		FPFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_open_small_unknown_service.txt");

		try {
			registryProcessor.processFile(file);

			List<Registry> registries = eircRegistryService.findObjects(new Page<Registry>(), file.getId());
			assertEquals("Expected 1 registry", 1, registries.size());
			Registry registry = registries.get(0);
			List<RegistryRecord> records = registryRecordService.listRecords(
					registry, new ImportErrorTypeFilter(),
					new RegistryRecordStatusFilter(), new Page<RegistryRecord>());
			assertFalse("No records in registry", records.isEmpty());
			RegistryRecord record = records.get(0);
			ImportError error = record.getImportError();
			assertNotNull("Import error expected", error);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}

	@Test
	@NotTransactional
	public void testProcessQuittancesSmallRegistry() throws Throwable {
		FPFile file = uploadFile("org/flexpay/eirc/actions/sp/ree_quittances_small.txt");

		try {
			registryProcessor.processFile(file);
		} catch (FlexPayExceptionContainer c) {
			for (Exception e : c.getExceptions()) {
				e.printStackTrace();
			}
			throw c;
		} finally {
			deleteRecords(file);
			deleteFile(file);
		}
	}
}
