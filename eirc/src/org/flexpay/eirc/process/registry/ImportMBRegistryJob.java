package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.registry.ProcessRegistryVariableInstance;
import org.flexpay.eirc.service.ProcessRegistryVariableInstanceService;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.parsing.ParserParameterConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImportMBRegistryJob extends Job {

	private static final String RESULT_END = "end";

	public static final String PARAM_FILE_ID = "fileId";

	public static final String PARAM_FLUSH_NUMBER_REGISTRY_RECORDS = "flushNumberRegistryRecords";

	private FPFileService fpFileService;
	private RegistryService registryService;
	protected RegistryTypeService registryTypeService;
	protected RegistryStatusService registryStatusService;
	protected RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryFPFileTypeService registryFPFileTypeService;
	private ProcessRegistryVariableInstanceService processRegistryVariableInstanceService;

	private FileParser mbFileParser;

	private String moduleName;

	private Long flushNumberRegistryRecords = 50L;

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		log.debug("start action");

		Logger processLog = ProcessLogger.getLogger(getClass());

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

		ProcessRegistryVariableInstance variable = processRegistryVariableInstanceService.findVariable(getProcessId());

		if (variable != null && variable.getRegistry() != null) {
			infoRegistry = registryService.read(Stub.stub(variable.getRegistry()));
		} else {
			infoRegistry = new Registry();
			createProcessRegistry(spFile, infoRegistry);
		}
		List<Registry> registries = CollectionUtils.list(infoRegistry);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(spFile.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
		} catch (IOException e) {
			throw new FlexPayException("Error open file " + spFile, e);
		}
		try {
			if (variable != null && variable.getCharPoint() != null) {
				reader.skip(variable.getCharPoint());
			}
			if (variable == null) {
				variable = new ProcessRegistryVariableInstance();
				variable.setCharPoint(0L);
				variable.setProcessedCountLines(0);
				variable.setProcessedCountRecords(0);
				variable.setProcessId(getProcessId());
				variable.setRegistry(infoRegistry);
				variable = processRegistryVariableInstanceService.create(variable);
			}

			parseFile(parameters, processLog, variable, registries, reader);

			processLog.info("Registry parse completed, total lines {}, total records {}",
					new Object[]{variable.getProcessedCountLines(),
							variable.getProcessedCountRecords()});
		} catch (Exception e) {
			processLog.error("Inner error");
			log.error("Parse error", e);
		}
		return RESULT_ERROR;
	}

	@Transactional(readOnly = true)
	private void parseFile(Map<Serializable, Serializable> parameters, Logger processLog, ProcessRegistryVariableInstance variable, List<Registry> registries, BufferedReader reader) throws FlexPayException {

		Map<String, Object> parserParameters = CollectionUtils.map();
		parserParameters.put(ParserParameterConstants.PARAM_REGISTRIES, registries);
		parserParameters.put(ParserParameterConstants.PARAM_TOTAL_LINE_NUM, Long.valueOf(variable.getProcessedCountLines()));
		parserParameters.put(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM, Long.valueOf(variable.getProcessedCountRecords()));
		parserParameters.put(ParserParameterConstants.PARAM_FLUSH_NUMBER_REGISTRY_RECORD, flushNumberRegistryRecords);

		long newCharPoint;
		do {
			newCharPoint = iterateParseFile(parameters, variable, reader, parserParameters);

			processLog.info("Parsed {} lines", variable.getProcessedCountLines());


		} while (newCharPoint >= 0);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private long iterateParseFile(Map<Serializable, Serializable> parameters, ProcessRegistryVariableInstance variable, BufferedReader reader, Map<String, Object> parserParameters) throws FlexPayException {

		long newCharPoint;
		newCharPoint = (long) mbFileParser.iterateParseFile(reader, parserParameters);

		variable.setCharPoint(variable.getCharPoint() + newCharPoint);
		variable.setProcessedCountLines(((Long)parameters.get(ParserParameterConstants.PARAM_TOTAL_LINE_NUM)).intValue());
		variable.setProcessedCountRecords(((Long)parameters.get(ParserParameterConstants.PARAM_TOTAL_RECORD_NUM)).intValue());

		processRegistryVariableInstanceService.update(variable);
		return newCharPoint;
	}

	@Transactional(readOnly = false)
	private ProcessRegistryVariableInstance createProcessRegistry(FPFile spFile, Registry registry) throws FlexPayException {

		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_INFO));
		registry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADING));
		registry.setModule(fpFileService.getModuleByName(moduleName));

		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.LOADED));

		registryService.create(registry);

		ProcessRegistryVariableInstance variable = new ProcessRegistryVariableInstance();
		variable.setRegistry(registry);
		variable.setProcessId(getProcessId());
		variable.setProcessedCountLines(0);
		variable.setProcessedCountRecords(0);
		variable.setCharPoint(0L);
		processRegistryVariableInstanceService.create(variable);

		return variable;
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

	@Required
	public void setProcessRegistryVariableInstanceService(ProcessRegistryVariableInstanceService processRegistryVariableInstanceService) {
		this.processRegistryVariableInstanceService = processRegistryVariableInstanceService;
	}
}
