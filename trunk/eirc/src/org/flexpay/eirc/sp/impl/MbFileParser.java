package org.flexpay.eirc.sp.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

public abstract class MbFileParser implements FileParser {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final String LAST_FILE_STRING_BEGIN = "999999999";
	public static final String REGISTRY_FILE_ENCODING = "Cp866";
	public static final String FILE_CREATION_DATE_FORMAT = "ddMMyy";

	protected RegistryRecordStatus statusLoaded;

	protected RegistryService registryService;
	protected RegistryRecordService registryRecordService;
	protected RegistryTypeService registryTypeService;
	protected ServiceProviderService serviceProviderService;
	protected RegistryStatusService registryStatusService;
	protected RegistryArchiveStatusService registryArchiveStatusService;
	protected PropertiesFactory propertiesFactory;
	protected CorrectionsService correctionsService;
	protected Stub<DataSourceDescription> megabankSD;

	private MbFileValidator validator;
	private RegistryRecordStatusService registryRecordStatusService;

	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	public Registry parse(FPFile spFile) throws FlexPayException {

		if (validator != null) {
			if (!validator.validate(spFile)) {
				log.debug("Validation failed");
				return null;
			}
		} else {
			log.debug("Validator is null. Skipping validation");
		}

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		long beginTime = System.currentTimeMillis();

		File file = FPFileUtil.getFileOnServer(spFile);
		if (file == null) {
			log.debug("Incorrect spFile: can't find file on server (spFile.id = {})", spFile.getId());
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		statusLoaded = registryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
		if (statusLoaded == null) {
			throw new FlexPayException("Can't get registry record status \"loaded\" from database");
		}

		Registry ret = parseFile(spFile);

		long endTime = System.currentTimeMillis();
		long time = (endTime - beginTime) / 1000;

		log.info("Parsing file {} finished in {}", spFile.getOriginalName(), (time / 60 + "m " + time % 60 + "s"));

		return ret;

	}

	protected abstract Registry parseFile(@NotNull FPFile spFile) throws FlexPayException;

	public void setValidator(MbFileValidator validator) {
		this.validator = validator;
	}

	@Required
	public void setSpRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
		this.registryRecordStatusService = registryRecordStatusService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setSpRegistryStatusService(RegistryStatusService registryStatusService) {
		this.registryStatusService = registryStatusService;
	}

	@Required
	public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
		this.registryArchiveStatusService = registryArchiveStatusService;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setMegabankSD(DataSourceDescription megabankSD) {
		this.megabankSD = stub(megabankSD);
	}

}
