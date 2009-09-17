package org.flexpay.payments.actions.monitor;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.actions.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.annotation.Secured;
import org.quartz.JobExecutionException;

import java.math.BigDecimal;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.Serializable;

public class PaymentPointsListMonitorAction extends FPActionWithPagerSupport<PaymentPointMonitorContainer> {
    private static final String PROCESS_DEFINITION_NAME = "TradingDay";
    private static final String DISABLE = "disable";
    private static final String ENABLE = "enable";

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private String selectedPaymentPointName;
    private String filter;
    private String updated;
    private String update;
    private String detail;
    private String paymentPointId;
    private String action;
    private List<PaymentPointMonitorContainer> paymentPoints;

    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private PaymentPointService paymentPointService;
    private OperationService operationService;
    private CashboxService cashboxService;
    private PaymentCollectorService paymentCollectorService;

	/**
     * {@inheritDoc}
     */
    @NotNull
    protected String doExecute() throws Exception {
        Page<PaymentPoint> page = new Page<PaymentPoint>();
        page.setPageSize(getPageSize());
        page.setPageNumber(getPager().getPageNumber());

//        List<org.flexpay.common.process.Process> processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);

		paymentPoints = new ArrayList<PaymentPointMonitorContainer>();
		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
        if (paymentCollectorId == null) {
            log.error("PaymentCollectorId is not defined in preferences of User {}(id={})", 
                    new Object[]{getUserPreferences().getUsername(), getUserPreferences().getId()});
            return ERROR;
        }
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
		if (paymentCollector == null) {
			log.error("No payment collector found with id {}", paymentCollectorId);
			return ERROR;
		}

        Set<PaymentPoint> lPaymentPoints = paymentCollector.getPaymentPoints();
//        List<PaymentPoint> lPaymentPoints = paymentPointService.listPoints(CollectionUtils.arrayStack(), page);
//        for (Process process : processes) {
        for (PaymentPoint paymentPoint : lPaymentPoints) {
            paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoint));
            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            if (String.valueOf(paymentPoint.getId()).equals(paymentPointId)) {
                if (getText(DISABLE).equals(action) && paymentPoint.getTradingDayProcessInstanceId() != null) {
                    disableTradingDay(paymentPoint);
                } if (getText(ENABLE).equals(action) && paymentPoint.getTradingDayProcessInstanceId() == null) {
                    enableTradingDay(paymentCollector, paymentPoint);
                }
            }

            Date startDate = DateUtil.now();
            Date finishDate = new Date();
            log.debug("Start date={} {}, finish date={} {}", new Object[]{DateUtil.format(startDate), formatTime.format(startDate), DateUtil.format(finishDate), formatTime.format(finishDate)});
            
            String status = null;
            if (paymentPoint.getTradingDayProcessInstanceId() != null && paymentPoint.getTradingDayProcessInstanceId() > 0) {
                Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
                if (process != null) {
                    status = (String) process.getParameters().get(TradingDay.PROCESS_STATUS);
                    // PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
                }
            }
            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(Stub.stub(paymentPoint), startDate, finishDate);
            List<Operation> operations = operationService.listLastPaymentOperationsForPaymentPoint(Stub.stub(paymentPoint), startDate, finishDate);

            container.setId(String.valueOf(paymentPoint.getId()));
            container.setName(paymentPoint.getName(getLocale()));
            container.setPaymentsCount(String.valueOf(getPaymentsCount(statistics)));
            container.setTotalSum(String.valueOf(getPaymentsSumm(statistics)));
            container.setStatus(status);

            if (paymentPoint.getTradingDayProcessInstanceId() != null) {
                container.setActionName(getText(DISABLE));
            } else {
                container.setActionName(getText(ENABLE));
            }

            if (operations != null && operations.size() > 0) {
                Operation operation = operations.get(0);
                Cashbox operationCashbox = cashboxService.read(new Stub<Cashbox>(operation.getCashbox()));
                if (operationCashbox != null) {
					container.setCashierFIO(operation.getCashierFio());
                    container.setCashBox(operationCashbox.getName(getLocale()));
                    container.setLastPayment(formatTime.format(operation.getCreationDate()));
                }
            }

            paymentPoints.add(container);
        }

        getPager().setElements(paymentPoints);
        getPager().setTotalElements(page.getTotalNumberOfElements());

        updated = formatTime.format(new Date());
        return SUCCESS;
    }

    @Secured (Roles.TRADING_DAY_ADMIN_ACTION)
    private void enableTradingDay(PaymentCollector paymentCollector, PaymentPoint paymentPoint) throws JobExecutionException {
        Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();

        parameters.put(ExportJobParameterNames.PAYMENT_POINT_ID, paymentPoint.getCollector().getOrganization().getId());
        log.debug("Set paymentPointId {}", paymentPoint.getId());

        //fill begin and end date
        Date beginDate = new Date();
        parameters.put(ExportJobParameterNames.BEGIN_DATE, beginDate);
        log.debug("Set beginDate {}", beginDate);

        parameters.put(ExportJobParameterNames.END_DATE, DateUtil.getEndOfThisDay(new Date()));
        log.debug("Set endDate {}", DateUtil.getEndOfThisDay(new Date()));

        Long recipientOrganizationId = paymentCollector.getOrganization().getId();
        parameters.put(ExportJobParameterNames.ORGANIZATION_ID, recipientOrganizationId);
        log.debug("Set organizationId {}", recipientOrganizationId);

        try {
            paymentPoint.setTradingDayProcessInstanceId(processManager.createProcess(PROCESS_DEFINITION_NAME, parameters));
            paymentPointService.update(paymentPoint);
        } catch (ProcessInstanceException e) {
            log.error("Failed run process trading day", e);
            throw new JobExecutionException(e);
        } catch (ProcessDefinitionException e) {
            log.error("Process trading day not started", e);
            throw new JobExecutionException(e);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Payment point did not save", flexPayExceptionContainer);
            // TODO Kill the process!!!
        }
    }

    @Secured (Roles.TRADING_DAY_ADMIN_ACTION)
    private void disableTradingDay(PaymentPoint paymentPoint) throws FlexPayExceptionContainer {
        Process process = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
        if (process != null) {
            processManager.deleteProcessInstance(process);
        }
        paymentPoint.setTradingDayProcessInstanceId(null);
        paymentPointService.update(paymentPoint);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getSelectedPaymentPointName() {
        return selectedPaymentPointName;
    }

    public void setSelectedPaymentPointName(String selectedPaymentPointName) {
        this.selectedPaymentPointName = selectedPaymentPointName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<PaymentPointMonitorContainer> getPaymentPoints() {
        return paymentPoints;
    }

    public void setPaymentPoints(List<PaymentPointMonitorContainer> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    public String getPaymentPointId() {
        return paymentPointId;
    }

    public void setPaymentPointId(String paymentPointId) {
        this.paymentPointId = paymentPointId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
        this.paymentCollectorService = paymentCollectorService;
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
}
