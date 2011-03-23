package org.flexpay.bti.service.importexport;

import org.flexpay.bti.persistence.building.BuildingAttributeConfig;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.IntegerUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TestCNCSVAttributeNameMapper extends SpringBeanAwareTestCase {

	@Autowired
	private AttributeNameMapper nameMapper;

	@Test
	public void testValidateMapper() {

		assertEquals("invalid mapper", BuildingAttributeConfig.ATTR_APARTMENTS_NUMBER, nameMapper.getName(12));
		assertEquals("invalid mapper", BuildingAttributeConfig.ATTR_TOTAL_SQUARE, nameMapper.getName(23));

		assertEquals("invalid mapper",
				BuildingAttributeConfig.ATTR_HOUSE_TYPE,
				nameMapper.getName(IntegerUtil.parseXLSInt("I")));
		assertEquals("invalid mapper",
				BuildingAttributeConfig.ATTR_LIFTS_NUMBER,
				nameMapper.getName(IntegerUtil.parseXLSInt("AJ")));
		assertEquals("invalid mapper",
				BuildingAttributeConfig.ATTR_DISPETCHER_SYSTEMS_TECH_SUPPORT,
				nameMapper.getName(IntegerUtil.parseXLSInt("CY")));
		assertEquals("invalid mapper",
				BuildingAttributeConfig.ATTR_TBO_CONTAINER_NUMBER,
				nameMapper.getName(IntegerUtil.parseXLSInt("DQ")));
	}
}
