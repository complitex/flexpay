package org.flexpay.eirc.actions;

import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpFileService;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSpFileAction extends TestSpFileCreateAction {

	@Autowired
	private SpFileAction fileAction;
	@Autowired
	private SpFileService fileService;
	@Autowired
	private SpRegistryDao spRegistryDao;

	@Ignore
	@Test
	public void testUploadFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(SpFile file) {
		for (SpRegistry registry : fileService.getRegistries(file)) {
			spRegistryDao.deleteRecordContainers(registry.getId());
			spRegistryDao.deleteRegistryContainers(registry.getId());
			spRegistryDao.deleteRecords(registry.getId());
			spRegistryDao.delete(registry);
		}
	}

	protected SpFile uploadFile(String fileName) throws Throwable {
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
		if (fileAction.getSpFileFormatException() != null) {
			throw fileAction.getSpFileFormatException();
		}
		return newFile;
	}
}
