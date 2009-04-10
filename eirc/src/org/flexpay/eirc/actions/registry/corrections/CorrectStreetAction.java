package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.actions.street.StreetsListAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.persistence.EircRegistryProperties;
import org.flexpay.eirc.service.RegistryRecordService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CorrectStreetAction extends StreetsListAction {

	private String setupType;
	private Street object = new Street();
	private RegistryRecord record = new RegistryRecord();

	private RawConsumersDataSource consumersDataSource;
	private CorrectionsService correctionsService;
	private RegistryRecordService recordService;
	private ServiceTypeService serviceTypeService;
	private ServiceProviderService serviceProviderService;

	@NotNull
	public String doExecute() throws Exception {

		record = recordService.read(record.getId());

		if ("street".equals(setupType)) {

			EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
			ServiceProvider provider = serviceProviderService.read(props.getServiceProviderStub());
			DataSourceDescription sd = provider.getDataSourceDescription();
			if (sd == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.doExecute();
			}

			RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

			// add correction for street
			DataCorrection correction = correctionsService.getStub(data.getStreetId(), object, sd);
			correctionsService.save(correction);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.doExecute();
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return "street".equals(setupType) ? "complete" : super.getErrorResult();
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.getServiceType(typeStub);
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public Street getObject() {
		return object;
	}

	public void setObject(Street object) {
		this.object = object;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	@Required
	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setRecordService(RegistryRecordService recordService) {
		this.recordService = recordService;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}
}
