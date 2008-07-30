package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class TestSpFileCreateAction extends SpringBeanAwareTestCase {

	@Autowired
	protected SpFileService fileService;
	@Autowired
	protected SpFileCreateAction fileCreateAction;

	@Test
	@Ignore
	@NotTransactional
	public void testCreateSpFile() throws Throwable {
		SpFile newFile = createSpFile("org/flexpay/eirc/actions/sp/k0108.ree");
		deleteFile(newFile);
	}

	protected SpFile createSpFile(@NotNull @NonNls String spFile) throws Throwable {
		String name = StringUtil.getFileName(spFile);
		String extension = StringUtil.getFileExtension(name);
		File tmpDataFile = File.createTempFile(name, extension);
		tmpDataFile.deleteOnExit();
		OutputStream os = null;
		InputStream is = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			os = new FileOutputStream(tmpDataFile);

			is = getFileStream(spFile);
			if (is == null) {
				fail("Cannot find source file " + spFile);
			}
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}

		fileCreateAction.setUpload(tmpDataFile);
		fileCreateAction.setUploadFileName(name);
		fileCreateAction.setSubmitted("submitted");

		assertEquals("Invalid Struts action result", SpFileCreateAction.INPUT, fileCreateAction.execute());
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
