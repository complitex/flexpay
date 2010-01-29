package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.actions.TradingDayControlPanel;
import org.flexpay.payments.actions.monitor.data.CashboxMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.payments.actions.monitor.PaymentPointDetailMonitorAction.getPaymentsCount;
import static org.flexpay.payments.actions.monitor.PaymentPointDetailMonitorAction.getPaymentsSumm;

public class PaymentPointCashboxesListAction extends CashboxCookieWithPagerActionSupport<CashboxMonitorContainer> implements InitializingBean {

	private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

	private PaymentPoint paymentPoint = new PaymentPoint();
	private List<CashboxMonitorContainer> cashboxes = list();

	private TradingDayControlPanel tradingDayControlPanel;

	private ProcessManager processManager;
	private CashboxService cashboxService;
	private OperationService operationService;
	private PaymentPointService paymentPointService;
	private PaymentsStatisticsService paymentsStatisticsService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (paymentPoint == null || paymentPoint.isNew()) {
			log.warn("Incorrect payment point id {}", paymentPoint);
			addActionError(getText("payments.payment_point.detail.error.pp.does_not_set"));
			return ERROR;
		}

		Stub<PaymentPoint> stub = stub(paymentPoint);
		paymentPoint = paymentPointService.read(stub);
		if (paymentPoint == null) {
			log.warn("Can't get Payment point with id {} from DB", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		} else if (paymentPoint.isNotActive()) {
			log.warn("Payment point with id {} is disabled", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		}

		tradingDayControlPanel.updatePanel(paymentPoint);

		Date startDate = now();
		Date finishDate = new Date();

		List<Cashbox> cbs = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());
		for (Cashbox cashbox : cbs) {
			List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypeCashboxStatistics(stub(cashbox), startDate, finishDate);
			List<Operation> operations = operationService.listLastPaymentOperationsForCashbox(stub(cashbox), startDate, finishDate);
			boolean hasOperations = operations != null && !operations.isEmpty();
			String lastPayment = hasOperations ? formatTime.format(operations.get(0).getCreationDate()) : null;
			String cashierFio = hasOperations ? operations.get(0).getCashierFio() : null;

			CashboxMonitorContainer container = new CashboxMonitorContainer();
			container.setId(cashbox.getId());
			container.setCashbox(cashbox.getName(getLocale()));
			container.setCashierFIO(cashierFio);
			container.setLastPayment(lastPayment);
			container.setPaymentsCount(getPaymentsCount(statistics));
			container.setTotalSumm(String.valueOf(getPaymentsSumm(statistics)));

			cashboxes.add(container);
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return ERROR;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tradingDayControlPanel = new TradingDayControlPanel(processManager, AccounterAssignmentHandler.ACCOUNTER, log);
	}

	public List<CashboxMonitorContainer> getCashboxes() {
		return cashboxes;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public TradingDayControlPanel getTradingDayControlPanel() {
		return tradingDayControlPanel;
	}

	public void setTradingDayControlPanel(TradingDayControlPanel tradingDayControlPanel) {
		this.tradingDayControlPanel = tradingDayControlPanel;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
		this.paymentsStatisticsService = paymentsStatisticsService;
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
