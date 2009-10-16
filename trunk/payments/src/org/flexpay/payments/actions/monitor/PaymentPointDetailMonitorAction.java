package org.flexpay.payments.actions.monitor;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.TradingDayControlPanel;
import org.flexpay.payments.actions.monitor.data.CashboxMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentPointDetailMonitorAction extends FPActionSupport implements InitializingBean {
    
    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private String name;
    private String paymentsCount;
    private String totalSum;
    private List<CashboxMonitorContainer> cashboxes;
    private String paymentPointId;

	private String status;
    private String activity;

	// trading day control panel
	private TradingDayControlPanel tradingDayControlPanel;

    private String update;
    private String updated;

	// required services
    private CashboxService cashboxService;
    private PaymentPointService paymentPointService;
    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private OperationService operationService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {
        Date startDate = DateUtil.now();
        Date finishDate = new Date();

        if (StringUtils.isEmpty(paymentPointId)) {
            log.error("Payment point does not set");
            addActionError(getText("payments.payment_point.detail.error.pp.does_not_set"));
            return ERROR;
        }

        PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(Long.parseLong(paymentPointId)));
        if (paymentPoint == null) {
            log.error("Payment point with id - {} does not exist", paymentPointId);
            addActionError(getText("payments.payment_point.detail.error.inner_error"));
            return ERROR;
        }

		initTradingDayPanel(paymentPoint);

        initPaymentPointInfo(paymentPoint, startDate, finishDate);

        initCashboxesInfo(paymentPoint, startDate, finishDate);

        updated = formatTime.format(new Date());

        return SUCCESS;
    }    

    public long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return summ;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tradingDayControlPanel = new TradingDayControlPanel(processManager, AccounterAssignmentHandler.ACCOUNTER, log);
	}

    private void initPaymentPointInfo(PaymentPoint paymentPoint, Date startDate, Date finishDate) {
        List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(Stub.stub(paymentPoint), startDate, finishDate);
        paymentsCount = String.valueOf(getPaymentsCount(statistics));
        totalSum = String.valueOf(getPaymentsSumm(statistics));
        name = paymentPoint.getName(getLocale());
    }

    private void initCashboxesInfo(@NotNull PaymentPoint paymentPoint, @NotNull Date startDate, @NotNull Date finishDate) {
        List<Cashbox> cbs = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());

        List<OperationTypeStatistics> statistics;
        cashboxes = new ArrayList<CashboxMonitorContainer>();
        if (cbs != null) {
            for (Cashbox cashbox : cbs) {
                statistics = paymentsStatisticsService.operationTypeCashboxStatistics(Stub.stub(cashbox), startDate, finishDate);
                List<Operation> operations = operationService.listLastPaymentOperationsForCashbox(Stub.stub(cashbox), startDate, finishDate);
                String lastPayment = operations != null && !operations.isEmpty() ? formatTime.format(operations.get(0).getCreationDate()) : null;
				String cashierFio = operations != null && !operations.isEmpty() ? operations.get(0).getCashierFio() : null;

                CashboxMonitorContainer container = new CashboxMonitorContainer();
                container.setId(String.valueOf(cashbox.getId()));
                container.setCashbox(cashbox.getName(getLocale()));
                container.setCashierFIO(cashierFio);
                container.setLastPayment(lastPayment);
                container.setPaymentsCount(String.valueOf(getPaymentsCount(statistics)));
                container.setTotalSum(String.valueOf(getPaymentsSumm(statistics)));
                cashboxes.add(container);
            }
        }
    }

	private void initTradingDayPanel(PaymentPoint paymentPoint) {

		tradingDayControlPanel.updatePanel(paymentPoint);
	}

	@NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(String paymentsCount) {
        this.paymentsCount = paymentsCount;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<CashboxMonitorContainer> getCashboxes() {
        return cashboxes;
    }

    public void setCashboxes(List<CashboxMonitorContainer> cashboxes) {
        this.cashboxes = cashboxes;
    }

    public String getPaymentPointId() {
        return paymentPointId;
    }

    public void setPaymentPointId(String paymentPointId) {
        this.paymentPointId = paymentPointId;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

	public TradingDayControlPanel getTradingDayControlPanel() {
		return tradingDayControlPanel;
	}

	public void setTradingDayControlPanel(TradingDayControlPanel tradingDayControlPanel) {
		this.tradingDayControlPanel = tradingDayControlPanel;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
	}

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
        this.paymentsStatisticsService = paymentsStatisticsService;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }
}
