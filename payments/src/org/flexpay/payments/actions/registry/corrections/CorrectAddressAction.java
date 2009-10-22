package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
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

public class CorrectAddressAction extends FPActionWithPagerSupport<Apartment> implements CashboxAware {

	protected String setupType;
	protected Apartment object = Apartment.newInstance();
	protected RegistryRecord record = new RegistryRecord();
	protected Long cashboxId;

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected ServiceTypeService serviceTypeService;
	protected ClassToTypeRegistry typeRegistry;
	protected OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		record = recordService.read(record.getId());

		if ("apartment".equals(setupType)) {

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
		return "apartment".equals(setupType) ? "complete" : SUCCESS;
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.read(stub(typeStub));
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public boolean getCanCreateApartment() {
		ImportError error = record.getImportError();
		return error != null &&
			   typeRegistry.getType(Apartment.class) == error.getObjectType();
	}

	public boolean getCanCreateBuilding() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(BuildingAddress.class) == error.getObjectType();

	}

	public boolean getCanCreateStreet() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(Street.class) == error.getObjectType();
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public Apartment getObject() {
		return object;
	}

	public void setObject(Apartment object) {
		this.object = object;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	@Override
	public Long getCashboxId() {
		return cashboxId;
	}

	@Override
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
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
