package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class CorrectStreetAction extends FPActionWithPagerSupport<Street> implements CashboxAware {

	protected String setupType;
	protected Street object = new Street();
	protected RegistryRecord record = new RegistryRecord();
	protected Long cashboxId;

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected ServiceTypeService serviceTypeService;
	protected OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		record = recordService.read(record.getId());

		if ("street".equals(setupType)) {

			EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
			Organization organization = organizationService.readFull(props.getSenderStub());
			if (organization == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return SUCCESS;
			}
			Stub<DataSourceDescription> sd = organization.sourceDescriptionStub();

			saveCorrection(sd);

			record = recordService.removeError(record);
			return "complete";
		}
		return SUCCESS;
	}

	protected void saveCorrection(Stub<DataSourceDescription> sd) {

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
		return "street".equals(setupType) ? "complete" : SUCCESS;
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

	public Long getCashboxId() {
		return cashboxId;
	}

	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
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
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
