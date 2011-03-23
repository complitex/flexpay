package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.BankService;
import org.flexpay.orgs.test.OrgsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TestBankHistoryBuilder extends OrgsSpringBeanAwareTestCase {

	@Autowired
	private BankService bankService;
	@Autowired
	private BankHistoryBuilder historyBuilder;
	@Autowired
	private OrganizationHistoryGenerator organizationHistoryGenerator;

	@Test
	public void testPatchBank() {

		Bank bank = bankService.read(TestData.BANK_CN);
		assertNotNull("BANK CN not found", bank);

		organizationHistoryGenerator.generateFor(bank.getOrganization());

		Diff diff = historyBuilder.diff(null, bank);
		Bank copy = new Bank();
		historyBuilder.patch(copy, diff);

		assertEquals("Patch code failed", bank.getBankIdentifierCode(), copy.getBankIdentifierCode());
		assertEquals("Patch account failed", bank.getCorrespondingAccount(), copy.getCorrespondingAccount());

		for (Language language : ApplicationConfig.getLanguages()) {
			BankDescription tr1 = bank.getDescription(language);
			BankDescription tr2 = copy.getDescription(language);
			assertSame("Invalid description patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid desc patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}
	}
}
