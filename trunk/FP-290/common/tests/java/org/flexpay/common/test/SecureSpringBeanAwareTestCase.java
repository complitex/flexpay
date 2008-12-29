package org.flexpay.common.test;

import org.springframework.test.context.ContextConfiguration;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
@ContextConfiguration (locations = {
		"file:WEB-INF/applicationContext.xml",
		"applicationContext-security.xml"
})
public abstract class SecureSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

}
