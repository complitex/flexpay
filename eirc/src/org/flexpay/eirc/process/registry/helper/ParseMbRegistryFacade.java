package org.flexpay.eirc.process.registry.helper;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.parsing.ParserParameterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.process.handler.TaskHandler.getProcessInstanceId;

@Transactional(readOnly = true)
public class ParseMbRegistryFacade implements ParseRegistryFacade {

	protected final Logger log = LoggerFactory.getLogger(getClass());

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
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	@Override
	public String parse(Map<String, Object> parameters) throws FlexPayException {

		// prepare process logger
		ProcessLogger.setThreadProcessId(getProcessInstanceId(parameters));

		Logger processLog = ProcessLogger.getLogger(getClass());

		Long spFileId = (Long) parameters.get(ParseRegistryConstants.PARAM_FILE_ID);
		FPFile spFile = fpFileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Inner error");
			log.error("Can't get spFile from DB (id = {})", spFileId);
			return TaskHandler.RESULT_ERROR;
		}
		if (parameters.containsKey(ParseRegistryConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORDS)) {
			flushNumberRegistryRecords = (Long)parameters.get(ParseRegistryConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORDS);
		}
		Registry infoRegistry;
		Long totalLineNum = 0L;
		Long totalRecordsNum = 0L;
		Long currentCharPoint = 0L;
		if (parameters.containsKey(ParseRegistryConstants.PARAM_REGISTRY_ID)) {
			Long registryId = (Long)parameters.get(ParseRegistryConstants.PARAM_REGISTRY_ID);
			infoRegistry = registryService.read(new Stub<Registry>(registryId));
			if (infoRegistry == null) {
				processLog.error("Inner error");
				log.error("Registry {} did not find in DB", registryId);
				return TaskHandler.RESULT_ERROR;
			}
			totalLineNum = (Long)parameters.get(ParseRegistryConstants.PARAM_NUMBER_PRECESSED_LINES);
			if (totalLineNum == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", ParseRegistryConstants.PARAM_NUMBER_PRECESSED_LINES);
				return TaskHandler.RESULT_ERROR;
			}
			totalRecordsNum = (Long)parameters.get(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
			if (totalRecordsNum == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
				return TaskHandler.RESULT_ERROR;
			}
			currentCharPoint = (Long)parameters.get(ParseRegistryConstants.PARAM_CURRENT_CHAR_POINT);
			if (currentCharPoint == null) {
				processLog.error("Inner error");
				log.error("Parameter '{}' did not find", ParseRegistryConstants.PARAM_CURRENT_CHAR_POINT);
				return TaskHandler.RESULT_ERROR;
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

			parameters.put(ParseRegistryConstants.PARAM_REGISTRY_ID, infoRegistry.getId());
			parameters.put(ParseRegistryConstants.PARAM_NUMBER_PRECESSED_LINES, parserParameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM));
			parameters.put(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS, parserParameters.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM));
			parameters.put(ParseRegistryConstants.PARAM_CURRENT_CHAR_POINT, currentCharPoint);

			if (newCharPoint >= 0) {
				processLog.info("Parsed {} lines", parameters.get(ParseRegistryConstants.PARAM_NUMBER_PRECESSED_LINES));
				return TaskHandler.RESULT_NEXT;
			}
			infoRegistry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));
			registryService.update(infoRegistry);

			processLog.info("Registry parse completed, total lines {}, total records {}",
					new Object[]{parameters.get(ParseRegistryConstants.PARAM_NUMBER_PRECESSED_LINES),
								parameters.get(ParseRegistryConstants.PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS)});

			return ParseRegistryConstants.RESULT_END;
		} catch (Exception e) {
			processLog.error("Inner error");
			log.error("Parse error", e);
		}
		return TaskHandler.RESULT_ERROR;
	}

	public void initRegistry(FPFile spFile, Registry registry) {
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
