package org.flexpay.eirc.process.registry;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.PropertiesFactory;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.sp.RegistryFormatException;
import org.flexpay.eirc.sp.RegistryUtil;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class ProcessRecordActionHandler extends FlexPayActionHandler {
	private PropertiesFactory propertiesFactory;
	private ConsumerService consumerService;

	private RegistryRecordWorkflowManager recordWorkflowManager;

	@SuppressWarnings ({"unchecked"})
	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");
		List<String> messageFieldList = (List<String>)parameters.get(ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
		if (messageFieldList == null) {
			processLog.error("Can`t get {} from parameters", ProcessRegistryMessageActionHandler.PARAM_MESSAGE_FIELDS);
			return RESULT_ERROR;
		}
		Long serviceProviderId = (Long)parameters.get(ProcessHeaderActionHandler.PARAM_SERVICE_PROVIDER_ID);
		if (serviceProviderId == null) {
			processLog.error("Can`t get {} from parameters", ProcessHeaderActionHandler.PARAM_SERVICE_PROVIDER_ID);
			return RESULT_ERROR;
		}

		Stub<ServiceProvider> serviceProviderStub = new Stub<ServiceProvider>(serviceProviderId);
		RegistryRecord record = processRecord(messageFieldList, serviceProviderStub);
		if (record == null) {
			return RESULT_ERROR;
		}
		List<RegistryRecord> records = (List<RegistryRecord>)parameters.get(ProcessRegistryMessageActionHandler.PARAM_REGISTRY_RECORDS);
		if (records == null) {
			records = list();
		}
		records.add(record);
		if (!parameters.containsKey(ProcessRegistryMessageActionHandler.PARAM_REGISTRY_RECORDS)) {
			parameters.put(ProcessRegistryMessageActionHandler.PARAM_REGISTRY_RECORDS, records);
		}

		return RESULT_NEXT;
	}

	private RegistryRecord processRecord(List<String> messageFieldList, Stub<ServiceProvider> serviceProviderStub) {
		if (messageFieldList.size() < 10) {
			processLog.error("Message record error, invalid number of fields: {}", messageFieldList.size());
			return null;
		}

		RegistryRecord record = new RegistryRecord();
		record.setProperties(propertiesFactory.newRecordProperties());
		try {
			processLog.info("adding record: '{}'", StringUtils.join(messageFieldList, '-'));
			int n = 1;
			record.setServiceCode(messageFieldList.get(++n));
			record.setPersonalAccountExt(messageFieldList.get(++n));

			EircRegistryRecordProperties recordProps = (EircRegistryRecordProperties) record.getProperties();
			Service service = consumerService.findService(serviceProviderStub, record.getServiceCode());
			if (service == null) {
				processLog.warn("Unknown service code: {}", record.getServiceCode());
			}
			recordProps.setService(service);

			// setup consumer address
			String addressStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(addressStr)) {
				List<String> addressFieldList = StringUtil.splitEscapable(
						addressStr, Operation.ADDRESS_DELIMITER, Operation.ESCAPE_SYMBOL);

				if (addressFieldList.size() != 6) {
					throw new RegistryFormatException(
							String.format("Address group '%s' has invalid number of fields %d",
									addressStr, addressFieldList.size()));
				}
				record.setTownName(addressFieldList.get(0));
				record.setStreetType(addressFieldList.get(1));
				record.setStreetName(addressFieldList.get(2));
				record.setBuildingNum(addressFieldList.get(3));
				record.setBuildingBulkNum(addressFieldList.get(4));
				record.setApartmentNum(addressFieldList.get(5));
			}

			// setup person first, middle, last names
			String fioStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(fioStr)) {
				List<String> fields = RegistryUtil.parseFIO(fioStr);
				record.setLastName(fields.get(0));
				record.setFirstName(fields.get(1));
				record.setMiddleName(fields.get(2));
			}

			// setup operation date
			DateFormat dateFormat = new SimpleDateFormat(ParseRegistryConstants.DATE_FORMAT);
			record.setOperationDate(dateFormat.parse(messageFieldList.get(++n)));

			// setup unique operation number
			String uniqueOperationNumberStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(uniqueOperationNumberStr)) {
				record.setUniqueOperationNumber(Long.valueOf(uniqueOperationNumberStr));
			}

			// setup amount
			String amountStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(amountStr)) {
				record.setAmount(new BigDecimal(amountStr));
			}

			// setup containers
			String containersStr = messageFieldList.get(++n);
			if (StringUtils.isNotEmpty(containersStr)) {
				record.setContainers(parseContainers(record, containersStr));
			}

			// setup record status
			recordWorkflowManager.setInitialStatus(record);

			return record;
		} catch (NumberFormatException e) {
			processLog.error("Record number parse error", e);
		} catch (ParseException e) {
			processLog.error("Record parse error", e);
		} catch (RegistryFormatException e) {
			processLog.error("Record number parse error", e);
		} catch (TransitionNotAllowed transitionNotAllowed) {
			processLog.error("Record number parse error", transitionNotAllowed);
		} catch (FlexPayException e) {
			processLog.error("Record number parse error", e);
		}
		return null;
	}

	private List<RegistryRecordContainer> parseContainers(RegistryRecord record, String containersData)
			throws RegistryFormatException {

		List<String> containers = StringUtil.splitEscapable(
				containersData, Operation.CONTAINER_DELIMITER, Operation.ESCAPE_SYMBOL);
		List<RegistryRecordContainer> result = new ArrayList<RegistryRecordContainer>(containers.size());
		int n = 0;
		for (String data : containers) {
			if (StringUtils.isBlank(data)) {
				continue;
			}
			if (data.length() > ParseRegistryConstants.MAX_CONTAINER_SIZE) {
				throw new RegistryFormatException("Too long container found: " + data);
			}
			RegistryRecordContainer container = new RegistryRecordContainer();
			container.setOrder(n++);
			container.setRecord(record);
			container.setData(data);
			result.add(container);
		}

		return result;
	}

	@Required
	public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
		this.propertiesFactory = propertiesFactory;
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}
}
