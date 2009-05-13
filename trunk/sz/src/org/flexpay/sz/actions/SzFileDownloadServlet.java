package org.flexpay.sz.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class SzFileDownloadServlet extends HttpServlet {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	public void doGet(@NonNls HttpServletRequest request, @NonNls HttpServletResponse response) throws IOException {

		Long fileId = Long.parseLong(request.getParameter("szFileId"));
		Boolean isReq = Boolean.parseBoolean(request.getParameter("req"));

		SzFileService szFileService = getSzFileService();
        @NonNls OutputStream os = response.getOutputStream();

        SzFile szFile;

		szFile = szFileService.readFull(fileId);

		if (szFile == null) {
			log.error("File not found: id = {}", fileId);
			os.write(("File not found: id = " + fileId).getBytes());
			IOUtils.closeQuietly(os);
			return;
		}

		FPFile fpFile;
		if (isReq) {
			fpFile = szFile.getUploadedFile();
		} else {
			fpFile = szFile.getFileToDownload();
		}

        if (fpFile == null) {
            log.error("File not found: id = {}", fileId);
            os.write(("File not found: id = " + fileId).getBytes());
            IOUtils.closeQuietly(os);
            return;
        }

		File file = FPFileUtil.getFileOnServer(fpFile);

		response.setContentType("multipart/form-data");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fpFile.getOriginalName() + "\"");

		InputStream is = null;
		try {
			is = new FileInputStream(file);
			IOUtils.copyLarge(is, os);
		} catch (IOException e) {
			log.error("Error getting file " + file, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	private SzFileService getSzFileService() {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		return (SzFileService) context.getBean("szFileService");
	}

}
