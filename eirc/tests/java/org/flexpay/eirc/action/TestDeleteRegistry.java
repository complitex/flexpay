package org.flexpay.eirc.action;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.TestRegistryUtil;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

public class TestDeleteRegistry extends EircSpringBeanAwareTestCase {

	@Resource(name = "commonTestRegistryUtil")
	private TestRegistryUtil registryUtil;

	@Autowired
    private RegistryService registryService;

	@Test
	public void testDeleteRegistry() {
		assertNotNull("Registry util did not init", registryUtil);
		for (long i = 51; i < 114; i++) {
			Registry registry = registryService.read(new Stub<Registry>(i));
			if (registry != null) {
				registryUtil.delete(registry);
			}
		}
	}
}
