package org.flexpay.payments.actions;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR;
import static org.flexpay.payments.process.handlers.AccounterAssignmentHandler.ACCOUNTER;

public class ProcessTradingDayControlPanelAction extends CashboxCookieActionSupport {

	private Cashbox cashbox = new Cashbox();
	private PaymentPoint paymentPoint = new PaymentPoint();
	private TradingDayControlPanel tradingDayControlPanel = new TradingDayControlPanel();

	private CashboxService cashboxService;
	private PaymentPointService paymentPointService;
	private ProcessManager processManager;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		String actor;
		Stub<PaymentPoint> paymentPointStub;

		if (cashbox != null && cashbox.isNotNew()) {
			actor = PAYMENT_COLLECTOR;

			Stub<Cashbox> stub = stub(cashbox);
			cashbox = cashboxService.read(stub);
			if (cashbox == null) {
				log.warn("Can't get cashbox with id {} from DB", stub.getId());
				return SUCCESS;
			} else if (cashbox.isNotActive()) {
				log.warn("Cashbox with id {} is disabled", stub.getId());
				return SUCCESS;
			}

			paymentPointStub = stub(cashbox.getPaymentPoint());

		} else if (paymentPoint != null && paymentPoint.isNotNew()) {
			actor = ACCOUNTER;
			paymentPointStub = stub(paymentPoint);
		} else {
			log.warn("Incorrect cashbox id and payment point id ({}, {})", cashbox, paymentPoint);
			return SUCCESS;
		}

		paymentPoint = paymentPointService.read(paymentPointStub);
		if (paymentPoint == null) {
			log.warn("Can't get payment point with id {} from DB", paymentPointStub.getId());
			return SUCCESS;
		}

		if (tradingDayControlPanel == null) {
			tradingDayControlPanel = new TradingDayControlPanel(processManager, actor, log);
		} else {
			tradingDayControlPanel.setProcessManager(processManager);
			tradingDayControlPanel.setActor(actor);
			tradingDayControlPanel.setUserLog(log);
		}

		tradingDayControlPanel.updatePanel(paymentPoint);

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public void setTradingDayControlPanel(TradingDayControlPanel tradingDayControlPanel) {
		this.tradingDayControlPanel = tradingDayControlPanel;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
