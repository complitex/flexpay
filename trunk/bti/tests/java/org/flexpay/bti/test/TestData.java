package org.flexpay.bti.test;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.common.persistence.Stub;

public abstract class TestData {

	public static final Stub<BuildingAttributeGroup> GROUP_1 = new Stub<BuildingAttributeGroup>(1L);

	// attribute types
	public static final Stub<BuildingAttributeType> ATTR_SECTION_NUMBER = new Stub<BuildingAttributeType>(4L);
	public static final Stub<BuildingAttributeType> ATTR_HOUSE_TYPE_ENUM = new Stub<BuildingAttributeType>(9L);
}
