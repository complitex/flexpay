package org.flexpay.payments.service.statistics;

import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class TestPaymentsStatisticsService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private PaymentsStatisticsService statisticsService;

	@Test
	public void testGetStatistics() throws Exception {

		Date beginDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 00:00:00");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 23:59:59");

		List<ServicePaymentsStatistics> statisticses = statisticsService.servicePaymentStatistics(beginDate, endDate);
		assertFalse("No stats found", statisticses.isEmpty());
	}
}
