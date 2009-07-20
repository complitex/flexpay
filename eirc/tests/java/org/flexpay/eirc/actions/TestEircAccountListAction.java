package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.actions.eirc_account.EircAccountsListAction;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestEircAccountListAction extends EircSpringBeanAwareTestCase {

	@Autowired
	private EircAccountsListAction accountsListAction;

	@Test
	public void listAccounts() throws Exception {

		assertEquals("Failed listing accounts", FPActionSupport.SUCCESS, accountsListAction.execute());

		assertFalse("No accounts found", accountsListAction.getEircAccounts().isEmpty());
	}
}
