package org.flexpay.eirc.service.exchange;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TestQuittanceDetailsFinder extends SpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;
	@Autowired
	private QuittanceNumberService quittanceNumberService;
	@Autowired
	@Qualifier("eircQuittanceDetailsFinder")
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
}
