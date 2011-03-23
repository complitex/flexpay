package org.flexpay.bti.service;

import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeSimple;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.*;

public class TestBuildingAttributeTypeService extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeTypeService attributeTypeService;
	@Autowired
	private BuildingAttributeTypeDaoExt attributeTypeDaoExt;

	@Test
	public void testReadTypes() {

		attributeTypeService.listTypes(new Page<BuildingAttributeType>());
	}

	@Test
	public void testReadType() {

		BuildingAttributeType type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(4L));
		assertNotNull("Attribute type #4 not found", type);
		assertTrue("Type #4 has no translations", !type.getTranslations().isEmpty());


		type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(5L));
		assertNotNull("Attribute type #5 not found", type);
		assertTrue("Type #5 has invalid type", type instanceof BuildingAttributeTypeSimple);
		assertTrue("type #5 has no translations", !type.getTranslations().isEmpty());

		type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(9L));
		assertNotNull("Attribute type #9 not found", type);
		assertTrue("Type #9 has invalid type", type instanceof BuildingAttributeTypeEnum);
		assertTrue("type #9 no translations", !type.getTranslations().isEmpty());

		BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;
		assertTrue("Enum type is empty", !enumType.getValues().isEmpty());
	}

	@Test
	public void testFindType() {

		assertNotNull("Section number attribute not found", attributeTypeService.findTypeByName("Номер участка"));
		assertNotNull("Habitans count attribute not found", attributeTypeService.findTypeByName("Habitans count"));
		assertNotNull("Habitans count attribute not found", attributeTypeService.findTypeByName("Количество жителей"));
		assertNotNull("ATTR_HOUSE_TYPE not found", attributeTypeService.findTypeByName("ATTR_HOUSE_TYPE"));
	}

	@Test
	public void testCheckUnique() {

		assertTrue("Not existing name not unique #1", attributeTypeDaoExt.isUniqueTypeName("-----------", null));
		assertTrue("Not existing name not unique #2", attributeTypeDaoExt.isUniqueTypeName("-----------", 0L));
		assertTrue("Not existing name not unique #3", attributeTypeDaoExt.isUniqueTypeName("-----------", 1L));

		assertFalse("Existing name not unique #4", attributeTypeDaoExt.isUniqueTypeName("Номер участка", null));
		assertFalse("Existing name not unique #5", attributeTypeDaoExt.isUniqueTypeName("Номер участка", 0L));
		assertFalse("Existing name not unique #6", attributeTypeDaoExt.isUniqueTypeName("Номер участка", 5L));
		assertTrue("Existing name not unique #7", attributeTypeDaoExt.isUniqueTypeName("Номер участка", 4L));
	}

	@Test
	public void testUpdateEnum() throws Exception {

		BuildingAttributeType type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(9L));
		BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;

		Map<Integer, String> newValues = map(ar(0, 1), ar("Пермский", "Московский"));
		enumType.rawValues(newValues);

		assertEquals("Invalid set raw values", 2, enumType.getValues().size());

		attributeTypeService.update(enumType);
		enumType = (BuildingAttributeTypeEnum) attributeTypeService.readFull(new Stub<BuildingAttributeType>(9L));
		assertEquals("Invalid update", 2, enumType.getValues().size());
	}
}
