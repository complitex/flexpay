package org.flexpay.eirc.process.quittance.report.util;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.quittance.report.ServiceGroup;
import org.flexpay.eirc.process.quittance.report.ServiceTotals;
import org.flexpay.eirc.process.quittance.report.ServiceTotalsBase;
import org.flexpay.eirc.process.quittance.report.SubServiceTotals;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestQuittanceInfoGenerator extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittanceDao quittanceDao;

	// see init_db for ids definitions
	private Stub<EircServiceOrganization> organizationStub = new Stub<EircServiceOrganization>(1L);

	private Date dt_2007_12_01 = new GregorianCalendar(2007, 11, 1).getTime();
	private Date dt_2007_12_31 = new GregorianCalendar(2007, 11, 31).getTime();
	private Date dt_2008_01_01 = new GregorianCalendar(2008, Calendar.JANUARY, 1).getTime();
	private Date dt_2008_02_01 = new GregorianCalendar(2008, 1, 1).getTime();

	private List<Quittance> list(Date begin, Date end) {

//		return quittanceDao.listQuittancesForPrinting(organizationStub.getId(), begin, end);
		return CollectionUtils.list();
	}

	@Test
	@Ignore
	public void testGetUniqueQuittance() throws Throwable {

		List<Quittance> uniqueQuittances = list(dt_2007_12_01, dt_2008_01_01);

		assertEquals("Invalid quittances count", 1, uniqueQuittances.size());
		assertEquals("Invalid quittance fetched", Long.valueOf(1), uniqueQuittances.get(0).getId());
	}

	@Test
	@Ignore
	public void testGet2Quittances() throws Throwable {

		List<Quittance> uniqueQuittances = list(dt_2007_12_01, dt_2008_02_01);

		assertEquals("Invalid quittances count", 2, uniqueQuittances.size());

		// second quittance has greater number, see init_db
		assertEquals("Invalid quittance fetched", Long.valueOf(2L), uniqueQuittances.get(0).getId());
		assertEquals("Invalid quittance fetched", Long.valueOf(1L), uniqueQuittances.get(1).getId());
	}

	@Test
	@Ignore
	public void testMakeServiceGroups1() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2008_01_01).get(0);
		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);

		assertEquals("Invalid groups size", 1, groups.size());
	}

	@Test
	@Ignore
	public void testMakeServiceGroups2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2008_02_01).get(0);
		assertEquals("Invalid quittance fetched", Long.valueOf(2L), q.getId());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);

		assertEquals("Invalid groups size", 2, groups.size());
	}

	@Test
	@Ignore
	public void testBuildServicesTotals1() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2008_01_01).get(0);

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup services totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvartplata
		assertEquals("Invalid services totals", 1, servicesTotals.size());
		// only kvartplata
		assertEquals("Invalid service types totals", 1, serviceTypesTotals.size());
	}

	@Test
	@Ignore
	public void testBuildServicesTotals2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2008_02_01).get(0);

		// two kvartplata + one territory cleanup
		assertEquals("Invalid number of QuittanceDetails fetched", 3, q.getQuittanceDetails().size());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup services totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvartplata
		assertEquals("Invalid services totals", 1, servicesTotals.size());
		// kvartplata only here
		assertEquals("Invalid service types totals", 1, serviceTypesTotals.size());

		ServiceTotals serviceTotals = servicesTotals.values().iterator().next();
		assertNotNull("Service totals is null", serviceTotals);
	}

	@Test
	@Ignore
	public void testBuildSubServicesTotals2() throws Throwable {

		Quittance q = list(dt_2007_12_01, dt_2008_01_01).get(0);

		// two kvartplata + one territory cleanup
		assertEquals("Invalid number of QuittanceDetails fetched", 14, q.getQuittanceDetails().size());

		Map<Service, ServiceGroup> groups = QuittanceInfoGenerator.makeServiceGroups(q);
		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup services totals
		QuittanceInfoGenerator.buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// now setup subservices totals and add them to services totals
		QuittanceInfoGenerator.buildSubservicesTotals(groups, servicesTotals, serviceTypesTotals);

		// only kvartplata
		assertEquals("Invalid services totals", 6, servicesTotals.size());
		// kvartplata and territory cleanup subservice
		assertEquals("Invalid service types totals", 14, serviceTypesTotals.size());

		// check if there are actually subservice totals
		ServiceTotals serviceTotals = servicesTotals.values().iterator().next();
		assertNotNull("Service totals is null", serviceTotals);

		List<SubServiceTotals> subServicesTotals = serviceTotals.getSubServicesTotalsList();
		assertEquals("invalid subservices totals size", 8, subServicesTotals.size());
		assertNotNull("SubserviceTotals is null", subServicesTotals.get(0));
	}
}
