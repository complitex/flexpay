package org.flexpay.orgs.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.test.OrgsSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOrganisationService extends OrgsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService service;

	@Test
	public void testGetOrganizations() {
		assertNotNull("No organizations found in db", service.listOrganizations(new Page<Organization>()));
	}
}
