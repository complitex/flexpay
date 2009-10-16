package org.flexpay.eirc.actions;

import org.flexpay.common.dao.registry.RegistryDao;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.RegistryFileService;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.flexpay.eirc.actions.spfile.SpFileAction;
import org.jetbrains.annotations.NonNls;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

public class TestSpFileAction extends TestSpFileCreateAction {

	@Autowired
	protected SpFileAction fileAction;
	@Autowired
	protected RegistryDao registryDao;

	@Autowired
	@Qualifier ("processManager")
	protected ProcessManager processManager;
	@Autowired
	protected RegistryFileService registryFileService;

	@Test
	@NotTransactional
	public void testUploadFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadZipFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.zip");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadGZipFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt.gz");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(FPFile file) {

		log.debug("Deleting registries of file: {}", file);

		for (Registry registry : registryFileService.getRegistries(file)) {
			deleteQuittances(registry.getId());
			deleteContainers(registry.getId());
			registryDao.deleteRegistryContainers(registry.getId());
			registryDao.deleteRecordProperties(registry.getId());
			registryDao.deleteRecords(registry.getId());
			registryDao.delete(registry);
		}

		log.debug("Deleted!");
	}

	private void deleteQuittances(Long registryId) {
		String sql = "delete q " +
					 "from common_registries_tbl r " +
					 "left join common_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	private void deleteContainers(Long registryId) {
		String sql = "delete c " +
					 "from common_registries_tbl r " +
					 "left join common_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join common_registry_record_containers_tbl c on rr.id=c.record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	protected FPFile uploadFile(@NonNls String fileName) throws Throwable {
		FPFile newFile = createSpFile(fileName);

		fileAction.setSpFile(newFile);
		fileAction.setAction("loadToDb");

		try {
			log.debug("Starting upload file: {}", newFile);
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
