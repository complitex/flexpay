package org.flexpay.eirc.process.registry;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.service.importexport.ProcessRecordsRangeService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class ProcessRecordsRangeActionHandler extends FlexPayActionHandler {
	
	private static final int DEFAULT_RANGE = 50;

	private RegistryFileService registryFileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private ProcessRecordsRangeService processRecordsRangeService;

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
		ProcessingContext context = processRecordsRangeService.prepareContext(registry);

		if (!processRecordsRangeService.processRecords(records, context)) {
			return RESULT_ERROR;
		}
		if (!processRecordsRangeService.doUpdate(context)) {
			return RESULT_ERROR;
		}
		if (range != null && recordIds == null) {
			range.nextPage();
		}

		return RESULT_NEXT;
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
	public void setProcessRecordsRangeService(ProcessRecordsRangeService processRecordsRangeService) {
		this.processRecordsRangeService = processRecordsRangeService;
	}
}
