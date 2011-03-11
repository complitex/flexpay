package org.flexpay.eirc.process.registry;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.*;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


public class ProcessHeaderActionHandler extends FlexPayActionHandler {
	public static final String PARAM_REGISTRY_ID = "registryId";
	public static final String PARAM_SERVICE_PROVIDER_ID = "serviceProviderId";

	private FPFileService fileService;

	private RegistryService registryService;
	private EircRegistryService eircRegistryService;
	private RegistryTypeService registryTypeService;
	private RegistryArchiveStatusService registryArchiveStatusService;
	private RegistryFPFileTypeService registryFPFileTypeService;
	private RegistryWorkflowManager registryWorkflowManager;

	private PropertiesFactory propertiesFactory;

	private OrganizationService organizationService;
	private ServiceProviderService providerService;

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;
	private ClassToTypeRegistry typeRegistry;

	private String moduleName;

	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");
		
		Long spFileId = (Long) parameters.get(GetRegistryMessageActionHandler.PARAM_FILE_ID);
		FPFile spFile = fileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Can't get spFile from DB (id = " + spFileId + ")");
			return RESULT_ERROR;
		}
		List<String> messageFieldList = (List<String>)parameters.get(ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
		if (messageFieldList == null) {
			processLog.error("Can`t get {} from parameters", ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
			return RESULT_ERROR;
		}
		Registry registry = processHeader(spFile, messageFieldList);
		if (registry == null) {
			return RESULT_ERROR;
		}
		parameters.put(PARAM_REGISTRY_ID, registry.getId());
		parameters.put(PARAM_SERVICE_PROVIDER_ID, ((EircRegistryProperties)registry.getProperties()).getServiceProvider().getId());
		processLog.debug("Create registry {}. Add it to process parameters", registry.getId());
		return RESULT_NEXT;
	}

	@Transactional(readOnly = false)
	private Registry processHeader(FPFile spFile, List<String> messageFieldList) {
		if (messageFieldList.size() < 10) {
			processLog.error("Message header error, invalid number of fields: {}, expected at least 10", messageFieldList.size());
			return null;
		}

		processLog.info("Adding header: {}", messageFieldList);

		DateFormat dateFormat = new SimpleDateFormat(ParseRegistryConstants.DATE_FORMAT);

		Registry newRegistry = new Registry();
		newRegistry.setModule(fileService.getModuleByName(moduleName));
		newRegistry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		try {
			registryWorkflowManager.setInitialStatus(newRegistry);
			newRegistry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), spFile);
			int n = 0;
			newRegistry.setRegistryNumber(Long.valueOf(messageFieldList.get(++n)));
			String value = messageFieldList.get(++n);
			RegistryType registryType = registryTypeService.read(Long.valueOf(value));
			if (registryType == null) {
				processLog.error("Unknown registry type field: {}", value);
				return null;
			}
			newRegistry.setRegistryType(registryType);
			newRegistry.setRecordsNumber(Long.valueOf(messageFieldList.get(++n)));
			newRegistry.setCreationDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setFromDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setTillDate(dateFormat.parse(messageFieldList.get(++n)));
			newRegistry.setSenderCode(Long.valueOf(messageFieldList.get(++n)));
			newRegistry.setRecipientCode(Long.valueOf(messageFieldList.get(++n)));
			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				newRegistry.setAmount(new BigDecimal(amountStr));
			}
			if (messageFieldList.size() > n) {
				if (!parseContainers(newRegistry, messageFieldList.get(++n))) {
					return null;
				}
			}

			processLog.info("Creating new registry: {}", newRegistry);

			EircRegistryProperties props = (EircRegistryProperties) propertiesFactory.newRegistryProperties();
			newRegistry.setProperties(props);

			Organization recipient = setRecipient(newRegistry, props);
			if (recipient == null) {
				processLog.error("Failed processing registry header, recipient not found: #{}", newRegistry.getRecipientCode());
				return null;
			}
			Organization sender = setSender(newRegistry, props);
			if (sender == null) {
				processLog.error("Failed processing registry header, sender not found: #{}", newRegistry.getSenderCode());
				return null;
			}
			processLog.info("Recipient: {}\n sender: {}", recipient, sender);

			if (!validateProvider(newRegistry)) {
				return null;
			}
			ServiceProvider provider = getProvider(newRegistry);
			if (provider == null) {
				processLog.error("Failed processing registry header, provider not found: #{}", newRegistry.getSenderCode());
				return null;
			}
			props.setServiceProvider(provider);

			if (!validateRegistry(newRegistry)) {
				return null;
			}

			return registryService.create(newRegistry);
		} catch (NumberFormatException e) {
			processLog.error("Header parse error", e);
		} catch (ParseException e) {
			processLog.error("Header parse error", e);
		} catch (TransitionNotAllowed e) {
			processLog.error("Header parse error", e);
		} catch (FlexPayException e) {
			processLog.error("Header parse error", e);
		}
		return null;
	}

	private boolean validateProvider(Registry registry) {
		if (registry.getRegistryType().isPayments()) {
			Stub<Organization> recipient = new Stub<Organization>(registry.getRecipientCode());
			if (recipient.sameId(ApplicationConfig.getSelfOrganization())) {
				processLog.error("Expected service provider recipient, but recieved eirc code");
				return false;
			}
		}
		return true;
	}

	private ServiceProvider getProvider(Registry registry) {
		// for payments registry assume recipient is a service provider
		if (registry.getRegistryType().isPayments()) {
			return providerService.getProvider(new Stub<Organization>(registry.getRecipientCode()));
		}
		return providerService.getProvider(new Stub<Organization>(registry.getSenderCode()));
	}

	private Organization setSender(Registry registry, EircRegistryProperties props) {

		processLog.debug("Fetching sender via code={}", registry.getSenderCode());
		Organization sender = findOrgByRegistryCorrections(registry, registry.getSenderCode());
		if (sender == null) {
			sender = organizationService.readFull(props.getSenderStub());
		}
		props.setSender(sender);
		return sender;
	}

	private Organization setRecipient(Registry registry, EircRegistryProperties props) {
		Organization recipient;
		if (registry.getRecipientCode() == 0) {
			processLog.debug("Recipient is EIRC, code=0");
			recipient = organizationService.readFull(ApplicationConfig.getSelfOrganizationStub());
		} else {
			processLog.debug("Fetching recipient via code={}", registry.getRecipientCode());
			recipient = findOrgByRegistryCorrections(registry, registry.getRecipientCode());
			if (recipient == null) {
				recipient = organizationService.readFull(props.getRecipientStub());
			}
		}
		props.setRecipient(recipient);
		return recipient;
	}

	@Nullable
	private Organization findOrgByRegistryCorrections(Registry registry, Long code) {

		for (RegistryContainer container : registry.getContainers()) {
			String data = container.getData();
			processLog.debug("Candidate: {}", data);
			if (data.startsWith("502"+Operation.CONTAINER_DATA_DELIMITER)) {
				List<String> datum = StringUtil.splitEscapable(
								data, Operation.CONTAINER_DATA_DELIMITER, Operation.ESCAPE_SYMBOL);
				// skip if correction is not for Organization type
				if (Integer.parseInt(datum.get(1)) != typeRegistry.getType(Organization.class)) {
					continue;
				}
				// skip if correction is not for the object with requested code
				if (Long.parseLong(datum.get(2)) != code) {
					continue;
				}

				if (StringUtils.isNotBlank(datum.get(4)) && "1".equals(datum.get(5))) {
					Stub<Organization> stub = correctionsService.findCorrection(
							datum.get(4), Organization.class, masterIndexService.getMasterSourceDescriptionStub());
					if (stub == null) {
						throw new IllegalStateException("Expected master correction for organization, " +
														"but not found: " + data);
					}
					processLog.debug("Found organization by master correction: {}", datum.get(4));
					Organization org = organizationService.readFull(stub);
					if (org == null) {
						throw new IllegalStateException("Existing master correction for organization " +
														"references nowhere: " + data);
					}
					return org;
				}
			}
		}

		return null;
	}

	private boolean parseContainers(Registry registry, String containersData) {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SYMBOL);
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > ParseRegistryConstants.MAX_CONTAINER_SIZE) {
				processLog.error("Too long container found: {}", data);
				return false;
			}
			registry.addContainer(new RegistryContainer(data));
		}
		return true;
	}

	/**
	 * Check if registry header is valid
	 *
	 * @param registry Registry to validate
	 * @return false if registry header validation fails
	 */
	@Transactional (readOnly = true)
	private boolean validateRegistry(Registry registry) {
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Registry persistent = eircRegistryService.getRegistryByNumber(registry.getRegistryNumber(), props.getSenderStub());
		return persistent == null;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setEircRegistryService(EircRegistryService eircRegistryService) {
		this.eircRegistryService = eircRegistryService;
	}

	@Required
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
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
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setProviderService(ServiceProviderService providerService) {
		this.providerService = providerService;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
