package org.flexpay.eirc.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;
import org.springframework.test.annotation.NotTransactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestSpFileCreateAction extends SpringBeanAwareTestCase {

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
	@NotTransactional
	public void testCreateSpFile() throws Throwable {
		FPFile newFile = createSpFile("org/flexpay/eirc/actions/sp/k0108.ree");
		deleteFile(newFile);
	}

	protected FPFile createSpFile(@NotNull @NonNls String spFile) throws Throwable {

		GrantedAuthority[] authorities = {new GrantedAuthorityImpl("ROLE_TEST")};
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
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

	protected void deleteFile(FPFile file) {

		log.debug("Deleting registry file: {}", file);

		FPFileService.delete(file);
		uploadFileAction.getUpload().delete();

		log.debug("Deleted file!");
	}

}
