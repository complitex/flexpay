package org.flexpay.eirc.actions;

import org.apache.log4j.Logger;
import org.flexpay.common.persistence.FlexPayFile;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.process.ProcessManager;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.flexpay.eirc.dao.RegistryDao;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.RegistryFileService;
import org.jetbrains.annotations.NonNls;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

public class TestSpFileAction extends TestSpFileCreateAction {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	protected SpFileAction fileAction;
	@Autowired
	protected RegistryDao registryDao;

	@Autowired
	@Qualifier("processManager")
	private ProcessManager processManager;
	@Autowired
	protected RegistryFileService registryFileService;

	@Test
	@NotTransactional
	public void testUploadFile() throws Throwable {
		FlexPayFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadZipFile() throws Throwable {
		FlexPayFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.zip");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadGZipFile() throws Throwable {
		FlexPayFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt.gz");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(FlexPayFile file) {

		if (log.isDebugEnabled()) {
			log.debug("Deleting registries of file: " + file);
		}

		for (SpRegistry registry : registryFileService.getRegistries(file)) {
			deleteQuittances(registry.getId());
			deleteContainers(registry.getId());
			registryDao.deleteRegistryContainers(registry.getId());
			registryDao.deleteRecords(registry.getId());
			registryDao.delete(registry);
		}

		log.debug("Deleted!");
	}

	private void deleteQuittances(Long registryId) {
		String sql = "delete q " +
					 "from eirc_registries_tbl r " +
					 "left join eirc_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	private void deleteContainers(Long registryId) {
		String sql = "delete c " +
					 "from eirc_registries_tbl r " +
					 "left join eirc_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_registry_record_containers_tbl c on rr.id=c.record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	protected FlexPayFile uploadFile(@NonNls String fileName) throws Throwable {
		FlexPayFile newFile = createSpFile(fileName);

		fileAction.setSpFileId(newFile.getId());
		fileAction.setAction("loadToDb");

		try {
			if (log.isDebugEnabled()) {
				log.debug("Starting upload file: " + newFile);
			}
			assertEquals("Invalid Struts action result", "redirectSuccess", fileAction.execute());

			processManager.join(fileAction.getProcessId());

			assertTrue("File is not loaded", registryFileService.isLoaded(stub(newFile)));
			log.debug("Uploaded!");
		} catch (Throwable e) {
			deleteRecords(newFile);
			deleteFile(newFile);
			throw e;
		}

		return newFile;
	}
}
