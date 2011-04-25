package org.flexpay.eirc.action.registry;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.action.registry.data.RecordErrorsGroupView;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_STREET;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_STREET_TYPE;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.*;

public class RegistryRecordErrorFixAction extends AccountantAWPActionSupport {

    private Registry registry = new Registry();
    private RecordErrorsGroup group = new RecordErrorsGroup();
    private District district = new District();

    private Map<Long, String> names = treeMap();
    private Map<Long, String> shortNames = treeMap();

    private ApartmentService apartmentService;
    private BuildingService buildingService;
    private StreetService streetService;
    private StreetTypeService streetTypeService;
    private ObjectsFactory objectsFactory;
    private RegistryRecordStatusService registryRecordStatusService;
    private RegistryRecordService registryRecordService;
    private RegistryService registryService;
    private ClassToTypeRegistry typeRegistry;
    private OrganizationService organizationService;
    private CorrectionsService correctionsService;
    private RawConsumersDataSource consumersDataSource;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        registry = registryService.read(stub(registry));

        log.debug("Group: {}", group);

        RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
        RegistryRecordStatus status = registryRecordStatusService.findByCode(PROCESSED_WITH_ERROR);
        if (status == null) {
            log.warn("Can't get status by code from DB ({})", PROCESSED_WITH_ERROR);
            addActionError(getText("eirc.error.registry.internal_error"));
            return SUCCESS;
        }
        recordStatusFilter.setSelectedId(status.getId());

        ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
        importErrorTypeFilter.setSelectedType(group.getErrorType());

        RecordErrorsGroupView groupView = new RecordErrorsGroupView(group, typeRegistry);

        List<ObjectFilter> filters = list();
        filters.add(recordStatusFilter);
        filters.add(importErrorTypeFilter);

        Page<RegistryRecord> pager = new Page<RegistryRecord>(2000);
        List<RegistryRecord> records = registryRecordService.listRecords(registry, filters, groupView.getCriteria(typeRegistry), groupView.getParams(typeRegistry), pager);
        if (records.isEmpty()) {
            log.debug("Records not found");
            return SUCCESS;
        }

        Stub<DataSourceDescription> stubDataSourceDescription = getDataSourceDescription(registry);
        log.debug("Found stub data source {}", stubDataSourceDescription);
        RawConsumerData data = consumersDataSource.getById(String.valueOf(records.get(0).getId()));
        log.debug("Found raw consumer data {}", data);

        Integer objectType = group.getErrorType();

        if (checkStreetType(objectType)) {
            try {
                createStreet(data, stubDataSourceDescription);
            } catch (FlexPayException e) {
                log.debug("Error with creating street", e);
                addActionError(getText("eirc.error.registry.cant_create_street"));
                return SUCCESS;
            }
        } else if (checkBuildingType(objectType)) {
            try {
                createBuilding(data, stubDataSourceDescription);
            } catch (FlexPayExceptionContainer e) {
                log.debug("Error with creating building", e);
                addActionError(getText("eirc.error.registry.cant_create_building"));
                return SUCCESS;
            }
        } else if (checkApartmentType(objectType)) {
            try {
                createApartment(data, stubDataSourceDescription);
            } catch (FlexPayException e) {
                log.debug("Error with creating apartment", e);
                addActionError(getText("eirc.error.registry.cant_create_apartment"));
                return SUCCESS;
            }
        } else if (checkStreetTypeType(objectType)) {
            try {
                createStreetType();
            } catch (FlexPayExceptionContainer e) {
                log.debug("Error with creating street type", e);
                addActionError(getText("eirc.error.registry.cant_create_street_type"));
                return SUCCESS;
            }
        }

        for (;;) {
            registryRecordService.removeError(records);
            records = registryRecordService.listRecords(registry, filters, groupView.getCriteria(typeRegistry), groupView.getParams(typeRegistry), pager);
            if (records.isEmpty()) {
                log.debug("Records list is empty!");
                break;
            }
        }

        addActionMessage(getText("eirc.registry.object_added"));

        return SUCCESS;
    }

    protected Stub<DataSourceDescription> getDataSourceDescription(Registry registry) {

        EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
        Organization organization = organizationService.readFull(props.getSenderStub());
        if (organization == null) {
            log.warn("Can't get organization with id {} from DB", props.getSenderStub().getId());
            addActionError(getText("payments.error.data_source_not_found"));
            return null;
        }

        return organization.sourceDescriptionStub();

    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    private void createStreet(RawConsumerData data, Stub<DataSourceDescription> stubDataSourceDescription) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Checking street for existence");
        Street street = streetService.findStreet(arrayStack(
                new TownFilter(getDefaultTownStub().getId()),
                new StringValueFilter(group.getStreetType(), TYPE_STREET_TYPE),
                new StringValueFilter(group.getStreetName(), TYPE_STREET)));
        if (street != null) {
            log.debug("Street for this error already exists");
            return;
        }
        log.debug("Street not found");
        log.debug("Checking street correction for existence");
        Stub<Street> streetStub = correctionsService.findCorrection(data.getStreetId(), Street.class, stubDataSourceDescription);
        if (streetStub != null) {
            log.debug("Street correction for this error already exists");
            return;
        }
        log.debug("Street correction not found");

        log.debug("Creating new street");
        street = new Street();
        StreetName streetName = new StreetName();
        streetName.setTranslation(new StreetNameTranslation(group.getStreetName(), getDefaultLanguage()));
        street.setName(streetName);
        street.setParent(new Town(getDefaultTownStub()));
        Stub<StreetType> stub = streetTypeService.findTypeByName(group.getStreetType());
        if (stub == null) {
            log.debug("Street type with name {} not found", group.getStreetType());

            stub = correctionsService.findCorrection(data.getStreetTypeId(), StreetType.class, stubDataSourceDescription);

            if (stub == null) {
                log.debug("Correction for street type not found too");
                addActionError(getText("eirc.error.registry.street_type_not_found"));
                throw new FlexPayException("Street type not found");
            }
        }
        street.setType(new StreetType(stub));
        streetService.create(street);
        log.debug("Street with id {} created", street.getId());
    }

    private void createBuilding(RawConsumerData data, Stub<DataSourceDescription> stubDataSourceDescription) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Checking building for existence");
        Street street = streetService.findStreet(arrayStack(
                new TownFilter(getDefaultTownStub().getId()),
                new StringValueFilter(group.getStreetType(), TYPE_STREET_TYPE),
                new StringValueFilter(group.getStreetName(), TYPE_STREET)));
        Stub<Street> stub;
        if (street == null) {
            log.debug("Street for building not found");
            stub = correctionsService.findCorrection(data.getStreetId(), Street.class, stubDataSourceDescription);

            if (stub == null) {
                log.debug("Correction for street for building not found too");
                addActionError(getText("eirc.error.registry.street_not_found"));
                throw new FlexPayExceptionContainer(new FlexPayException("Street not found"));
            }

            street = new Street(stub);
        }
        BuildingAddress add = buildingService.findAddresses(stub(street), buildingService.attributes(group.getBuildingNumber(), group.getBuildingBulk()));
        if (add != null) {
            log.debug("Building for this error already exists");
            return;
        }
        log.debug("Building not found");
        log.debug("Checking building correction for existence");
        Stub<BuildingAddress> addressStub = correctionsService.findCorrection(data.getBuildingId(), BuildingAddress.class, stubDataSourceDescription);
        if (addressStub != null) {
            log.debug("Building correction for this error already exists");
            return;
        }
        log.debug("Building correction not found");

        log.debug("Creating new building");

        Building building = objectsFactory.newBuilding();
        if (district == null || district.isNew()) {
            log.error("Incorrect district id");
            addActionError(getText("eirc.error.registry.internal_error"));
            return;
        }
        building.setDistrict(district);
        log.debug("Found street for building: {}", street);
        BuildingAddress address = new BuildingAddress();
        address.setPrimaryStatus(true);
        address.setStreet(street);
        address.setBuildingAttribute(group.getBuildingNumber(), getBuildingAttributeTypeNumber());
        address.setBuildingAttribute(group.getBuildingBulk(), getBuildingAttributeTypeBulk());
        address.setBuildingAttribute("", getBuildingAttributeTypePart());
        building.addAddress(address);
        buildingService.create(building);
        log.debug("Building with id {} created", building.getId());
    }

    private void createApartment(RawConsumerData data, Stub<DataSourceDescription> stubDataSourceDescription) throws FlexPayExceptionContainer, FlexPayException {

        Street street = streetService.findStreet(arrayStack(
                new TownFilter(getDefaultTownStub().getId()),
                new StringValueFilter(group.getStreetType(), TYPE_STREET_TYPE),
                new StringValueFilter(group.getStreetName(), TYPE_STREET)));
        Stub<Street> stub;
        if (street == null) {
            log.debug("Street for apartment not found");
            stub = correctionsService.findCorrection(data.getStreetId(), Street.class, stubDataSourceDescription);

            if (stub == null) {
                log.debug("Correction for street for apartment not found too");
                addActionError(getText("eirc.error.registry.street_not_found"));
                throw new FlexPayExceptionContainer(new FlexPayException("Street not found"));
            }

            street = new Street(stub);
        }
        log.debug("Found street for apartment: {}", street);
        BuildingAddress address = buildingService.findAddresses(stub(street), buildingService.attributes(group.getBuildingNumber(), group.getBuildingBulk()));
        if (address == null) {
            log.error("Building address with number = {}, bulk = {} not found", group.getBuildingNumber(), group.getBuildingBulk());

            Stub<BuildingAddress> addressStub = correctionsService.findCorrection(data.getBuildingId(), BuildingAddress.class, stubDataSourceDescription);
            if (addressStub == null) {
                log.debug("Correction for building address not found too");
                addActionError(getText("eirc.error.registry.building_not_found"));
                throw new FlexPayExceptionContainer(new FlexPayException("Building not found"));
            }

            address = buildingService.readFullAddress(addressStub);
        }
        log.debug("Found building address for apartment: {}", address);
        log.debug("Checking apartment for existence");

        Stub<Apartment> apStub = apartmentService.findApartmentStub(address.getBuilding(), group.getApartmentNumber());
        if (apStub != null) {
            log.debug("Apartment for this error already exists");
            return;
        }
        log.debug("Checking building correction for existence");
        apStub = correctionsService.findCorrection(data.getApartmentId(), Apartment.class, stubDataSourceDescription);
        if (apStub != null) {
            log.debug("Apartment correction for this error already exists");
            return;
        }
        log.debug("Apartment correction not found");

        log.debug("Creating apartment");
        Apartment apartment = objectsFactory.newApartment();
        apartment.setNumber(group.getApartmentNumber());
        apartment.setBuilding(new Building(address.getBuilding().getId()));
        apartmentService.create(apartment);
        log.debug("Apartment with id {} created", apartment.getId());
    }

    private void createStreetType() throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Checking street type for existence");
        Stub<StreetType> stub = streetTypeService.findTypeByName(group.getStreetType());
        if (stub != null) {
            log.debug("Street type for this error already exists");
            return;
        }

        log.debug("Street type not found");
        log.debug("Creating street type");
        StreetType streetType = new StreetType();

        for (Map.Entry<Long, String> name : names.entrySet()) {
            String value = name.getValue();
            String shortName = shortNames.get(name.getKey());
            Language lang = getLang(name.getKey());
            try {
                streetType.setTranslation(new StreetTypeTranslation(value, shortName, lang));
            } catch (Exception e) {
                log.error("Can't create StreetTypeTranslation with value = {}, shortName = {}, lang = {}", new Object[] {value, shortName, lang});
                throw new FlexPayExceptionContainer(new FlexPayException("Can't create StreetTypeTranslation with value = " + value + ", shortName = " + shortName));
            }
        }

        streetTypeService.create(streetType);
        log.debug("Street type with id {} created", streetType.getId());
    }

    protected boolean checkApartmentType(int objectType) {
        return typeRegistry.getType(Apartment.class) == objectType;
    }

    protected boolean checkBuildingType(int objectType) {
        return typeRegistry.getType(BuildingAddress.class) == objectType;
    }

    protected boolean checkStreetType(int objectType) {
        return typeRegistry.getType(Street.class) == objectType;
    }

    protected boolean checkStreetTypeType(int objectType) {
        return typeRegistry.getType(StreetType.class) == objectType;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public RecordErrorsGroup getGroup() {
        return group;
    }

    public void setGroup(RecordErrorsGroup group) {
        this.group = group;
    }

    public Map<Long, String> getNames() {
        return names;
    }

    public void setNames(Map<Long, String> names) {
        this.names = names;
    }

    public Map<Long, String> getShortNames() {
        return shortNames;
    }

    public void setShortNames(Map<Long, String> shortNames) {
        this.shortNames = shortNames;
    }

    @Required
    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

    @Required
    public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Required
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Required
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }

    @Required
    public void setStreetTypeService(StreetTypeService streetTypeService) {
        this.streetTypeService = streetTypeService;
    }

    @Required
    public void setObjectsFactory(ObjectsFactory objectsFactory) {
        this.objectsFactory = objectsFactory;
    }

    @Required
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    @Required
    public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
        this.consumersDataSource = consumersDataSource;
    }

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
