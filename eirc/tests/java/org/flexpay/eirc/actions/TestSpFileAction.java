package org.flexpay.eirc.actions;

import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.common.process.ProcessManager;
import org.jetbrains.annotations.NonNls;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

public class TestSpFileAction extends TestSpFileCreateAction {

	@Autowired
	protected SpFileAction fileAction;
	@Autowired
	protected SpRegistryDao spRegistryDao;
	@Autowired
	protected ProcessManager processManager;

	@Test
	@NotTransactional
	public void testUploadFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadZipFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.zip");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadGZipFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt.gz");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(SpFile file) {
		for (SpRegistry registry : fileService.getRegistries(file)) {
			spRegistryDao.deleteQuittances(registry.getId());
			spRegistryDao.deleteRecordContainers(registry.getId());
			spRegistryDao.deleteRegistryContainers(registry.getId());
			spRegistryDao.deleteRecords(registry.getId());
			spRegistryDao.delete(registry);
		}
	}

	protected SpFile uploadFile(@NonNls String fileName) throws Throwable {
		SpFile newFile = createSpFile(fileName);

		fileAction.setSpFileId(newFile.getId());
		fileAction.setAction("loadToDb");

		try {
			assertEquals("Invalid Struts action result", "success", fileAction.execute());
		} catch (Exception e) {
			deleteRecords(newFile);
			deleteFile(newFile);
			throw e;
		}
		return newFile;
	}
}
