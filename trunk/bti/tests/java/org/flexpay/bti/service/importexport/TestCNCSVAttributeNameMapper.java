package org.flexpay.bti.service.importexport;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.bti.persistence.BuildingAttributeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestCNCSVAttributeNameMapper extends SpringBeanAwareTestCase {

	@Autowired
	private AttributeNameMapper nameMapper;

	@Test
	public void testValidateMapper() {

		assertEquals("invalid mapper", BuildingAttributeConfig.ATTR_APARTMENTS_NUMBER, nameMapper.getName(12));
		assertEquals("invalid mapper", BuildingAttributeConfig.ATTR_TOTAL_SQUARE, nameMapper.getName(23));
	}
}
