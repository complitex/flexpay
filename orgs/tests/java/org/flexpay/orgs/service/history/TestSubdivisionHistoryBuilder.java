package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.SubdivisionService;
import org.flexpay.orgs.test.OrgsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.common.persistence.Stub.stub;
import static org.junit.Assert.*;

public class TestSubdivisionHistoryBuilder extends OrgsSpringBeanAwareTestCase {

	@Autowired
	private SubdivisionService subdivisionService;
	@Autowired
	private SubdivisionHistoryBuilder historyBuilder;
	@Autowired
	private OrganizationHistoryGenerator organizationHistoryGenerator;

	@Test
	public void testPatchSubdivision() {

		Subdivision subdivision = subdivisionService.read(TestData.SUBDIVISION_1);
		assertNotNull("Subdivision #1 not found", subdivision);

		ensureRefsHistoryGenerated(subdivision);

		Diff diff = historyBuilder.diff(null, subdivision);
		Subdivision copy = new Subdivision();
		historyBuilder.patch(copy, diff);

		assertEquals("Patch real address failed", subdivision.getRealAddress(), copy.getRealAddress());
		assertEquals("Patch tree path failed", subdivision.getTreePath(), copy.getTreePath());

		assertEquals("Head organization patch failed", subdivision.getHeadOrganization(), copy.getHeadOrganization());
		assertEquals("Juridical person patch failed", subdivision.getJuridicalPerson(), copy.getJuridicalPerson());
		assertEquals("Parent subdivision patch failed", subdivision.getParentSubdivision(), copy.getParentSubdivision());

		for (Language language : ApplicationConfig.getLanguages()) {
			SubdivisionName tr1 = subdivision.getNameTranslation(language);
			SubdivisionName tr2 = copy.getNameTranslation(language);
			assertSame("Invalid name patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid name patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}
		for (Language language : ApplicationConfig.getLanguages()) {
			SubdivisionDescription tr1 = subdivision.getDescriptionTranslation(language);
			SubdivisionDescription tr2 = copy.getDescriptionTranslation(language);
			assertSame("Invalid description patch for lang " + language, tr1 == null, tr2 == null);
			if (tr1 != null && tr2 != null) {
				assertSame("Invalid desc patch for lang " + language, tr1.getName(), tr2.getName());
			}
		}
	}

	private void ensureRefsHistoryGenerated(Subdivision subdivision) {

		organizationHistoryGenerator.generateFor(subdivision.getHeadOrganization());

		Organization juridicalPerson = subdivision.getJuridicalPerson();
		if (juridicalPerson != null) {
			organizationHistoryGenerator.generateFor(juridicalPerson);
		}

		Subdivision parent = subdivision.getParentSubdivision();
		if (parent != null) {
			ensureRefsHistoryGenerated(subdivisionService.read(stub(parent)));
		}
	}
}
