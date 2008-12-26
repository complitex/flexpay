package org.flexpay.sz.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
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

        FPFileService fpFileService = getFpFileService();
        @NonNls OutputStream os = response.getOutputStream();

        FPFile fpFile;

        try {
            fpFile = fpFileService.read(fileId);
        } catch (FlexPayException e) {
            log.error("Can't get file with id {}", fileId, e);
            os.write(("Can't get file with id " + fileId).getBytes());
            IOUtils.closeQuietly(os);
            return;
        }

        File file;
        if (fpFile == null) {
            log.error("File not found: id = {}", fileId);
            os.write(("File not found: id = " + fileId).getBytes());
            IOUtils.closeQuietly(os);
            return;
        } else {
            file = FPFileUtil.getFileOnServer(fpFile);
        }

		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fpFile.getOriginalName() + "\"");

		InputStream is = null;
		try {
			is = new DataInputStream(new FileInputStream(file));
			IOUtils.copyLarge(is, os);
		} catch (IOException e) {
			log.error("Error getting file {}", file, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	private FPFileService getFpFileService() {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		return (FPFileService) context.getBean("fpFileService");
	}

}
