package org.flexpay.tc.actions.tariff;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class TariffCalcRulesFileDownloadServlet extends HttpServlet {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	public void doGet(@NonNls HttpServletRequest request, @NonNls HttpServletResponse response) throws IOException {

		Long fileId = Long.parseLong(request.getParameter("rulesFileId"));

		TariffCalculationRulesFileService tariffCalculationRulesFileService = getTariffCalculationRulesFileService();
		@NonNls OutputStream os = response.getOutputStream();

		TariffCalculationRulesFile rulesFile;

		rulesFile = tariffCalculationRulesFileService.read(new Stub<TariffCalculationRulesFile>(fileId));

		if (rulesFile == null) {
			log.error("File not found: id = {}", fileId);
			os.write(("File not found: id = " + fileId).getBytes());
			IOUtils.closeQuietly(os);
			return;
		}

		FPFile fpFile = rulesFile.getFile();

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

	private TariffCalculationRulesFileService getTariffCalculationRulesFileService() {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		return (TariffCalculationRulesFileService) context.getBean("tariffCalculationRulesFileService");
	}

}
