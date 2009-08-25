package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest.quittanceNumberRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
		QuittanceDetailsResponse response = detailsFinder.findQuittance(quittanceNumberRequest(number));
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();
		watch.start();
		response = detailsFinder.findQuittance(quittanceNumberRequest(number));
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();
		watch.start();
		response = detailsFinder.findQuittance(quittanceNumberRequest(number));
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);
		watch.reset();
		watch.start();
		response = detailsFinder.findQuittance(quittanceNumberRequest(number));
		watch.stop();
		log.info("Got response {}, time spent {}", response, watch);

		assertEquals("Invalid response", QuittanceDetailsResponse.CODE_SUCCESS, response.getErrorCode());
	}
}
