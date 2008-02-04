package org.flexpay.ab.service;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.test.SpringBeanAwareTestCase;

import java.util.List;

public class TestStreetTypeService extends SpringBeanAwareTestCase {

    @Override
    protected void runTest() throws Throwable {
        testGetStreetTypes();
    }

    public void testGetStreetTypes() {
        StreetTypeService service =
                (StreetTypeService) applicationContext.getBean("streetTypeService");

        List<StreetType> streetTypes = service.getEntities();

        assertNotNull("No streets", streetTypes);
//        assertFalse("No street types defined", streetTypes.isEmpty());
    }
}
