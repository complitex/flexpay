package org.flexpay.eirc.actions;

import org.flexpay.eirc.dao.SpRegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpFileService;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
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

	protected void deleteRecords(SpFile newFile) {
		for (SpRegistry registry : fileService.getRegistries(newFile)) {
			spRegistryDao.deleteRecords(registry.getId());
			spRegistryDao.delete(registry);
		}
	}

	protected SpFile uploadFile(String fileName) throws Throwable {
		SpFile newFile = createSpFile(fileName);

		fileAction.setSpFileId(newFile.getId());
		fileAction.setAction("loadToDb");

		assertEquals("Invalid Struts action result", "success", fileAction.execute());
		if (fileAction.getSpFileFormatException() != null) {
			throw fileAction.getSpFileFormatException();
		}
		return newFile;
	}
}
