package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;

public abstract class TestData {

	// test apartments
	public static final Stub<Apartment> IVANOVA_27_329 = new Stub<Apartment>(1L);
	public static final Stub<Apartment> IVANOVA_27_330 = new Stub<Apartment>(2L);
	public static final Stub<Apartment> IVANOVA_27_1 = new Stub<Apartment>(3L);

	// test buildings
	public static final Stub<Building> IVANOVA_27 = new Stub<Building>(1L);
	public static final Stub<BuildingAddress> ADDR_IVANOVA_27 = new Stub<BuildingAddress>(1001L);

	public static final Stub<Building> IVANOVA_2 = new Stub<Building>(2L);
	public static final Stub<BuildingAddress> ADDR_IVANOVA_2 = new Stub<BuildingAddress>(1002L);
	public static final Stub<BuildingAddress> ADDR_DEMAKOVA_220D = new Stub<BuildingAddress>(3L);
	public static final Stub<BuildingAddress> ADDR_ROSSIISKAYA_220R = new Stub<BuildingAddress>(4L);

	// test streets
	public static final Stub<Street> DEMAKOVA = new Stub<Street>(1L);
	public static final Stub<Street> IVANOVA = new Stub<Street>(2L);
	public static final Stub<Street> ROSSIISKAYA = new Stub<Street>(3L);

	// test districts
	public static final Stub<District> DISTRICT_SOVETSKII = new Stub<District>(1L);
}
