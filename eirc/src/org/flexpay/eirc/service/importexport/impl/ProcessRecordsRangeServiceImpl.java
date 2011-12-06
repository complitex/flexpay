package org.flexpay.eirc.service.importexport.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryName;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessorTx;
import org.flexpay.eirc.service.importexport.ProcessRecordsRangeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import static org.flexpay.common.persistence.Stub.stub;

@Transactional(readOnly = true)
public class ProcessRecordsRangeServiceImpl implements ProcessRecordsRangeService {

	private HandleError handleError;
	private ServiceProviderFileProcessorTx processorTx;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private StreetService streetService;
	private OrganizationService organizationService;

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public ProcessingContext prepareContext(Registry registry) throws FlexPayException {
		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);

		ArrayStack filters = new ArrayStack();

        Town defaultTown = getDefaultTown();
        if (defaultTown == null) {
            throw new FlexPayException("Can not find default town in application config");
        }
        filters.push(new TownFilter(defaultTown.getId()));
        List<Street> townStreets = streetService.find(filters);

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);
		context.setNameStreetMap(nameObjsMap);

		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Organization sender = organizationService.readFull(props.getSenderStub());
		if (sender == null) {
			throw new IllegalStateException("Cannot find sender organization: #" + props.getSenderStub().getId());
		}
		props.setSender(sender);
		context.setSd(sender.sourceDescriptionStub());
		return context;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public boolean processRecords(Collection<RegistryRecord> records, ProcessingContext context) {
		for (RegistryRecord record : records) {
//			log.debug("Check record status");
			if (record.getRecordStatus().getCode() == RegistryRecordStatus.PROCESSED) {
				continue;
			}
//			log.debug("Processing record");
			try {
				context.setCurrentRecord(record);
//				log.debug("Current record set");
				processorTx.prepareRecordUpdates(context);
//				log.debug("Record updates prepared");
				if (context.getCurrentRecord() != null) {
					recordWorkflowManager.setNextSuccessStatus(record);
				}
			} catch (Throwable t) {
				try {
					log.debug("Try set next status for record with Id {}, current status {}", record.getId(), record.getRecordStatus());
					synchronized (context.getRegistry()) {
						handleError.handleError(t, context);
					}
				} catch (Exception e) {
					log.error("Inner error. Record Id is: " + record.getId(), e);
					return false;
				}
			}
		}
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	@Override
	public boolean doUpdate(ProcessingContext context) {
		try {
			processorTx.doUpdate(context);
			return true;
		} catch (Exception e) {
			log.error("Inner error", e);
		}
		return false;
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
