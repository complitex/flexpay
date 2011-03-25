package org.flexpay.bti.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttribute;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeConfig;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestApartmentAttribute extends BtiSpringBeanAwareTestCase {

	@Autowired
	private BtiApartmentService apartmentService;
	@Autowired
	private ApartmentAttributeTypeService attributeTypeService;
	@Autowired
	private PlatformTransactionManager txManager;

	@Test
	public void testSetApartmentAttributeTx() throws Exception {

		TransactionStatus txStatus = txManager.getTransaction(null);
		try {
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

			txManager.commit(txStatus);
		} catch (Throwable t) {
			txManager.rollback(txStatus);
		}
	}

	@Test
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
