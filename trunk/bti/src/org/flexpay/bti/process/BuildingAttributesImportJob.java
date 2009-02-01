package org.flexpay.bti.process;

import org.apache.commons.io.IOUtils;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributeDataProcessor;
import org.flexpay.bti.service.importexport.BuildingAttributesImporter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildingAttributesImportJob extends Job {

	public static final String PARAM_FILE_ID = "fileId";
	public static final String PARAM_IMPORT_DATE = "importDate";

	private FPFileService fileService;
	private BuildingAttributesImporter attributesImporter;
	private BuildingAttributeDataProcessor attributeDataProcessor;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		File file = fileService.getFileFromFileSystem((Long) parameters.get(PARAM_FILE_ID));

		List<BuildingAttributeData> datas = null;
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			datas = attributesImporter.readAttributes(is);
		} catch (Exception e) {
			throw new FlexPayException("Failed reading source file #" + parameters.get(PARAM_FILE_ID), e);
		} finally {
			IOUtils.closeQuietly(is);
		}

		for (BuildingAttributeData data : datas) {

			try {
//				attributeDataProcessor.processData();
			} catch (Exception e) {

			}
		}

		return RESULT_NEXT;
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
