package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.flexpay.payments.persistence.quittance.InfoRequest.apartmentNumberRequest;
import static org.flexpay.payments.persistence.quittance.InfoRequest.quittanceNumberRequest;

import org.flexpay.payments.actions.request.data.DebtsRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestJmsQuittanceDetailsFinder extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;
	@Autowired
	private QuittanceNumberService quittanceNumberService;
	@Autowired
	@Qualifier ("jmsQuittanceDetailsFinder")
	private QuittanceDetailsFinder detailsFinder;

	private static final Stub<Quittance> quittanceStub = new Stub<Quittance>(1L);

	@Test
	public void testGetQuittanceDetails() throws Exception {

		Quittance q = quittanceService.readFull(quittanceStub);
		assertNotNull("Not found quittance: " + quittanceStub, q);

		String number = quittanceNumberService.getNumber(q);

		Requester r1 = new Requester(number);
		Requester r2 = new Requester(number);
		Thread thr1 = new Thread(r1);
		Thread thr2 = new Thread(r2);

		thr1.start();
		Thread.sleep(1);
		thr2.start();

		thr1.join();
		thr2.join();

		log.info("Got responses:\n{}\n{}", r1.response, r2.response);
	}

	private class Requester implements Runnable {

		private QuittanceDetailsResponse response;
		private String number;

		private Requester(String number) {
			this.number = number;
		}

		public void run() {
			response = detailsFinder.findQuittance(quittanceNumberRequest(number, DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST));
		}
	}

	@Test
	public void testGetQuittanceDetailsByApartment() {

		String number = String.valueOf(1L);
		QuittanceDetailsResponse response = detailsFinder.findQuittance(apartmentNumberRequest(number, DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST));

		log.info("Got response {}", response);
	}
}
