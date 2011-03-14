package org.flexpay.common.action;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.StringUtil.getFileExtension;
import static org.flexpay.common.util.StringUtil.getFileNameWithoutExtension;

public class FileDownloadServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(FileDownloadServlet.class);

	private boolean isInline = false;
	private FPFile file = new FPFile();
	private Long fileId = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		isInline = request.getParameter("inline") != null;

		String fileIdStr = getFileNameWithoutExtension(request.getRequestURI());
		log.debug("Request uri: {}", request.getRequestURI());
		log.debug("Request url: {}", request.getRequestURL());
		log.debug("QueryString: {}", request.getQueryString());
		log.debug("FileId: {}", fileIdStr);

		if (isNotBlank(fileIdStr)) {
			try {
				file.setId(Long.parseLong(fileIdStr));
			} catch (NumberFormatException ex) {
				log.debug("Invalid file.id parameter {}", fileIdStr);
			}
		}

		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		FPFileService fileService = (FPFileService) context.getBean("fpFileService");

		if (file.isNotNew()) {
			fileId = file.getId();
			file = fileService.read(stub(file));
		}

		log.debug("Downloading file: {}", file);

		response.setContentType(getContentType());
		response.setContentLength(file.getSize().intValue());
		response.setHeader("Content-Disposition", getContentDisposition());

		InputStream is = getFileStream();
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			IOUtils.copyLarge(is, os);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}

	public String getFileName() {
		return file != null && file.isNotNew() ? file.getOriginalName() : "error.txt";
	}

	public InputStream getFileStream() throws IOException {

		String error = null;
		if (file == null) {
			error = "No file id specified";
		} else if (file.isNew()) {
			error = "Invalid file id: " + String.valueOf(fileId);
		}

		if (error != null) {
			return new ByteArrayInputStream(error.getBytes());
		}

		return file.getInputStream();
	}

	public String getContentDisposition() throws UnsupportedEncodingException {
		String result = isInline ? "inline" : "attachment;filename=\"" + new String(getFileName().getBytes("UTF-8"), "ISO-8859-1") + "\"";

		log.debug("Result: {}", result);

		return result;
	}

	public String getContentType() {
		String result;
		String ext = getFileExtension(getFileName());
		if (".pdf".equalsIgnoreCase(ext)) {
			result = "application/pdf";
		} else if (".csv".equalsIgnoreCase(ext)) {
			result = "text/csv";
		} else if (".txt".equalsIgnoreCase(ext)) {
			result = "text/plain";
		} else if (".html".equalsIgnoreCase(ext)) {
			result = "text/html";
		} else {
			result = "application/unknown";
		}

		log.debug("Content-type: {}", result);

		return result;
	}

}
