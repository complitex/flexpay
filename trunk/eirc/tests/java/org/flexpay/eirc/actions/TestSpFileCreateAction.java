package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class TestSpFileCreateAction extends SpringBeanAwareTestCase {

	private SpFileService fileService;
	private SpFileCreateAction fileCreateAction;

	protected void runTest() throws Throwable {
		testCreateSpFile();
	}

	public void testCreateSpFile() throws Throwable {
		SpFile newFile = createSpFile("org/flexpay/eirc/actions/sp/payments_100.44268.bin");
		deleteFile(newFile);
	}

	protected SpFile createSpFile(String spFile) throws Throwable {
		File tmpDataFile = File.createTempFile("sp_sample", ".txt");
		OutputStream os = null;
		InputStream is = null;
		try {
			os = new FileOutputStream(tmpDataFile);

			is = getClass().getClassLoader().getResourceAsStream(spFile);
			if (is == null) {
				fail("Cannot find source file");
			}
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
			tmpDataFile.delete();
		}

		fileCreateAction.setUpload(tmpDataFile);
		fileCreateAction.setUploadFileName("sp.txt");
		fileCreateAction.setSubmit("submit");

		assertEquals("Invalid Struts action result", "form", fileCreateAction.execute());
		return getLastFile();
	}

	private SpFile getLastFile() {
		List<SpFile> spFiles = fileService.getEntities();
		return spFiles.get(spFiles.size() - 1);
	}

	protected void deleteFile(SpFile file) {
		fileService.delete(file);
	}

	protected void prepareTestInstance() throws Exception {
		fileService = (SpFileService) applicationContext.getBean("spFileService");
		fileCreateAction = (SpFileCreateAction) applicationContext.getBean("spFileCreateAction");
	}
}
