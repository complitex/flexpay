package org.flexpay.mule.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.locking.LockManager;
import org.flexpay.common.persistence.Stub;
import org.flexpay.mule.Request;
import org.flexpay.mule.request.MuleAddressAttribute;
import org.flexpay.mule.request.MuleBuildingAddress;
import org.flexpay.mule.service.MuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.HashSet;
import java.util.Set;

public class MuleServiceImpl implements MuleService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private CountryService countryService;
    private RegionService regionService;
    private TownTypeService townTypeService;
    private TownService townService;
    private DistrictService districtService;
    private StreetTypeService streetTypeService;
    private StreetService streetService;
    private BuildingService buildingService;
    private ApartmentService apartmentService;
    private AddressAttributeTypeService addressAttributeTypeService;
    private LockManager lockManager;

    @Override
    public void processApartment(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing apartment");

        if (request.isDeleteAction()) {
            log.debug("Deleting apartment");
            apartmentService.disable(request.getApartment().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating apartment");
            Set<Apartment> apartments = request.getApartment().convert();
            log.debug("Converted apartments: {}", apartments);
            for (Apartment apartment : apartments) {
                apartmentService.create(apartment);
                log.debug("Created apartment: {}", apartment);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating apartment");
            Apartment apartment;
            Long apartmentId = request.getApartment().getId();
            if (apartmentId != null && apartmentId > 0) {
                apartment = apartmentService.readFull(new Stub<Apartment>(apartmentId));
                if (apartment == null) {
                    log.warn("Can't update apartment with id {}, because it's not found", apartmentId);
                    throw new FlexPayException("Can't update apartment with id " + apartmentId + ", because it's not found");
                }
            } else {
                log.warn("Can't update apartment with id {}, because id is incorrect", apartmentId);
                throw new FlexPayException("Can't update apartment. Incorrect apartment id - \"" + apartmentId + "\"");
            }

            apartment.setNumber(request.getApartment().getNumber());
            log.debug("Converted apartment = {}", apartment);
            apartmentService.update(apartment);
        }
    }

    @Override
    public void processBuildingAddress(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing building address");

        if (request.isDeleteAction()) {
            log.debug("Deleting building address");
            buildingService.disableAddresses(request.getBuildingAddress().getIds(), new Stub<Building>(request.getBuildingAddress().getBuildingId()));
        } else if (request.isInsertAction()) {
            log.debug("Creating building address");
            BuildingAddress address = new BuildingAddress();
            request.getBuildingAddress().convert(address, addressAttributeTypeService);
            log.debug("Converted building address: {}", address);
            Building building = buildingService.readFull(new Stub<Building>(request.getBuildingAddress().getBuildingId()));
            building.addAddress(address);
            buildingService.update(building);
            log.debug("Created building address: {}", address);
        } else if (request.isUpdateAction()) {
            log.debug("Updating building address");
            BuildingAddress address;
            MuleBuildingAddress muleAddress = request.getBuildingAddress();
            Long addressId = muleAddress.getId();
            if (addressId != null && addressId > 0) {
                address = buildingService.readWithHierarchy(new Stub<BuildingAddress>(addressId));
                if (address == null) {
                    log.warn("Can't update building address with id {}, because it's not found", addressId);
                    throw new FlexPayException("Can't update building address with id " + addressId + ", because it's not found");
                }
            } else {
                log.warn("Can't update building address with id {}, because id is incorrect", addressId);
                throw new FlexPayException("Can't update building address. Incorrect address id - \"" + addressId + "\"");
            }
            address.setPrimaryStatus(muleAddress.getPrimary());
            address.setStreet(new Street(muleAddress.getStreetId()));
            for (MuleAddressAttribute muleAttr : muleAddress.getAttributes()) {
                for (AddressAttribute attr : address.getBuildingAttributes()) {
                    if (attr.getBuildingAttributeType().getId().equals(muleAttr.getId())) {
                        attr.setValue(muleAttr.getValue());
                    } else {
                        attr.setValue("");
                    }
                }
            }
            Building building = buildingService.readFull(new Stub<Building>(muleAddress.getBuildingId()));
            building.addAddress(address);
            buildingService.update(building);
        }
    }

    @Override
    public void processBuilding(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing building");

        if (request.isDeleteAction()) {
            log.debug("Deleting building");
            buildingService.disable(request.getBuilding().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating buildings");
            Set<Building> buildings = request.getBuilding().convert(addressAttributeTypeService);
            log.debug("Converted buildings: {}", buildings);
            for (Building building : buildings) {
                buildingService.create(building);
                log.debug("Created building: {}", building);
            }
        } else if (request.isUpdateAction()) {
            log.warn("We can't update buildings");
        }
    }

    @Override
    public void processStreet(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing street");

        if (request.isDeleteAction()) {
            log.debug("Deleting street");
            streetService.disable(request.getStreet().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating street");
            Street street = new Street();
            request.getStreet().convert(street);
            log.debug("Converted street = {}", street);
            for (StreetNameTranslation trans : street.getCurrentName().getTranslations()) {
                log.debug(" - Street translations: {}", trans);
            }
            streetService.create(street);
            log.debug("Created street: {}", street);
            for (StreetNameTranslation trans : street.getCurrentName().getTranslations()) {
                log.debug(" - Street translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating street");
            Street street;
            Long streetId = request.getStreet().getId();
            if (streetId != null && streetId > 0) {
                street = streetService.readFull(new Stub<Street>(streetId));
                if (street == null) {
                    log.warn("Can't update street with id {}, because it's not found", streetId);
                    throw new FlexPayException("Can't update street with id " + streetId + ", because it's not found");
                }
            } else {
                log.warn("Can't update street with id {}, because id is incorrect", streetId);
                throw new FlexPayException("Can't update street. Incorrect street id - \"" + streetId + "\"");
            }
            log.debug("Old street: {}", street);
            for (StreetNameTranslation trans : street.getCurrentName().getTranslations()) {
                log.debug(" - Street translations: {}", trans);
            }
            request.getStreet().convert(street);
            log.debug("Converted street = {}", street);
            for (StreetNameTranslation trans : street.getCurrentName().getTranslations()) {
                log.debug(" - Street translations: {}", trans);
            }
            streetService.update(street);
            for (StreetNameTranslation trans : street.getCurrentName().getTranslations()) {
                log.debug(" - Street translations: {}", trans);
            }
        } else if (request.isUpdateStreetDistrictsAction()) {
            log.debug("Updating street-districts");
            Street street;
            Long streetId = request.getStreet().getId();
            if (streetId != null && streetId > 0) {
                street = streetService.readFull(new Stub<Street>(streetId));
                if (street == null) {
                    log.warn("Can't update street with id {}, because it's not found", streetId);
                    throw new FlexPayException("Can't update street with id " + streetId + ", because it's not found");
                }
            } else {
                log.warn("Can't update street with id {}, because id is incorrect", streetId);
                throw new FlexPayException("Can't update street. Incorrect street id - \"" + streetId + "\"");
            }
            Set<Long> districtIds = CollectionUtils.isNotEmpty(request.getStreet().getDistricts()) ? request.getStreet().getDistricts() : new HashSet<Long>();
            streetService.saveDistricts(street, districtIds);
            log.debug("Updated street: {}", street);
            for (StreetDistrictRelation distrRelation : street.getStreetDistricts()) {
                log.debug(" - Street-districts: {}", distrRelation);
            }
        }
    }

    @Override
    public void processStreetType(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing street type");

        if (request.isDeleteAction()) {
            log.debug("Deleting street type");
            streetTypeService.disable(request.getStreetType().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating street type");
            StreetType streetType = new StreetType();
            request.getStreetType().convert(streetType);
            log.debug("Converted street type = {}", streetType);
            for (StreetTypeTranslation trans : streetType.getTranslations()) {
                log.debug(" - Street type translations: {}", trans);
            }
            streetTypeService.create(streetType);

            log.debug("Created street type = {}", streetType);
            for (StreetTypeTranslation trans : streetType.getTranslations()) {
                log.debug(" - Street type translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating street type");
            StreetType streetType;
            Long streetTypeId = request.getStreetType().getId();
            if (streetTypeId != null && streetTypeId > 0) {
                streetType = streetTypeService.readFull(new Stub<StreetType>(streetTypeId));
                if (streetType == null) {
                    log.warn("Can't update streetType with id {}, because it's not found", streetTypeId);
                    throw new FlexPayException("Can't update district with id " + streetTypeId + ", because it's not found");
                }
            } else {
                log.warn("Can't update streetType with id {}, because id is incorrect", streetTypeId);
                throw new FlexPayException("Can't update streetType. Incorrect streetType id - \"" + streetTypeId + "\"");
            }
            log.debug("Old street type = {}", streetType);
            for (StreetTypeTranslation trans : streetType.getTranslations()) {
                log.debug(" - Street type translations: {}", trans);
            }
            request.getStreetType().convert(streetType);
            log.debug("Converted street type = {}", streetType);
            for (StreetTypeTranslation trans : streetType.getTranslations()) {
                log.debug(" - Street type translations: {}", trans);
            }
            streetTypeService.update(streetType);
            log.debug("Updated street type = {}", streetType);
            for (StreetTypeTranslation trans : streetType.getTranslations()) {
                log.debug(" - Street type translations: {}", trans);
            }
        }
    }

    @Override
    public void processDistrict(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing district");

        if (request.isDeleteAction()) {
            log.debug("Deleting district");
            districtService.disable(request.getDistrict().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating district");
            District district = new District();
            request.getDistrict().convert(district);
            log.debug("Converted district = {}", district);
            for (DistrictNameTranslation trans : district.getCurrentName().getTranslations()) {
                log.debug(" - District translations: {}", trans);
            }
            districtService.create(district);
            log.debug("Created district: {}", district);
            for (DistrictNameTranslation trans : district.getCurrentName().getTranslations()) {
                log.debug(" - District translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating district");
            District district;
            Long districtId = request.getDistrict().getId();
            if (districtId != null && districtId > 0) {
                district = districtService.readFull(new Stub<District>(districtId));
                if (district == null) {
                    log.warn("Can't update district with id {}, because it's not found", districtId);
                    throw new FlexPayException("Can't update district with id " + districtId + ", because it's not found");
                }
            } else {
                log.warn("Can't update district with id {}, because id is incorrect", districtId);
                throw new FlexPayException("Can't update district. Incorrect district id - \"" + districtId + "\"");
            }

            log.debug("Old district = {}", district);
            for (DistrictNameTranslation trans : district.getCurrentName().getTranslations()) {
                log.debug(" - District translations: {}", trans);
            }

            request.getDistrict().convert(district);
            log.debug("Converted district = {}", district);
            for (DistrictNameTranslation trans : district.getCurrentName().getTranslations()) {
                log.debug(" - District translations: {}", trans);
            }
            districtService.update(district);
            log.debug("Updated district = {}", district);
            for (DistrictNameTranslation trans : district.getCurrentName().getTranslations()) {
                log.debug(" - District translations: {}", trans);
            }
        }
    }

    @Override
    public void processTownType(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing town type");

        if (request.isDeleteAction()) {
            log.debug("Deleting town type");
            townTypeService.disable(request.getTownType().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating town type");
            TownType townType = new TownType();
            request.getTownType().convert(townType);

            log.debug("Converted town type = {}", townType);
            for (TownTypeTranslation trans : townType.getTranslations()) {
                log.debug(" - Town type translations: {}", trans);
            }
            townTypeService.create(townType);

            log.debug("Created town type: {}", townType);
            for (TownTypeTranslation trans : townType.getTranslations()) {
                log.debug(" - Town type translations: {}", trans);
            }

        } else if (request.isUpdateAction()) {
            log.debug("Updating town type");
            TownType townType;
            Long townTypeId = request.getTownType().getId();
            if (townTypeId != null && townTypeId > 0) {
                townType = townTypeService.readFull(new Stub<TownType>(townTypeId));
                if (townType == null) {
                    log.warn("Can't update townType with id {}, because it's not found", townTypeId);
                    throw new FlexPayException("Can't update townType with id " + townTypeId + ", because it's not found");
                }
            } else {
                log.warn("Can't update townType with id {}, because id is incorrect", townTypeId);
                throw new FlexPayException("Can't update townType. Incorrect townType id - \"" + townTypeId + "\"");
            }

            log.debug("Old town type = {}", townType);
            for (TownTypeTranslation trans : townType.getTranslations()) {
                log.debug(" - Town type translations: {}", trans);
            }

            request.getTownType().convert(townType);

            log.debug("Converted town type = {}", townType);
            for (TownTypeTranslation trans : townType.getTranslations()) {
                log.debug(" - Town type translations: {}", trans);
            }

            townTypeService.update(townType);

            log.debug("Updated town type: {}", townType);
            for (TownTypeTranslation trans : townType.getTranslations()) {
                log.debug(" - Town type translations: {}", trans);
            }

        }
    }

    @Override
    public void processTown(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing town");

        if (request.isDeleteAction()) {
            log.debug("Deleting town");
            townService.disable(request.getTown().getIds());
        } else if (request.isInsertAction()) {
            log.debug("Creating town");
            Town town = new Town();
            request.getTown().convert(town);
            log.debug("Converted town = {}", town);
            for (TownNameTranslation trans : town.getCurrentName().getTranslations()) {
                log.debug(" - Town translations: {}", trans);
            }
            townService.create(town);
            log.debug("Created town: {}", town);
            for (TownNameTranslation trans : town.getCurrentName().getTranslations()) {
                log.debug(" - Town translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating town");
            Long townId = request.getTown().getId();
            Town town;
            if (townId != null && townId > 0) {
                town = townService.readFull(new Stub<Town>(townId));
                if (town == null) {
                    log.warn("Can't update town with id {}, because it's not found", townId);
                    throw new FlexPayException("Can't update town with id " + townId + ", because it's not found");
                }
            } else {
                log.warn("Can't update town with id {}, because id is incorrect", townId);
                throw new FlexPayException("Can't update town. Incorrect town id - \"" + townId + "\"");
            }

            log.debug("Old town = {}", town);
            for (TownNameTranslation trans : town.getCurrentName().getTranslations()) {
                log.debug(" - Town translations: {}", trans);
            }

            request.getTown().convert(town);
            log.debug("Converted town = {}", town);
            for (TownNameTranslation trans : town.getCurrentName().getTranslations()) {
                log.debug(" - Town translations: {}", trans);
            }
            townService.update(town);
            log.debug("Updated town: {}", town);
            for (TownNameTranslation trans : town.getCurrentName().getTranslations()) {
                log.debug(" - Town translations: {}", trans);
            }

        }
    }

    @Override
    public void processRegion(Request request) throws FlexPayExceptionContainer, FlexPayException {

        log.debug("Processing region");

        if (request.isDeleteAction()) {
            log.debug("Deleting region");
            regionService.disable(request.getRegion().getIds());
            log.debug("Region deleted");
        } else if (request.isInsertAction()) {
            log.debug("Creating region");
            Region region = new Region();
            request.getRegion().convert(region);

            log.debug("Converted region = {}", region);
            for (RegionNameTranslation trans : region.getCurrentName().getTranslations()) {
                log.debug(" - Region translations: {}", trans);
            }
            regionService.create(region);
            log.debug("Created region: {}", region);
            for (RegionNameTranslation trans : region.getCurrentName().getTranslations()) {
                log.debug(" - Region translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.debug("Updating region");
            Region region;
            Long regionId = request.getRegion().getId();
            if (regionId != null && regionId > 0) {
                region = regionService.readFull(new Stub<Region>(regionId));
                if (region == null) {
                    log.warn("Can't update region with id {}, because it's not found", regionId);
                    throw new FlexPayException("Can't update region with id " + regionId + ", because it's not found");
                }
            } else {
                log.warn("Can't update region with id {}, because id is incorrect", regionId);
                throw new FlexPayException("Can't update region. Incorrect region id - \"" + regionId + "\"");
            }

            log.debug("Old region = {}", region);
            for (RegionNameTranslation trans : region.getCurrentName().getTranslations()) {
                log.debug(" - Region translations: {}", trans);
            }

            request.getRegion().convert(region);

            log.debug("Converted region = {}", region);
            for (RegionNameTranslation trans : region.getCurrentName().getTranslations()) {
                log.debug(" - Region translations: {}", trans);
            }
            regionService.update(region);
            log.debug("Updated region: {}", region);
            for (RegionNameTranslation trans : region.getCurrentName().getTranslations()) {
                log.debug(" - Region translations: {}", trans);
            }
        }
    }

    @Override
    public void processCountry(Request request) throws FlexPayExceptionContainer {

        log.debug("Processing country");

        if (request.isDeleteAction()) {
            log.warn("We can't delete countries");
        } else if (request.isInsertAction()) {
            log.debug("Creating country");
            Country country = new Country();
            request.getCountry().convert(country);
            log.debug("Converted country = {}", country);
            for (CountryTranslation trans : country.getTranslations()) {
                log.debug(" - Country translations: {}", trans);
            }
            country = countryService.create(country);
            log.debug("Created country: {}", country);
            for (CountryTranslation trans : country.getTranslations()) {
                log.debug(" - Country translations: {}", trans);
            }
        } else if (request.isUpdateAction()) {
            log.warn("We can't update countries");
        }
    }

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        lockManager.setJpaTemplate(jpaTemplate);
        countryService.setJpaTemplate(jpaTemplate);
        regionService.setJpaTemplate(jpaTemplate);
        townService.setJpaTemplate(jpaTemplate);
        townTypeService.setJpaTemplate(jpaTemplate);
        districtService.setJpaTemplate(jpaTemplate);
        streetTypeService.setJpaTemplate(jpaTemplate);
        streetService.setJpaTemplate(jpaTemplate);
        buildingService.setJpaTemplate(jpaTemplate);
        addressAttributeTypeService.setJpaTemplate(jpaTemplate);
        apartmentService.setJpaTemplate(jpaTemplate);
    }

    @Required
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Required
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    @Required
    public void setTownTypeService(TownTypeService townTypeService) {
        this.townTypeService = townTypeService;
    }

    @Required
    public void setTownService(TownService townService) {
        this.townService = townService;
    }

    @Required
    public void setDistrictService(DistrictService districtService) {
        this.districtService = districtService;
    }

    @Required
    public void setStreetTypeService(StreetTypeService streetTypeService) {
        this.streetTypeService = streetTypeService;
    }

    @Required
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }

    @Required
    public void setBuildingService(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Required
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @Required
    public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
        this.addressAttributeTypeService = addressAttributeTypeService;
    }

    @Required
    public void setLockManager(LockManager lockManager) {
        this.lockManager = lockManager;
    }

}
