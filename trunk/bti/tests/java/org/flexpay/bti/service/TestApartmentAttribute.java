package org.flexpay.bti.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttribute;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.TransactionalSpringBeanAwareTestCase;
import org.flexpay.common.util.DateUtil;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestApartmentAttribute extends TransactionalSpringBeanAwareTestCase {

	@Autowired
	private BtiApartmentService apartmentService;
	@Autowired
	private ApartmentAttributeTypeService attributeTypeService;

	@Test
	public void testSetApartmentAttributeTx() throws Exception {

		BtiApartment apartment = apartmentService.readWithAttributes(new Stub<Apartment>(1L));
		assertNotNull("Apartment not found", apartment);

		ApartmentAttributeType type = attributeTypeService.findTypeByName(ApartmentAttributeConfig.ATTR_LIVE_SQUARE);
		assertNotNull("Attribute not found", type);

		ApartmentAttribute attribute = new ApartmentAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("123.23");
		apartment.setNormalAttribute(attribute);
		apartmentService.updateAttributes(apartment);

		Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse("2009-05-25");
		attribute = new ApartmentAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("123.34");
		apartment.setTmpAttributeForDate(attribute, beginDate);
		apartmentService.updateAttributes(apartment);

		ApartmentAttribute persisted = apartment.getAttributeForDate(type, DateUtil.nextMonth(beginDate));
		assertNotNull("just set attribute not found", persisted);
		assertEquals("Invalid attribute setup", "123.34", persisted.getStringValue());
	}

	@Test
	@NotTransactional
	public void testSetApartmentAttribute() throws Exception {

		BtiApartment apartment = apartmentService.readWithAttributes(new Stub<Apartment>(1L));
		assertNotNull("Apartment not found", apartment);

		ApartmentAttributeType type = attributeTypeService.findTypeByName(ApartmentAttributeConfig.ATTR_TOTAL_SQUARE);
		assertNotNull("Attribute not found", type);

		ApartmentAttribute attribute = new ApartmentAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("123.23");
		apartment.setNormalAttribute(attribute);
		apartmentService.updateAttributes(apartment);

		Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse("2009-05-25");
		attribute = new ApartmentAttribute();
		attribute.setAttributeType(type);
		attribute.setStringValue("123.34");
		apartment.setTmpAttributeForDate(attribute, beginDate);
		apartmentService.updateAttributes(apartment);

		ApartmentAttribute persisted = apartment.getAttributeForDate(type, DateUtil.nextMonth(beginDate));
		assertNotNull("just set attribute not found", persisted);
		assertEquals("Invalid attribute setup", "123.34", persisted.getStringValue());
	}
}
