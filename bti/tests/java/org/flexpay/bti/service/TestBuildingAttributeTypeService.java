package org.flexpay.bti.service;

import org.flexpay.bti.dao.BuildingAttributeTypeDaoExt;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnum;
import org.flexpay.bti.persistence.BuildingAttributeTypeSimple;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

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

		BuildingAttributeType type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(1L));
		assertNotNull("Attribute type #1 not found", type);
		assertTrue("type #1 no translations", type.getTranslations().size() > 0);


		type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(2L));
		assertNotNull("Attribute type #2 not found", type);
		assertTrue("Type #2 has invalid type", type instanceof BuildingAttributeTypeSimple);
		assertTrue("type #2 no translations", type.getTranslations().size() > 0);

		type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(3L));
		assertNotNull("Attribute type #3 not found", type);
		assertTrue("Type #3 has invalid type", type instanceof BuildingAttributeTypeEnum);
		assertTrue("type #3 no translations", type.getTranslations().size() > 0);

		BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;
		assertTrue("Enum type is empty", enumType.getValues().size() > 0);
	}

	@Test
	public void testCheckUnique() {

		assertTrue("Not existing name not unique #1", attributeTypeDaoExt.isUniqueTypeName("-----------", null));
		assertTrue("Not existing name not unique #2", attributeTypeDaoExt.isUniqueTypeName("-----------", 0L));
		assertTrue("Not existing name not unique #3", attributeTypeDaoExt.isUniqueTypeName("-----------", 1L));

		assertFalse("Existing name not unique #4", attributeTypeDaoExt.isUniqueTypeName("Цвет здания", null));
		assertFalse("Existing name not unique #5", attributeTypeDaoExt.isUniqueTypeName("Цвет здания", 0L));
		assertFalse("Existing name not unique #6", attributeTypeDaoExt.isUniqueTypeName("Цвет здания", 2L));
		assertTrue("Existing name not unique #7", attributeTypeDaoExt.isUniqueTypeName("Цвет здания", 1L));
	}

	@Test
	public void testUpdateEnum() throws Exception {

		BuildingAttributeType type = attributeTypeService.readFull(new Stub<BuildingAttributeType>(3L));
		BuildingAttributeTypeEnum enumType = (BuildingAttributeTypeEnum) type;

		Map<Integer, String> newValues = map(ar(0, 1), ar("Фанера", "Пенопласт"));
		enumType.setRawValues(newValues);

		assertEquals("Invalid set raw values", 2, enumType.getValues().size());

		attributeTypeService.update(enumType);
		enumType = (BuildingAttributeTypeEnum) attributeTypeService.readFull(new Stub<BuildingAttributeType>(3L));
		assertEquals("Invalid update", 2, enumType.getValues().size());
	}
}
