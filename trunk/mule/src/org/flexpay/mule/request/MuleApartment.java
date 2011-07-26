package org.flexpay.mule.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.exception.FlexPayException;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class MuleApartment extends MuleIdObject {

    public final String APARTMENTS_SEPARATOR = ",";
    public final String INTERVAL_SEPARATOR = "\\.\\.";

    private String number;
    private Long buildingId;

    public Set<Apartment> convert() throws FlexPayException {

        Set<Apartment> apartments = set();

        String[] apIntervals = number.contains(APARTMENTS_SEPARATOR) ? number.trim().split(APARTMENTS_SEPARATOR) : new String[] {number.trim()};

        for (String interval : apIntervals) {

            String[] apValues = interval.contains("..") ? interval.trim().split(INTERVAL_SEPARATOR) : new String[] {interval.trim()};

            if (apValues.length == 1) {
                apartments.add(createApartment(apValues[0]));
            } else if (apValues.length == 2) {

                int start = Integer.parseInt(apValues[0].trim());
                int finish = Integer.parseInt(apValues[1].trim());

                for (int i = start; i <= finish; i++) {
                    apartments.add(createApartment(i + ""));
                }
            }
        }

        return apartments;
    }

    private Apartment createApartment(String apartmentNumber) {
        Apartment ap = Apartment.newInstance();
        ap.setBuilding(new Building(buildingId));
        ap.setNumber(apartmentNumber);
        return ap;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("ids", ids).
                append("number", number).
                append("buildingId", buildingId).
                toString();
    }
}
