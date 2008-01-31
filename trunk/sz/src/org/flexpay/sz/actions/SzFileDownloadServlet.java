package org.flexpay.sz.actions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.ServiceHolder;
import org.flexpay.sz.service.SzFileService;

public class SzFileDownloadServlet extends HttpServlet {

	private static final int BUFSIZE = 100;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String szFileIdParam = request.getParameter("szFileId");
		Long szFileId = new Long(szFileIdParam);
		SzFileService szFileService = ServiceHolder.getSzFileService();

		SzFile szFile = szFileService.readFull(szFileId);
		File szDataRoot = ApplicationConfig.getInstance().getSzDataRoot();
		File file = null;
		if (szFile != null) {
			file = szFile.getResponseFile(szDataRoot);
		}

		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ szFile.getRequestFileName() + "\"");

		byte[] bbuf = new byte[BUFSIZE];
		int length = 0;
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new DataInputStream(new FileInputStream(file));
			os = response.getOutputStream();
			while ((is != null) && ((length = is.read(bbuf)) != -1)) {
				os.write(bbuf, 0, length);
			}
		} finally {

			is.close();
			os.flush();
			os.close();
		}
	}

}
