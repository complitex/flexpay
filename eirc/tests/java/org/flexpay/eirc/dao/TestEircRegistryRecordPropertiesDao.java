package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestEircRegistryRecordPropertiesDao extends EircSpringBeanAwareTestCase {

	@Autowired
	private EircRegistryRecordPropertiesDao propertiesDao;

	@Test
	public void testGetApartmentAttributes() {

		Long registryId = 45L;
		Long lowerBound = 300L;
		Long upperBound = 400L;

		List<EircRegistryRecordProperties> props = propertiesDao
				.findWithApartmentAttributes(registryId, lowerBound, upperBound);
		assertNotNull("No props found", props);
	}
}
