package org.flexpay.ab.service;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.list;

public abstract class Security extends org.flexpay.common.service.Security {

	static {
		USER_HISTORY_SYNCER_AUTHORITIES.addAll(list(
				PERSON_READ,
				PERSON_ADD,
				PERSON_CHANGE,
				PERSON_DELETE,
				APARTMENT_READ,
				APARTMENT_ADD,
				APARTMENT_CHANGE,
				APARTMENT_DELETE,
				BUILDING_READ,
				BUILDING_ADD,
				BUILDING_CHANGE,
				BUILDING_DELETE,
				STREET_READ,
				STREET_ADD,
				STREET_CHANGE,
				STREET_DELETE,
				STREET_TYPE_READ,
				STREET_TYPE_ADD,
				STREET_TYPE_CHANGE,
				STREET_TYPE_DELETE,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_ATTRIBUTE_TYPE_ADD,
				BUILDING_ATTRIBUTE_TYPE_CHANGE,
				BUILDING_ATTRIBUTE_TYPE_DELETE,
				DISTRICT_READ,
				DISTRICT_ADD,
				DISTRICT_CHANGE,
				DISTRICT_DELETE,
				TOWN_TYPE_READ,
				TOWN_TYPE_ADD,
				TOWN_TYPE_CHANGE,
				TOWN_TYPE_DELETE,
				TOWN_READ,
				TOWN_ADD,
				TOWN_CHANGE,
				TOWN_DELETE,
				IDENTITY_TYPE_READ,
				IDENTITY_TYPE_ADD,
				IDENTITY_TYPE_CHANGE,
				IDENTITY_TYPE_DELETE
		));

        USER_MULE_AUTHORITIES.addAll(list(
                APARTMENT_READ,
                APARTMENT_ADD,
                APARTMENT_CHANGE,
                APARTMENT_DELETE,
                BUILDING_READ,
                BUILDING_ADD,
                BUILDING_CHANGE,
                BUILDING_DELETE,
                STREET_READ,
                STREET_ADD,
                STREET_CHANGE,
                STREET_DELETE,
                STREET_TYPE_READ,
                STREET_TYPE_ADD,
                STREET_TYPE_CHANGE,
                STREET_TYPE_DELETE,
                BUILDING_ATTRIBUTE_TYPE_READ,
                BUILDING_ATTRIBUTE_TYPE_ADD,
                BUILDING_ATTRIBUTE_TYPE_CHANGE,
                BUILDING_ATTRIBUTE_TYPE_DELETE,
                DISTRICT_READ,
                DISTRICT_ADD,
                DISTRICT_CHANGE,
                DISTRICT_DELETE,
                TOWN_TYPE_READ,
                TOWN_TYPE_ADD,
                TOWN_TYPE_CHANGE,
                TOWN_TYPE_DELETE,
                TOWN_READ,
                TOWN_ADD,
                TOWN_CHANGE,
                TOWN_DELETE,
                REGION_READ,
                REGION_ADD,
                REGION_CHANGE,
                REGION_DELETE,
                COUNTRY_READ,
                COUNTRY_ADD
        ));
    }

	/**
	 * Touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		// do nothing
	}

}
