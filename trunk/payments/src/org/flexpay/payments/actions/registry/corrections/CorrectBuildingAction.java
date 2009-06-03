package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.ab.actions.buildings.BuildingsListAction;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CorrectBuildingAction extends BuildingsListAction {

	protected String setupType;
	protected BuildingAddress object = new BuildingAddress();
	protected RegistryRecord record = new RegistryRecord();

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected ServiceTypeService serviceTypeService;
	protected ServiceProviderService serviceProviderService;

	@NotNull
	public String doExecute() throws Exception {

		record = recordService.read(record.getId());

		if ("building".equals(setupType)) {

			EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
			ServiceProvider provider = serviceProviderService.read(props.getServiceProviderStub());
			DataSourceDescription sd = provider.getDataSourceDescription();
			if (sd == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.doExecute();
			}

            saveCorrection(sd);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.doExecute();
	}

    protected void saveCorrection(DataSourceDescription sd) {

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
		return "building".equals(setupType) ? "complete" : super.getErrorResult();
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.read(stub(typeStub));
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public BuildingAddress getObject() {
		return object;
	}

	public void setObject(BuildingAddress object) {
		this.object = object;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
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
