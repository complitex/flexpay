package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class TestSpFileCreateAction extends SpringBeanAwareTestCase {

	@Autowired
	private SpFileService fileService;
	@Autowired
	private SpFileCreateAction fileCreateAction;

	@Test
	@Ignore
	@NotTransactional
	public void testCreateSpFile() throws Throwable {
		SpFile newFile = createSpFile("org/flexpay/eirc/actions/sp/k0108.ree");
		deleteFile(newFile);
	}

	protected SpFile createSpFile(String spFile) throws Throwable {
		File tmpDataFile = File.createTempFile("sp_sample", ".txt");
		tmpDataFile.deleteOnExit();
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
		fileCreateAction.getUpload().delete();

		if (file.getRequestFile() != null) {
			file.getRequestFile().delete();
		}
	}
}
