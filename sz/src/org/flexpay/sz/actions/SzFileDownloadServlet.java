package org.flexpay.sz.actions;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.ServiceHolder;
import org.flexpay.sz.service.SzFileService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class SzFileDownloadServlet extends HttpServlet {

	private Logger log = Logger.getLogger(getClass());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String szFileIdParam = request.getParameter("szFileId");
		Long szFileId = new Long(szFileIdParam);
		SzFileService szFileService = ServiceHolder.getSzFileService();

		SzFile szFile = szFileService.readFull(szFileId);
		File szDataRoot = ApplicationConfig.getInstance().getSzDataRoot();
		File file;
		if (szFile != null) {
			file = szFile.getResponseFile(szDataRoot);
		} else {
			OutputStream os = response.getOutputStream();
			os.write(("File not found: " + szFileId).getBytes());
			IOUtils.closeQuietly(os);
			return;
		}

		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""
												  + szFile.getRequestFileName() + "\"");

		InputStream is = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			is = new DataInputStream(new FileInputStream(file));
			IOUtils.copyLarge(is, os);
		} catch (IOException e) {
			log.error("Error getting file " + file, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
}
