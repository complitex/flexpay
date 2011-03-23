package org.flexpay.eirc.sp.impl;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.impl.validation.FileValidator;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public abstract class MbFileParser implements FileParser {

	protected Logger log = LoggerFactory.getLogger(getClass());

//	public static final String FOOTER_MARKER = "999999999";
//	public static final String REGISTRY_FILE_ENCODING = "Cp866";
//	public static final String FILE_CREATION_DATE_FORMAT = "ddMMyy";

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
	protected ServiceTypesMapper serviceTypesMapper;
	protected SPService spService;
	protected ConsumerService consumerService;

	protected RegistryRecordStatusService registryRecordStatusService;
    private ServiceValidationFactory serviceValidationFactory;
    private FileValidationSchema fileValidationSchema;
    protected LineParser lineParser;

	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<Registry> parse(FPFile spFile, Logger logger) throws FlexPayException {
        FileValidator validator = serviceValidationFactory.createFileValidator(fileValidationSchema, lineParser, logger);

        if (!validator.validate(spFile)) {
            log.debug("Validation failed");
            return null;
        }

		log.info("Starting parsing file: {}", spFile.getOriginalName());

		StopWatch watch = new StopWatch();
		watch.start();

		statusLoaded = registryRecordStatusService.findByCode(RegistryRecordStatus.LOADED);
		if (statusLoaded == null) {
			throw new FlexPayException("Can't get registry record status \"loaded\" from database");
		}

		List<Registry> registries = parseFile(spFile);
		watch.stop();

		log.info("Parsing file {} finished in {}", spFile, watch);

		return registries;
    }

    @Transactional (propagation = Propagation.NOT_SUPPORTED)
	@Override
	public List<Registry> parse(FPFile spFile) throws FlexPayException {
        return parse(spFile, null);
	}

    protected abstract List<Registry> parseFile(@NotNull FPFile spFile) throws FlexPayException;

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

	@Required
	public void setServiceTypesMapper(ServiceTypesMapper serviceTypesMapper) {
		this.serviceTypesMapper = serviceTypesMapper;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

    @Required
    public void setServiceValidationFactory(ServiceValidationFactory serviceValidationFactory) {
        this.serviceValidationFactory = serviceValidationFactory;
    }

    @Required
    public void setFileValidationSchema(FileValidationSchema fileValidationSchema) {
        this.fileValidationSchema = fileValidationSchema;
    }

    @Required
    public void setLineParser(LineParser lineParser) {
        this.lineParser = lineParser;
    }

}
