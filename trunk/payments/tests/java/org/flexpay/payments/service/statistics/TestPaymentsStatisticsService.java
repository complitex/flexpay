package org.flexpay.payments.service.statistics;

import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestPaymentsStatisticsService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private PaymentsStatisticsService statisticsService;

	@Test
	public void testGetStatistics() throws Exception {

		Date begin = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 00:00:00");
		Date end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 23:59:59");

		List<ServicePaymentsStatistics> statisticses = statisticsService.servicePaymentStatistics(begin, end);

		System.out.println("Stats: " + statisticses);

		assertFalse("No stats found", statisticses.isEmpty());
	}
}