package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataUtil;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;

import java.util.List;

/**
 * Close service provider personal account
 */
public class CloseAccountOperation extends AbstractChangePersonalAccountOperation {

	private ServiceOperationsFactory factory;

	public CloseAccountOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(datum);
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public void process(Registry registry, RegistryRecord record) throws FlexPayException {

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		Consumer consumer = props.getConsumer();
		if (consumer == null) {
			throw new FlexPayException("Cannot close account without consumer set");
		}

		if (consumer.getService().isSubService()) {
			throw new FlexPayException("Cannot close subaccount");
		}

		disableConsumer(consumer);

		// remove corrections to consumer
		EircRegistryProperties registryProperties = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = factory.getServiceProviderService().read(registryProperties.getServiceProviderStub());
		DataSourceDescription sd = provider.getDataSourceDescription();

		removeCorrections(consumer, record, sd);

		EircAccount account = factory.getAccountService().readFull(consumer.getEircAccountStub());
		if (account == null) {
			throw new IllegalStateException("EIRC account not found for consumer: " + consumer);
		}

		// close all subaccounts
		Service service = consumer.getService();
		for (Consumer c : account.getConsumers()) {
			if (service.equals(c.getService().getParentService())) {
				disableConsumer(c);
				removeCorrections(c, record, sd);
			}
		}
	}

	private void disableConsumer(Consumer consumer) throws FlexPayException {

		consumer.disable();
		try {
			factory.getConsumerService().save(consumer);
		} catch (FlexPayExceptionContainer container) {
			throw container.getFirstException();
		}
	}

	private void removeCorrections(Consumer consumer, RegistryRecord record, DataSourceDescription sd) {

		RawConsumerData data = RawConsumersDataUtil.convert(record);

		// update corrections (by internal service id)
		String internalId = String.valueOf(consumer.getService().getId());
		removeCorrections(data, consumer, internalId, sd);

		// and now by external service code
		String externalCode = consumer.getService().getExternalCode();
		if (StringUtils.isNotBlank(externalCode)) {
			removeCorrections(data, consumer, externalCode, sd);
		}
	}

	private void removeCorrections(RawConsumerData data, Consumer consumer,
								   String serviceCode, DataSourceDescription sd) {

		CorrectionsService service = factory.getCorrectionsService();

		setServiceCode(data, serviceCode);
		service.delete(service.getStub(data.getFullConsumerId(), consumer, sd));
		service.delete(service.getStub(data.getShortConsumerId(), consumer, sd));
	}

	private void setServiceCode(RawConsumerData data, String serviceCode) {
		data.addNameValuePair(RawConsumerData.FIELD_SERVICE_CODE, serviceCode);
	}
}
