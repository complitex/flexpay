package org.flexpay.payments.action;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.payments.actions.registry.RegistryViewPageAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegistryViewAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistryViewPageAction action;

	@Test
	public void testSimple() throws Exception {

		action.setRegistry(new Registry(1L));
		assertEquals("Action failed", FPActionSupport.SUCCESS, action.execute());

		action.getPager().moveLastPage();
//		action.getPager().setPageNumber(30);
		assertEquals("Action failed", FPActionSupport.SUCCESS, action.execute());
	}
}
