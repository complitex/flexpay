package org.flexpay.payments.action.registry;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TestRegistriesListAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistriesListAction action;

	@Test
	public void testSimple() throws Exception {

		action.setFromDate("1900/01/01");
		assertEquals("Action failed", FPActionSupport.SUCCESS, action.execute());

		action.getPager().setPageNumber(3);
		assertEquals("Action failed", FPActionSupport.SUCCESS, action.execute());
	}
}
