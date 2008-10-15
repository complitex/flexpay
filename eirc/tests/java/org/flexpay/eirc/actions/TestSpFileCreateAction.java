package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.service.SpFileService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestSpFileCreateAction extends SpringBeanAwareTestCase {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	protected SpFileService fileService;
	protected SpFileCreateAction fileCreateAction;

	@Autowired
	public void setFileCreateAction(@Qualifier ("spFileCreateAction") SpFileCreateAction fileCreateAction) {
		fileCreateAction.setUserPreferences(new UserPreferences());
		this.fileCreateAction = fileCreateAction;
	}

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
		return fileCreateAction.getSpFile();
	}

	protected void deleteFile(SpFile file) {

		if (log.isDebugEnabled()) {
			log.debug("Deleting registry file: " + file);
		}

		fileService.delete(file);
		fileCreateAction.getUpload().delete();

		if (file.getRequestFile() != null) {
			file.getRequestFile().delete();
		}

		log.debug("Deleted file!");
	}
}
