package org.flexpay.eirc.process.quittance;

import org.flexpay.ab.persistence.TestData;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestGenerateQuittanceJob extends EircSpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittanceJob job;
	@Autowired
	private ServiceOrganizationService organizationService;

	@Test
	public void testExecute() throws Exception {
		List<ServiceOrganization> organizations = organizationService.listServiceOrganizations();
		for (ServiceOrganization org : organizations) {

			Map<Serializable, Serializable> params = CollectionUtils.map();

			params.put(GenerateQuittanceJob.PARAM_DATE_FROM, DateUtil.parseBeginDate("2008/01/01"));
			params.put(GenerateQuittanceJob.PARAM_DATE_TILL, DateUtil.parseEndDate("2008/01/01"));
			params.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, org.getId());
			params.put(GenerateQuittanceJob.PARAM_TOWN_ID, TestData.TOWN_NSK.getId());

			assertEquals("Failed generating quittances for organization #" + org.getId(),
					Job.RESULT_NEXT, job.execute(params));
		}
	}
}
