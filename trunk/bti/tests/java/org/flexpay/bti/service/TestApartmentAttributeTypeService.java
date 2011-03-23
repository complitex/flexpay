package org.flexpay.bti.service;

import org.flexpay.bti.dao.ApartmentAttributeTypeDaoExt;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeTypeSimple;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestApartmentAttributeTypeService extends SpringBeanAwareTestCase {

	@Autowired
	private ApartmentAttributeTypeService attributeTypeService;
	@Autowired
	private ApartmentAttributeTypeDaoExt attributeTypeDaoExt;

	@Test
	public void testReadType() {

		ApartmentAttributeType type = attributeTypeService.readFull(new Stub<ApartmentAttributeType>(1L));
		assertNotNull("Attribute type #5 not found", type);
		assertTrue("Type #5 has invalid type", type instanceof ApartmentAttributeTypeSimple);
		assertTrue("type #5 has no translations", !type.getTranslations().isEmpty());

	}

	@Test
	public void testFindType() {

		assertNotNull("Habitans count attribute not found", attributeTypeService.findTypeByName("Habitans count"));
		assertNotNull("Habitans count attribute not found", attributeTypeService.findTypeByName("Количество проживающих"));
		assertNotNull("ATTR_NUMBER_OF_HABITANTS not found", attributeTypeService.findTypeByName("ATTR_NUMBER_OF_HABITANTS"));

	}

	@Test
	public void testCheckUnique() {

		assertTrue("Not existing name not unique #1", attributeTypeDaoExt.isUniqueTypeName("-----------", null));
		assertTrue("Not existing name not unique #2", attributeTypeDaoExt.isUniqueTypeName("-----------", 0L));
		assertTrue("Not existing name not unique #3", attributeTypeDaoExt.isUniqueTypeName("-----------", 1L));

	}

}
