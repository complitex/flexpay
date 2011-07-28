package org.flexpay.mule;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.mule.request.*;

import java.io.Serializable;

public class Request implements Serializable {

    public static String ACTION_DELETE = "delete";
    public static String ACTION_INSERT = "insert";
    public static String ACTION_UPDATE = "update";
    public static String ACTION_UPDATE_STREET_DISTRICTS = "update_sd";
    public static String ACTION_UPDATE_ADDRESS_SET_PRIMARY = "set_primary";

    private String action;

    private MuleCountry country;
    private MuleRegion region;
    private MuleTownType townType;
    private MuleTown town;
    private MuleDistrict district;
    private MuleStreetType streetType;
    private MuleStreet street;
    private MuleBuilding building;
    private MuleBuildingAddress buildingAddress;
    private MuleApartment apartment;

    public boolean isDeleteAction() {
        return ACTION_DELETE.equals(action);
    }

    public boolean isInsertAction() {
        return ACTION_INSERT.equals(action);
    }

    public boolean isUpdateAction() {
        return ACTION_UPDATE.equals(action);
    }

    public boolean isUpdateStreetDistrictsAction() {
        return ACTION_UPDATE_STREET_DISTRICTS.equals(action);
    }

    public boolean isUpdateAddressSetPrimaryAction() {
        return ACTION_UPDATE_ADDRESS_SET_PRIMARY.equals(action);
    }

    public boolean isCountry() {
        return country != null;
    }

    public boolean isRegion() {
        return region != null;
    }

    public boolean isTown() {
        return town != null;
    }

    public boolean isDistrict() {
        return district != null;
    }

    public boolean isStreet() {
        return street != null;
    }

    public boolean isStreetType() {
        return streetType != null;
    }

    public boolean isTownType() {
        return townType != null;
    }

    public boolean isBuilding() {
        return building != null;
    }

    public boolean isBuildingAddress() {
        return buildingAddress != null;
    }

    public boolean isApartment() {
        return apartment != null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MuleCountry getCountry() {
        return country;
    }

    public void setCountry(MuleCountry country) {
        this.country = country;
    }

    public MuleRegion getRegion() {
        return region;
    }

    public void setRegion(MuleRegion region) {
        this.region = region;
    }

    public MuleTownType getTownType() {
        return townType;
    }

    public void setTownType(MuleTownType townType) {
        this.townType = townType;
    }

    public MuleTown getTown() {
        return town;
    }

    public void setTown(MuleTown town) {
        this.town = town;
    }

    public MuleDistrict getDistrict() {
        return district;
    }

    public void setDistrict(MuleDistrict district) {
        this.district = district;
    }

    public MuleStreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(MuleStreetType streetType) {
        this.streetType = streetType;
    }

    public MuleStreet getStreet() {
        return street;
    }

    public void setStreet(MuleStreet street) {
        this.street = street;
    }

    public MuleBuilding getBuilding() {
        return building;
    }

    public void setBuilding(MuleBuilding building) {
        this.building = building;
    }

    public MuleBuildingAddress getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(MuleBuildingAddress buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public MuleApartment getApartment() {
        return apartment;
    }

    public void setApartment(MuleApartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("action", action).
                append("country", country).
                append("region", region).
                append("townType", townType).
                append("town", town).
                append("district", district).
                append("streetType", streetType).
                append("street", street).
                append("building", building).
                append("buildingAddress", buildingAddress).
                append("apartment", apartment).
                toString();
    }
}
