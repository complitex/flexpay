package org.flexpay.eirc.process.registry;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.sp.SpFileReader;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class ProcessRegistryMessageActionHandler extends FlexPayActionHandler {
    private static final String RESULT_EMPTY = "empty";
    private static final String RESULT_HEADER = "header";
    private static final String RESULT_RECORD = "record";
    private static final String RESULT_FOOTER = "footer";
    private static final String RESULT_END = "end";

    public static final String PARAM_MESSAGE_FIELDS = "messageFields";
	public static final String PARAM_REGISTRY_RECORDS = "registryRecords";
	public static final String PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS = "numberProcessedRegistryRecords";

	private RegistryService registryService;
	private RegistryRecordService registryRecordService;
	private RegistryWorkflowManager registryWorkflowManager;
	private Long flushNumberRegistryRecords;

	@SuppressWarnings ({"unchecked"})
    @Override
    public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");

		List<SpFileReader.Message> listMessage = (List<SpFileReader.Message>)parameters.get(GetRegistryMessageActionHandler.PARAM_MESSAGES);
		if (listMessage == null || listMessage.size() <= 0) {
			return RESULT_NEXT;
		}

        SpFileReader.Message message = listMessage.get(0);
		listMessage.remove(0);
		if (message == null) {
			return finalizeRegistry(parameters)? RESULT_END: RESULT_ERROR;

		}

        String messageValue = message.getBody();
		if (StringUtils.isEmpty(messageValue)) {
			return RESULT_EMPTY;
		}

		Integer messageType = message.getType();

		List<String> messageFieldList = StringUtil.splitEscapable(
				messageValue, Operation.RECORD_DELIMITER, Operation.ESCAPE_SYMBOL);

        parameters.put(PARAM_MESSAGE_FIELDS, messageFieldList);

		processLog.debug("Message fields: {}", messageFieldList);

		if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_HEADER)) {
			return RESULT_HEADER;
		} else if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_RECORD)) {
			return flushRecordStack(parameters)? RESULT_RECORD: RESULT_ERROR;
		} else if (messageType.equals(SpFileReader.Message.MESSAGE_TYPE_FOOTER)) {
			return RESULT_FOOTER;
		} else if (messageType.equals(-1)) {
			return finalizeRegistry(parameters)? RESULT_NEXT: RESULT_ERROR;
		}
        return RESULT_EMPTY;
    }

	private boolean flushRecordStack(Map<String, Object> parameters) {
		return flushRecordStack(parameters, null, false);
	}

	@SuppressWarnings ({"unchecked"})
	@Transactional(readOnly = false)
	private boolean flushRecordStack(Map<String, Object> parameters, Registry registry, boolean finalize) {
		log.debug("Flush data");
		List<RegistryRecord> records = (List<RegistryRecord>)parameters.get(PARAM_REGISTRY_RECORDS);
		if (records != null && (records.size() >= flushNumberRegistryRecords || finalize)) {
			if (registry == null) {
				registry = getRegistry(parameters);
				if (registry == null) {
					return false;
				}
			}

			Long recordCounter = (Long)parameters.get(PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);
			if (recordCounter == null) {
				recordCounter = 0L;
			}
			recordCounter += records.size();
			parameters.put(PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS, recordCounter);

			try {
				for (RegistryRecord record : records) {
					record.setRegistry(registry);
					registryRecordService.create(record);
				}
			} catch (FlexPayException e) {
				processLog.error("Registry record did not save to database");
				log.error("Can`t flush records", e);
				return false;
			}

			records.clear();
			log.debug("Flushing data");
		}
		return true;
	}

	private boolean finalizeRegistry(Map<String, Object> parameters) {
		Registry registry = getRegistry(parameters);
		if (registry == null) {
			return false;
		}

		if (!flushRecordStack(parameters, registry, true)) {
			return false;
		}

		processLog.debug("Finalize registry");

		Long recordCounter = (Long)parameters.get(PARAM_NUMBER_PROCESSED_REGISTRY_RECORDS);

		if (!registry.getRecordsNumber().equals(recordCounter)) {
			processLog.error("Registry records number error, expected: " +
											  registry.getRecordsNumber() + ", found: " + recordCounter);
			return false;
		}

		try {
			registryWorkflowManager.setNextSuccessStatus(registry);
			return true;
		} catch (TransitionNotAllowed transitionNotAllowed) {
			processLog.error("Does not finalize registry", transitionNotAllowed);
		}
		return false;
	}

	@Nullable
	private Registry getRegistry(Map<String, Object> parameters) {
		Long registryId = (Long)parameters.get(ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
		if (registryId == null) {
			processLog.error("Can`t get {} from parameters", ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
			log.error("Can`t get {} from parameters", ProcessHeaderActionHandler.PARAM_REGISTRY_ID);
			return null;
		}
		return registryService.read(new Stub<Registry>(registryId));
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
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setFlushNumberRegistryRecords(Long flushNumberRegistryRecords) {
		this.flushNumberRegistryRecords = flushNumberRegistryRecords;
	}
}
