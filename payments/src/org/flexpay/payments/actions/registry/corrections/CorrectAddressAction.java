package org.flexpay.payments.actions.registry.corrections;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.apartment.ApartmentsListAction;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CorrectAddressAction extends ApartmentsListAction implements CashboxAware {

	protected String setupType;
	protected Apartment object = Apartment.newInstance();
	protected RegistryRecord record = new RegistryRecord();
	protected Long cashboxId;

	protected DistrictFilter districtFilter = new DistrictFilter();

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected ServiceTypeService serviceTypeService;
	protected ClassToTypeRegistry typeRegistry;
	protected ServiceProviderService serviceProviderService;

	@NotNull
	public String doExecute() throws Exception {

		record = recordService.read(record.getId());

		if ("apartment".equals(setupType)) {

			EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
			ServiceProvider provider = serviceProviderService.read(props.getServiceProviderStub());
			if (provider == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.doExecute();
			}
			Stub<DataSourceDescription> sd = provider.getDataSourceDescriptionStub();

			saveCorrection(sd);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.doExecute();
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
		return "apartment".equals(setupType) ? "complete" : super.getErrorResult();
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

	public DistrictFilter getDistrictFilter() {
		return districtFilter;
	}

	public void setDistrictFilter(DistrictFilter districtFilter) {
		this.districtFilter = districtFilter;
	}

	public ArrayStack getFilters() {
		ArrayStack filters = new ArrayStack();
		filters.push(countryFilter);
		filters.push(regionFilter);
		filters.push(townFilter);
		filters.push(districtFilter);
		filters.push(streetNameFilter);
		filters.push(buildingsFilter);

		return filters;
	}

	public void setFilters(ArrayStack filters) {
		int n = 5;
		countryFilter = (CountryFilter) filters.peek(n--);
		regionFilter = (RegionFilter) filters.peek(n--);
		townFilter = (TownFilter) filters.peek(n--);
		districtFilter = (DistrictFilter) filters.peek(n--);
		streetNameFilter = (StreetNameFilter) filters.peek(n--);
		buildingsFilter = (BuildingsFilter) filters.peek(n);
	}

	public boolean getCanCreateApartment() {
		ImportError error = record.getImportError();
		return error != null &&
			   (typeRegistry.getType(Apartment.class) == error.getObjectType() ||
				typeRegistry.getType(Apartment.class) == error.getObjectType());

	}

	public boolean getCanCreateBuilding() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(BuildingAddress.class) == error.getObjectType();

	}

	public boolean getCanCreateStreet() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(Street.class) == error.getObjectType();
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
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

}
