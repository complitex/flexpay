package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest.apartmentNumberRequest;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest.quittanceNumberRequest;
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
	public void testGetQuittanceDetails() {

		Quittance q = quittanceService.readFull(quittanceStub);
		assertNotNull("Not found quittance: " + quittanceStub, q);

		String number = quittanceNumberService.getNumber(q);

		QuittanceDetailsResponse response = detailsFinder.findQuittance(quittanceNumberRequest(number));

		log.info("Got response {}", response);
	}

	@Test
	public void testGetQuittanceDetailsByApartment() {

		String number = ApplicationConfig.getInstanceId() + "-" + 1L;
		QuittanceDetailsResponse response = detailsFinder.findQuittance(apartmentNumberRequest(number));

		log.info("Got response {}", response);
	}
}
