package org.flexpay.ab.service;

/**
 * Set of predefined security roles for module
 */
public abstract class Roles {

	public static final String PREFIX = "ROLE_AB_";

	public static final String BASIC = PREFIX + "BASIC";

	//	<!-- Region roles -->
	public static final String COUNTRY_READ = PREFIX + "COUNTRY_READ";
	public static final String COUNTRY_ADD = PREFIX + "COUNTRY_ADD";
	public static final String COUNTRY_CHANGE = PREFIX + "COUNTRY_CHANGE";
	public static final String COUNTRY_DELETE = PREFIX + "COUNTRY_DELETE";
//	<!-- Region roles -->
	public static final String REGION_READ = PREFIX + "REGION_READ";
	public static final String REGION_CHANGE = PREFIX + "REGION_CHANGE";
	public static final String REGION_ADD = PREFIX + "REGION_ADD";
	public static final String REGION_DELETE = PREFIX + "REGION_DELETE";
//	<!-- Town roles -->
	public static final String TOWN_READ = PREFIX + "TOWN_READ";
	public static final String TOWN_CHANGE = PREFIX + "TOWN_CHANGE";
	public static final String TOWN_ADD = PREFIX + "TOWN_ADD";
	public static final String TOWN_DELETE = PREFIX + "TOWN_DELETE";
//	<!-- Town type roles -->
	public static final String TOWN_TYPE_READ = PREFIX + "TOWN_TYPE_READ";
	public static final String TOWN_TYPE_CHANGE = PREFIX + "TOWN_TYPE_CHANGE";
	public static final String TOWN_TYPE_ADD = PREFIX + "TOWN_TYPE_ADD";
	public static final String TOWN_TYPE_DELETE = PREFIX + "TOWN_TYPE_DELETE";
//	<!-- District roles -->
	public static final String DISTRICT_READ = PREFIX + "DISTRICT_READ";
	public static final String DISTRICT_CHANGE = PREFIX + "DISTRICT_CHANGE";
	public static final String DISTRICT_ADD = PREFIX + "DISTRICT_ADD";
	public static final String DISTRICT_DELETE = PREFIX + "DISTRICT_DELETE";
//	<!-- Street roles -->
	public static final String STREET_READ = PREFIX + "STREET_READ";
	public static final String STREET_CHANGE = PREFIX + "STREET_CHANGE";
	public static final String STREET_ADD = PREFIX + "STREET_ADD";
	public static final String STREET_DELETE = PREFIX + "STREET_DELETE";
//	<!-- Street type roles -->
	public static final String STREET_TYPE_READ = PREFIX + "STREET_TYPE_READ";
	public static final String STREET_TYPE_CHANGE = PREFIX + "STREET_TYPE_CHANGE";
	public static final String STREET_TYPE_ADD = PREFIX + "STREET_TYPE_ADD";
	public static final String STREET_TYPE_DELETE = PREFIX + "STREET_TYPE_DELETE";
//	<!-- Building attribute type roles -->
	public static final String BUILDING_ATTRIBUTE_TYPE_READ = PREFIX + "BUILDING_ATTRIBUTE_TYPE_READ";
	public static final String BUILDING_ATTRIBUTE_TYPE_CHANGE = PREFIX + "BUILDING_ATTRIBUTE_TYPE_CHANGE";
	public static final String BUILDING_ATTRIBUTE_TYPE_ADD = PREFIX + "BUILDING_ATTRIBUTE_TYPE_ADD";
	public static final String BUILDING_ATTRIBUTE_TYPE_DELETE = PREFIX + "BUILDING_ATTRIBUTE_TYPE_DELETE";
//	<!-- Building roles -->
	public static final String BUILDING_READ = PREFIX + "BUILDING_READ";
	public static final String BUILDING_CHANGE = PREFIX + "BUILDING_CHANGE";
	public static final String BUILDING_ADD = PREFIX + "BUILDING_ADD";
	public static final String BUILDING_DELETE = PREFIX + "BUILDING_DELETE";
//	<!-- Apartment roles -->
	public static final String APARTMENT_READ = PREFIX + "APARTMENT_READ";
	public static final String APARTMENT_CHANGE = PREFIX + "APARTMENT_CHANGE";
	public static final String APARTMENT_ADD = PREFIX + "APARTMENT_ADD";
	public static final String APARTMENT_DELETE = PREFIX + "APARTMENT_DELETE";
//	<!-- Identity type roles -->
	public static final String IDENTITY_TYPE_READ = PREFIX + "IDENTITY_TYPE_READ";
	public static final String IDENTITY_TYPE_CHANGE = PREFIX + "IDENTITY_TYPE_CHANGE";
	public static final String IDENTITY_TYPE_ADD = PREFIX + "IDENTITY_TYPE_ADD";
	public static final String IDENTITY_TYPE_DELETE = PREFIX + "IDENTITY_TYPE_DELETE";
//	<!-- Identity roles -->
	public static final String IDENTITY_READ = PREFIX + "IDENTITY_READ";
	public static final String IDENTITY_CHANGE = PREFIX + "IDENTITY_CHANGE";
	public static final String IDENTITY_ADD = PREFIX + "IDENTITY_ADD";
	public static final String IDENTITY_DELETE = PREFIX + "IDENTITY_DELETE";
//	<!-- Person roles -->
	public static final String PERSON_READ = PREFIX + "PERSON_READ";
	public static final String PERSON_CHANGE = PREFIX + "PERSON_CHANGE";
	public static final String PERSON_ADD = PREFIX + "PERSON_ADD";
	public static final String PERSON_DELETE = PREFIX + "PERSON_DELETE";
}
