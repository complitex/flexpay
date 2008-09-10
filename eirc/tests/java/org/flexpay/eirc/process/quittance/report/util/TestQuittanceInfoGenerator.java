package org.flexpay.eirc.process.quittance.report.util;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.TransactionalSpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.quittance.report.ServiceGroup;
import org.flexpay.eirc.process.quittance.report.ServiceTotals;
import org.flexpay.eirc.process.quittance.report.ServiceTotalsBase;
import org.flexpay.eirc.process.quittance.report.SubServiceTotals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class TestQuittanceInfoGenerator extends TransactionalSpringBeanAwareTestCase {

	@Autowired
	private QuittanceDao quittanceDao;

	// see init_db for ids definitions
	private Stub<ServiceOrganisation> organisationStub = new Stub<ServiceOrganisation>(1L);

	private Date dt_2007_12_01 = new GregorianCalendar(2007, 11, 1).getTime();
	private Date dt_2007_01_01 = new GregorianCalendar(2008, 0, 1).getTime();
	private Date dt_2007_02_01 = new GregorianCalendar(2008, 1, 1).getTime();

	private List<Quittance> list(Date begin, Date end) {

		return quittanceDao.findByServiceOrganisationAndDate(organisationStub.getId(), begin, end);
	}

	@Test
	public void testGetUniqueQuittance() throws Throwable {

		List<Quittance> uniqueQuittances = list(dt_2007_12_01, dt_2007_01_01);

		assertEquals("Invalid quittances count", 1, uniqueQuittances.size());
		assertEquals("Invalid quittance fetched", Long.valueOf(1), uniqueQuittances.get(0).getId());
	}

	@Test
	public void testGet2Quittances() throws Throwable {

		List<Quittance> uniqueQuittances = list(dt_2007_12_01, dt_2007_02_01);

		assertEquals("Invalid quittances count", 2, uniqueQuittances.size());

		// second quittance has greater number, see init_db
		assertEquals("Invalid quittance fetched", Long.valueOf(2L), uniqueQuittances.get(0).getId());
		assertEquals("Invalid quittance fetched", Long.valueOf(1L), uniqueQuittances.get(1).getId());
	}

	@Test
	public void testMakeServiceGroups1() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2007_01_01).get(0);
		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);

		assertEquals("Invalid groups size", 1, groups.size());
	}

	@Test
	public void testMakeServiceGroups2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2007_02_01).get(0);
		assertEquals("Invalid quittance fetched", Long.valueOf(2L), q.getId());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);

		assertEquals("Invalid groups size", 2, groups.size());
	}

	@Test
	public void testBuildServicesTotals1() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2007_01_01).get(0);

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup servises totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvarplata
		assertEquals("Invalid services totals", 1, servicesTotals.size());
		// only kvarplata
		assertEquals("Invalid service types totals", 1, serviceTypesTotals.size());
	}

	@Test
	public void testBuildServicesTotals2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2007_02_01).get(0);

		// two kvarplata + one teritory cleanup
		assertEquals("Invalid number of QuittanceDetails fetched", 3, q.getQuittanceDetails().size());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup servises totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvarplata
		assertEquals("Invalid services totals", 1, servicesTotals.size());
		// kvarplata only here
		assertEquals("Invalid service types totals", 1, serviceTypesTotals.size());
	}

	@Test
	public void testBuildSubServicesTotals2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2007_02_01).get(0);

		// two kvarplata + one teritory cleanup
		assertEquals("Invalid number of QuittanceDetails fetched", 3, q.getQuittanceDetails().size());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup servises totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// now setup subservices totals and add them to services totals
		QuittanceInfoGenerator.buildSubservicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvarplata
		assertEquals("Invalid services totals", 1, servicesTotals.size());
		// kvarplata and territory cleanup subservice
		assertEquals("Invalid service types totals", 2, serviceTypesTotals.size());

		// check if there are actually subservice totals
		ServiceTotals serviceTotals = servicesTotals.get(0);
		assertNotNull("Service totals is null", serviceTotals);

		List<SubServiceTotals> subServicesTotals = serviceTotals.getSubServicesTotalsList();
		assertEquals("invalid subservices totals size", 1, subServicesTotals.size());
	}
}
