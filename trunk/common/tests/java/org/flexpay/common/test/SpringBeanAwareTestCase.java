package org.flexpay.common.test;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Base class for all SpringFramework initialised beans aware tests
 */
public abstract class SpringBeanAwareTestCase extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[] {
                "file:WEB-INF/applicationContext.xml"
        };
    }
}
