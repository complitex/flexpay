package org.flexpay.eirc.actions;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.eirc.actions.eirc_account.EircAccountsListAction;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestEircAccountListAction extends SpringBeanAwareTestCase {

	@Autowired
	@Qualifier("eircAccountsListAction")
	private EircAccountsListAction accountsListAction;

	@Test
	public void listAccounts() throws Exception {

		UserPreferences prefs = new UserPreferences();
		prefs.setLocale(ApplicationConfig.getDefaultLocale());
		accountsListAction.setUserPreferences(prefs);
		assertEquals("Failed listing accounts", FPActionSupport.SUCCESS, accountsListAction.execute());

		assertFalse("No accounts found", accountsListAction.getEircAccounts().isEmpty());
	}
}
