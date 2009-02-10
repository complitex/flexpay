package org.flexpay.bti.process;

import org.apache.commons.io.IOUtils;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.flexpay.bti.service.importexport.BuildingAttributesImporter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class BuildingAttributesImportJob extends Job {

	public static final String PARAM_FILE_ID = "fileId";
	public static final String PARAM_IMPORT_DATE = "importDate";

	private FPFileService fileService;
	private BuildingAttributesImporter attributesImporter;
	private BuildingAttributeDataProcessor attributeDataProcessor;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		File file = fileService.getFileFromFileSystem((Long) parameters.get(PARAM_FILE_ID));
		Date beginDate = (Date) parameters.get(PARAM_IMPORT_DATE);
		if (beginDate == null) {
			log.error("No import date parameter specified");
			return RESULT_ERROR;
		}

		Logger plog = ProcessLogger.getLogger(getClass());
		plog.info("Starting dumping attributes");
		List<BuildingAttributeData> datas = fetchData(file, parameters);
		for (BuildingAttributeData data : datas) {

			try {
				attributeDataProcessor.processData(beginDate, ApplicationConfig.getFutureInfinite(), data);
			} catch (Exception e) {
				plog.warn("Failed importing building attributes", e);
			}
		}

		plog.info("End dumping attributes");
		return RESULT_NEXT;
	}

	private List<BuildingAttributeData> fetchData(File file, Map<Serializable, Serializable> parameters)
		throws FlexPayException {

		InputStream is = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			is = new BufferedInputStream(new FileInputStream(file));
			return attributesImporter.readAttributes(is);
		} catch (Exception e) {
			throw new FlexPayException("Failed reading source file #" + parameters.get(PARAM_FILE_ID), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	@Required
	public void setAttributesImporter(BuildingAttributesImporter attributesImporter) {
		this.attributesImporter = attributesImporter;
	}

	@Required
	public void setAttributeDataProcessor(BuildingAttributeDataProcessor attributeDataProcessor) {
		this.attributeDataProcessor = attributeDataProcessor;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}
}
