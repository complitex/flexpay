package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationDescription;
import org.flexpay.orgs.persistence.OrganizationName;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.test.OrgsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TestOrganizationHistoryBuilder extends OrgsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private OrganizationHistoryBuilder historyBuilder;

	@Test
	public void testPatchOrganization() {

		Organization organization = organizationService.readFull(TestData.ORG_TSZH);
		assertNotNull("TSZH not found", organization);

		Diff diff = historyBuilder.diff(null, organization);

		Organization newOrganization = new Organization();
		historyBuilder.patch(newOrganization, diff);

		assertEquals("Invalid kpp patch", organization.getKpp(), newOrganization.getKpp());
		assertEquals("Invalid inn patch",
				organization.getIndividualTaxNumber(), newOrganization.getIndividualTaxNumber());
		assertEquals("Invalid juridical address patch",
				organization.getJuridicalAddress(), newOrganization.getJuridicalAddress());
		assertEquals("Invalid postal address patch",
				organization.getPostalAddress(), newOrganization.getPostalAddress());

		for (Language language : ApplicationConfig.getLanguages()) {
			OrganizationDescription tr1 = organization.getDescriptionTranslation(language);
			OrganizationDescription tr2 = newOrganization.getDescriptionTranslation(language);
			assertSame("Invalid description patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid desc patch for lang " + language, tr1.getName(), tr2.getName() );
			}
		}

		for (Language language : ApplicationConfig.getLanguages()) {
			OrganizationName tr1 = organization.getNameTranslation(language);
			OrganizationName tr2 = newOrganization.getNameTranslation(language);
			assertSame("Invalid name patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid name patch for lang " + language, tr1.getName(), tr2.getName() );
			}
		}
	}
}
