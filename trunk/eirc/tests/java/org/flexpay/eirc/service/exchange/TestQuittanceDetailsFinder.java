package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.payments.action.outerrequest.request.GetQuittanceDebtInfoRequest;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.action.outerrequest.request.response.Status;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestQuittanceDetailsFinder extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;
	@Autowired
	private QuittanceNumberService quittanceNumberService;
	@Autowired
	@Qualifier ("eircQuittanceDetailsFinder")
	private QuittanceDetailsFinder detailsFinder;

	private static final Stub<Quittance> quittanceStub = new Stub<Quittance>(1L);

	@Test
	public void testGetQuittanceDetails() {

		Quittance q = quittanceService.readFull(quittanceStub);
		assertNotNull("Not found quittance: " + quittanceStub, q);

		String number = quittanceNumberService.getNumber(q);

		StopWatch watch = new StopWatch();
		watch.start();

        GetQuittanceDebtInfoRequest request1 = new GetQuittanceDebtInfoRequest();
        request1.setSearchCriteria(number);
        request1.setSearchType(SearchRequest.TYPE_QUITTANCE_NUMBER);

		SearchResponse response = detailsFinder.findQuittance(request1);
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();

		watch.start();
        GetQuittanceDebtInfoRequest request2 = new GetQuittanceDebtInfoRequest();
        request2.setSearchCriteria(number);
        request2.setSearchType(SearchRequest.TYPE_QUITTANCE_NUMBER);

		response = detailsFinder.findQuittance(request2);
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();

		watch.start();
        GetQuittanceDebtInfoRequest request3 = new GetQuittanceDebtInfoRequest();
        request3.setSearchCriteria(number);
        request3.setSearchType(SearchRequest.TYPE_QUITTANCE_NUMBER);

		response = detailsFinder.findQuittance(request3);
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();

		watch.start();
        GetQuittanceDebtInfoRequest request4 = new GetQuittanceDebtInfoRequest();
        request4.setSearchCriteria(number);
        request4.setSearchType(SearchRequest.TYPE_QUITTANCE_NUMBER);

		response = detailsFinder.findQuittance(request4);
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);

		assertEquals("Invalid response", Status.SUCCESS, response.getStatus());
	}
}
