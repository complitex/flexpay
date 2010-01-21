package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.parsing.ParserParameterConstants;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional (readOnly = true)
public class IterateMBRegistryActionHandler extends FlexPayActionHandler {
	private static final String RESULT_END = "end";

	public static final String PARAM_FILE_ID = "fileId";
	public static final String PARAM_REGISTRY_ID = "registryId";
	public static final String PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS = "numberProcessedRegistryRecords";
	public static final String PARAM_NUMBER_PRECESSED_LINES = "numberProcessedLines";
	public static final String PARAM_CURRENT_CHAR_POINT = "currentCharPoint";

	public static final String PARAM_FLUSH_NUMBER_REGISTRY_RECORDS = "flushNumberRegistryRecords";

	private FPFileService fpFileService;
	private RegistryService registryService;
	protected RegistryTypeService registryTypeService;
	protected RegistryStatusService registryStatusService;
	protected RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryFPFileTypeService registryFPFileTypeService;

	private FileParser mbFileParser;

	private String moduleName;

	private Long flushNumberRegistryRecords = 50L;

	@SuppressWarnings ({"unchecked"})
	@Override
	@Transactional(readOnly = false)
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		Long spFileId = (Long) parameters.get(PARAM_FILE_ID);
		FPFile spFile = fpFileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Inner error");
			log.error("Can't get spFile from DB (id = {})", spFileId);
			return RESULT_ERROR;
		}
		if (parameters.containsKey(PARAM_FLUSH_NUMBER_REGISTRY_RECORDS)) {
			flushNumberRegistryRecords = (Long)parameters.get(PARAM_FLUSH_NUMBER_REGISTRY_RECORDS);
		}
		Registry infoRegistry;
		Long totalLineNum = 0L;
		Long totalRecordsNum = 0L;
		Long currentCharPoint = 0L;
		if (parameters.containsKey(PARAM_REGISTRY_ID)) {
			Long registryId = (Long)parameters.get(PARAM_REGISTRY_ID);
			infoRegistry = registryService.read(new Stub<Registry>(registryId));
			if (infoRegistry == null) {
				processLog.error("Inner error");
				log.error("Registry {} did not find in DB", registryId);
				return RESULT_ERROR;
			}
			totalLineNum = (Long)parameters.get(PARAM_NUMBER_PRECESSED_LINES);
			if (totalLineNum == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", PARAM_NUMBER_PRECESSED_LINES);
				return RESULT_ERROR;
			}
			totalRecordsNum = (Long)parameters.get(PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
			if (totalRecordsNum == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
				return RESULT_ERROR;
			}
			currentCharPoint = (Long)parameters.get(PARAM_CURRENT_CHAR_POINT);
			if (currentCharPoint == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", PARAM_CURRENT_CHAR_POINT);
				return RESULT_ERROR;
			}
		} else {
			infoRegistry = new Registry();
			initRegistry(spFile, infoRegistry);
		}
		List<Registry> registries = CollectionUtils.list(infoRegistry);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(spFile.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
		} catch (IOException e) {
			throw new FlexPayException("Error open file " + spFile, e);
		}
		try {
			Map<String, Object> parserParameters = CollectionUtils.map();
			parserParameters.put(ParserParameterConstants.PARAM_REGISTRIES, registries);
			parserParameters.put(ParserParameterConstants.PARAM_TOTAL_LINE_NUM, totalLineNum);
			parserParameters.put(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM, totalRecordsNum);
			parserParameters.put(ParserParameterConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORD, flushNumberRegistryRecords);

			reader.skip(currentCharPoint.longValue());
			long newCharPoint = (long) mbFileParser.iterateParseFile(reader, parserParameters);
			currentCharPoint += newCharPoint;

			parameters.put(PARAM_REGISTRY_ID, infoRegistry.getId());
			parameters.put(PARAM_NUMBER_PRECESSED_LINES, parserParameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM));
			parameters.put(PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS, parserParameters.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM));
			parameters.put(PARAM_CURRENT_CHAR_POINT, currentCharPoint);

			if (newCharPoint >= 0) {
				return RESULT_NEXT;
			}
			infoRegistry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registryService.update(infoRegistry);

			return RESULT_END;
		} catch (Exception e) {
			processLog.error("Inner error");
			log.error("Parse error", e);
		}
		return RESULT_ERROR;
	}

	private void initRegistry(FPFile spFile, Registry registry) {
		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_INFO));
		registry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));
		registry.setModule(fpFileService.getModuleByName(moduleName));
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setRegistryStatusService(RegistryStatusService registryStatusService) {
		this.registryStatusService = registryStatusService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
		this.registryFPFileTypeService = registryFPFileTypeService;
	}

	@Required
	public void setMbFileParser(FileParser mbFileParser) {
		this.mbFileParser = mbFileParser;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
