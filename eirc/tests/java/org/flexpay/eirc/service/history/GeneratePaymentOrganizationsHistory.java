package org.flexpay.eirc.service.history;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.history.CashboxHistoryGenerator;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.flexpay.orgs.service.history.PaymentPointHistoryGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class GeneratePaymentOrganizationsHistory extends EircSpringBeanAwareTestCase {

	// required services
	@Autowired
	private PaymentCollectorService paymentCollectorService;
	@Autowired
	private PaymentPointService paymentPointService;
	@Autowired
	private CashboxService cashboxService;

	// history generators
	@Autowired
	@Qualifier ("paymentCollectorHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<PaymentCollectorDescription, PaymentCollector> paymentCollectorHistoryGenerator;

	@Autowired
	private PaymentPointHistoryGenerator paymentPointHistoryGenerator;

	@Autowired
	private CashboxHistoryGenerator cashboxHistoryGenerator;

	@Test
	public void generatePaymentOrganizationsHistory() {

		generateCashboxes();
	}


	private void generateCashboxes() {
		List<Cashbox> cashboxes = cashboxService.findObjects(new Page<Cashbox>(1000000));
		for (Cashbox cashbox : cashboxes) {

			log.error("Generating history for cashbox #{}", cashbox.getId());

			cashboxHistoryGenerator.generateFor(cashbox);
		}
	}
}
