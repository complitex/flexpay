package org.flexpay.bti.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.bti.persistence.BuildingAttributeType;
import org.flexpay.bti.persistence.BuildingAttributeTypeSimple;
import org.flexpay.bti.persistence.BuildingAttributeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestBuildingAttributeTypeService extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributeTypeService attributeTypeService;

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
}
