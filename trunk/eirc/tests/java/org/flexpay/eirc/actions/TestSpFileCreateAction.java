package org.flexpay.eirc.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestSpFileCreateAction extends EircSpringBeanAwareTestCase {

	@Autowired
	protected FPFileService FPFileService;
	protected UploadFileAction uploadFileAction;

	@Autowired
	public void setUploadFileAction(@Qualifier ("spFileUploadAjaxAction") UploadFileAction uploadFileAction) {
		uploadFileAction.setUserPreferences(new UserPreferences());
		this.uploadFileAction = uploadFileAction;
	}

	@Test
	@Ignore
	public void testCreateSpFile() throws Throwable {
		FPFile newFile = createSpFile("org/flexpay/eirc/actions/sp/k0108.ree");
		deleteFile(newFile);
	}

	protected FPFile createSpFile(@NotNull @NonNls String spFile) throws Throwable {

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

		uploadFileAction.setUpload(tmpDataFile);
		uploadFileAction.setUploadFileName(name);

		assertEquals("Invalid Struts action result", ActionSupport.SUCCESS, uploadFileAction.execute());
		return uploadFileAction.getFpFile();
	}

	@SuppressWarnings ({"ResultOfMethodCallIgnored"})
	protected void deleteFile(FPFile file) {

		log.debug("Deleting registry file: {}", file);

		FPFileService.delete(file);
		uploadFileAction.getUpload().delete();

		log.debug("Deleted file!");
	}

}
