package org.flexpay.eirc.actions.registry.corrections;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.actions.apartment.ListApartments;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.importexport.RawConsumerData;

public class CorrectAddressAction extends ListApartments {

	private Logger log = Logger.getLogger(getClass());

	private String setupType;
	private Apartment object = new Apartment();
	private SpRegistryRecord record = new SpRegistryRecord();

	private DistrictFilter districtFilter = new DistrictFilter();

	private RawConsumersDataSource consumersDataSource;
	private CorrectionsService correctionsService;
	private SpRegistryRecordService recordService;
	private SPService spService;
	private ClassToTypeRegistry typeRegistry;
	private BuildingService buildingService;

	public String execute() throws Exception {

		record = recordService.read(record.getId());

		boolean setupApartment = "apartment".equals(setupType);
		boolean setupBuilding = "building".equals(setupType);
		boolean setupStreet = "street".equals(setupType);
		boolean createApartment = "create-apartment".equals(setupType);
		boolean createBuilding = "create-building".equals(setupType);
		boolean createStreet = "create-street".equals(setupType);
		if (setupApartment || setupBuilding || setupStreet || createApartment || createBuilding || createStreet) {

			DataSourceDescription sd = recordService.getDataSourceDescription(record);
			if (sd == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.execute();
			}

			RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

			if (createApartment) {
				return createApartment(sd, data);
			}

			if (createBuilding) {
				return createBuilding(sd, data);
			}

			if (createStreet) {
				return createStreet(sd, data);
			}

			// add correction for apartment
			DataCorrection correction = correctionsService.getStub(data.getApartmentId(), object, sd);
			correctionsService.save(correction);

			// add correction for buildings
			if (setupBuilding || setupStreet) {
				Buildings buildings = new Buildings(buildingsFilter.getSelectedId());
				correction = correctionsService.getStub(data.getBuildingId(), buildings, sd);
				correctionsService.save(correction);
			}

			// add correction for street
			if (setupStreet) {
				Street street = new Street(streetFilter.getSelectedId());
				correction = correctionsService.getStub(data.getStreetId(), street, sd);
				correctionsService.save(correction);
			}

			record = recordService.removeError(record);
			return "complete";
		}
		return super.execute();
	}

	private String createApartment(DataSourceDescription sd, RawConsumerData data) throws Exception {

		Buildings buildings = new Buildings(buildingsFilter.getSelectedId());
		Building building = buildingService.findBuilding(buildings);
		if (building == null) {
			addActionError(getText("eirc.building_not_exists"));
			return super.execute();
		}
		Apartment apartment = new Apartment();
		apartment.setBuilding(building);
		apartment.setNumber(data.getAddressApartment());
		apartmentService.create(apartment);

		DataCorrection correction = correctionsService.getStub(data.getApartmentId(), apartment, sd);
		correctionsService.save(correction);

		return super.execute();
	}

	private String createStreet(DataSourceDescription sd, RawConsumerData data) throws Exception {
		addActionError("Street creation not implemented yet");
		return super.execute();
	}

	private String createBuilding(DataSourceDescription sd, RawConsumerData data) throws Exception {
		Street street = new Street(streetFilter.getSelectedId());
		District district = new District(districtFilter.getSelectedId());

		String numberValue = data.getAddressHouse();
		String bulkValue = data.getAddressBulk();

		try {
			Buildings buildings = buildingService.createBuildings(street, district, numberValue, bulkValue);
			DataCorrection correction = correctionsService.getStub(data.getBuildingId(), buildings, sd);
			correctionsService.save(correction);
		} catch (FlexPayException e) {
			log.error("Failed creating building", e);
			addActionError(getText("eirc.building_create_failed"));
			return super.execute();
		}

		record = recordService.removeError(record);

		return "complete";
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = spService.getServiceType(typeStub);
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

	public SpRegistryRecord getRecord() {
		return record;
	}

	public void setRecord(SpRegistryRecord record) {
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
		filters.push(streetFilter);
		filters.push(buildingsFilter);

		return filters;
	}

	public void setFilters(ArrayStack filters) {
		int n = 5;
		countryFilter = (CountryFilter) filters.peek(n--);
		regionFilter = (RegionFilter) filters.peek(n--);
		townFilter = (TownFilter) filters.peek(n--);
		districtFilter = (DistrictFilter) filters.peek(n--);
		streetFilter = (StreetFilter) filters.peek(n--);
		buildingsFilter = (BuildingsFilter) filters.peek(n);
	}

	public boolean getCanCreateApartment() {
		ImportError error = record.getImportError();
		return error != null &&
				(typeRegistry.getType(Apartment.class) == error.getObjectType() ||
						typeRegistry.getType(org.flexpay.bti.persistence.Apartment.class) == error.getObjectType());

	}

	public boolean getCanCreateBuilding() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(Buildings.class) == error.getObjectType();

	}

	public boolean getCanCreateStreet() {
		ImportError error = record.getImportError();
		return error != null && typeRegistry.getType(Street.class) == error.getObjectType();
	}

	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public void setRecordService(SpRegistryRecordService recordService) {
		this.recordService = recordService;
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
