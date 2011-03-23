package org.flexpay.eirc.action;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.util.impl.CommonTestRegistryUtil;
import org.flexpay.eirc.action.spfile.SpFileAction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

import static junit.framework.Assert.assertNull;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.junit.Assert.*;

public class TestSpFileAction extends TestSpFileCreateAction {

	@Autowired
	protected SpFileAction fileAction;
	@Autowired
	protected CommonTestRegistryUtil registryUtil;

	@Autowired
	@Qualifier ("processManager")
	protected ProcessManager processManager;
	@Autowired
	protected RegistryFileService registryFileService;

	@Test
	@NotTransactional
	public void testUploadFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/action/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadFailedFile() throws Throwable {
		uploadFileWithError("org/flexpay/eirc/action/sp/ree_failed.txt");
	}

	@Test
	@NotTransactional
	public void testUploadZipFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/action/sp/ree.zip");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadGZipFile() throws Throwable {
		FPFile newFile = uploadFile("org/flexpay/eirc/action/sp/ree.txt.gz");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(FPFile file) {

		log.debug("Deleting registries of file: {}. Registries: {}", new Object[]{file, registryFileService.getRegistries(file)});

		for (Registry registry : registryFileService.getRegistries(file)) {
			deleteQuittances(registry.getId());
			registryUtil.delete(registry);
		}

		log.debug("Deleted!");
	}

    @Override
    protected void deleteFile(FPFile file) {
        FPFileService.deleteFromFileSystem(file);
    }

    private void deleteQuittances(Long registryId) {
		String sql = "delete q " +
					 "from common_registries_tbl r " +
					 "left join common_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	protected FPFile uploadFile(String fileName) throws Throwable {
		FPFile newFile = createSpFile(fileName);

		fileAction.setSpFile(newFile);
		fileAction.setAction("loadToDb");

		try {
			log.debug("Starting upload file: {}", newFile);
			assertEquals("Invalid Struts action result", FPActionSupport.REDIRECT_SUCCESS, fileAction.execute());

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

    protected void uploadFileWithError(String fileName) throws Throwable {
		FPFile newFile = createSpFile(fileName);

		fileAction.setSpFile(newFile);
		fileAction.setAction("loadToDb");

		try {
			log.debug("Starting upload file: {}", newFile);
			assertEquals("Invalid Struts action result", FPActionSupport.REDIRECT_ERROR, fileAction.execute());

			assertNull("ProcessId is not null", fileAction.getProcessId());

			assertFalse("File is loaded", registryFileService.isLoaded(stub(newFile)));
		} catch (Throwable e) {
			deleteRecords(newFile);
			deleteFile(newFile);
			throw e;
		}

		super.deleteFile(newFile);
	}
}
