package org.flexpay.eirc.process.registry;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TemporaryName;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessorTx;
import org.flexpay.eirc.service.importexport.EircImportConsumerDataTx;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import static org.flexpay.common.persistence.Stub.stub;

public class ProcessRecordsRangeActionHandler extends FlexPayActionHandler {
	
	private static final int DEFAULT_RANGE = 50;

	private HandleError handleError;
	private RegistryFileService registryFileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private ServiceProviderFileProcessorTx processorTx;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private StreetService streetService;
	private OrganizationService organizationService;

	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");
		
		Long registryId = (Long)parameters.get(StartRegistryProcessingActionHandler.REGISTRY_ID);
		if (registryId == null) {
			log.error("Can not find '{}' in process parameters", StartRegistryProcessingActionHandler.REGISTRY_ID);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}
		Registry registry = registryService.read(new Stub<Registry>(registryId));
		if (registry == null) {
			log.error("Can not find registry '{}'", registryId);
			processLog.error("Inner error");
			return RESULT_ERROR;
		}

		List<Long> recordIds = (List<Long>)parameters.get(StartRegistryProcessingActionHandler.RECORD_IDS);
		FetchRange range = (FetchRange)parameters.get(HasMoreRecordActionHandler.RANGE);

		Collection<RegistryRecord> records;
		if (recordIds != null) {
			int rangeSize = DEFAULT_RANGE;
			if (range != null) {
				rangeSize = range.getPageSize();
			}
			List<Long> subRecordIds = CollectionUtils.listSlice(recordIds, 0, rangeSize);
			recordIds.removeAll(subRecordIds);
			records = registryRecordService.findObjects(registry, CollectionUtils.set(subRecordIds));
		} else {
			processLog.info("Fetching next page {}", range);
			records = registryFileService.getRecordsForProcessing(stub(registry), range);
		}
		ProcessingContext context = prepareContext(registry);

		if (!processRecords(records, context)) {
			return RESULT_ERROR;
		}
		try {
			processorTx.doUpdate(context);
		} catch (Exception e) {
			log.error("Inner error", e);
			return RESULT_ERROR;
		}
		if (range != null && recordIds == null) {
			range.nextPage();
		}

		return RESULT_NEXT;
	}

	public ProcessingContext prepareContext(Registry registry) throws FlexPayException {
		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);

		Town defaultTown = getDefaultTown();
		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(defaultTown.getId()));
		List<Street> townStreets = streetService.find(filters);

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		context.setNameStreetMap(nameObjsMap);

		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Organization sender = organizationService.readFull(props.getSenderStub());
		if (sender == null) {
			throw new IllegalStateException("Cannot find sender organization: #" + props.getSenderStub().getId());
		}
		context.setSd(sender.sourceDescriptionStub());
		return context;
	}

	public boolean processRecords(Collection<RegistryRecord> records, ProcessingContext context) {
		for (RegistryRecord record : records) {
			if (record.getRecordStatus().getCode() == RegistryRecordStatus.PROCESSED) {
				continue;
			}
			try {
				context.setCurrentRecord(record);
				processorTx.prepareRecordUpdates(context);
				if (context.getCurrentRecord() != null) {
					recordWorkflowManager.setNextSuccessStatus(record);
				}
			} catch (Throwable t) {
				try {
					log.debug("Try set next status for record with Id {}, current status {}", record.getId(), record.getRecordStatus());
					handleError.handleError(t, context);
				} catch (Exception e) {
					log.error("Inner error. Record Id is: " + record.getId(), e);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Build mapping from object names to objects themself
	 *
	 * @param ntds List of objects
	 * @return mapping
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if language configuration is invalid
	 */
	@SuppressWarnings ({"unchecked"})
	protected <NTD extends NameTimeDependentChild> Map<String, List<NTD>> initializeNamesToObjectsMap(List<NTD> ntds)
			throws FlexPayException {

		Map<String, List<NTD>> stringNTDMap = new HashMap<String, List<NTD>>(ntds.size());
		for (NTD object : ntds) {
			TemporaryName tmpName = (TemporaryName) object.getCurrentName();
			if (tmpName == null) {
				log.error("No current name for object: {}", object);
				continue;
			}
			Translation defTranslation = getDefaultLangTranslation(tmpName.getTranslations());
			String name = defTranslation.getName().toLowerCase();
			List<NTD> val = stringNTDMap.containsKey(name) ?
							stringNTDMap.get(name) : new ArrayList<NTD>();
			val.add(object);
			stringNTDMap.put(name, val);
		}

		return stringNTDMap;
	}

	private Translation getDefaultLangTranslation(Collection<? extends Translation> translations) {

		Long defaultLangId = org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage().getId();
		for (Translation translation : translations) {
			if (stub(translation.getLang()).getId().equals(defaultLangId)) {
				return translation;
			}
		}

		throw new IllegalArgumentException("No default lang translation found");
	}

	@Required
	public void setHandleError(HandleError handleError) {
		this.handleError = handleError;
	}

	@Required
	public void setRegistryFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
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
	public void setProcessorTx(ServiceProviderFileProcessorTx processorTx) {
		this.processorTx = processorTx;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
