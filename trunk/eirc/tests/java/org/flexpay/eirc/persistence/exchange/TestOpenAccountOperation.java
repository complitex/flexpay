package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOpenAccountOperation extends EircSpringBeanAwareTestCase {
	@Autowired
	private RegistryService registryService;

	@Autowired
	private RegistryRecordService recordService;

	@Test
	public void testOK() {
		
	}
}
