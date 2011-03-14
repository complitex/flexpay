package org.flexpay.payments.action.tradingday;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.action.OperatorAWPActionSupport;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.service.TradingDay;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.annotation.Secured;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.payments.service.Roles.TRADING_DAY_ACCOUNTER_ACTION;

public class ProcessTradingDayControlPanelAction extends OperatorAWPActionSupport {

    public static final short COMMAND_CLOSE = 0;
    public static final short COMMAND_OPEN = 1;
    public static final short COMMAND_CLOSE_ALL_CASHBOXES = 2;
    public static final short COMMAND_MARK_CLOSE_DAY = 3;
    public static final short COMMAND_UNMARK_CLOSE_DAY = 4;
    public static final short COMMAND_CONFIRM_CLOSING_DAY = 5;

    private short command;
	private Cashbox cashbox = new Cashbox();
	private PaymentPoint paymentPoint = new PaymentPoint();
    private PaymentCollector paymentCollector = new PaymentCollector();
	private TradingDayControlPanel tradingDayControlPanel = new TradingDayControlPanel();

    private PaymentCollectorService paymentCollectorService;
    private ProcessManager processManager;
	private TradingDay<Cashbox> cashboxTradingDayService;
    private TradingDay<PaymentPoint> paymentPointTradingDayService;
    private TradingDay<PaymentCollector> paymentCollectorTradingDayService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (cashbox != null && cashbox.isNotNew()) {

			Stub<Cashbox> stub = stub(cashbox);
			cashbox = cashboxService.read(stub);
			if (cashbox == null) {
				log.warn("Can't get cashbox with id {} from DB", stub.getId());
				return SUCCESS;
			} else if (cashbox.isNotActive()) {
				log.warn("Cashbox with id {} is disabled", stub.getId());
				return SUCCESS;
			}

            log.debug("cashbox = {}", cashbox);

            if (isAuthenticationGranted(TRADING_DAY_ACCOUNTER_ACTION)) {
                preparePanel(AccounterAssignmentHandler.ACCOUNTER);
            } else {
                preparePanel(PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR);
            }
            tradingDayControlPanel.updatePanel(cashbox.getTradingDayProcessInstanceId());

            log.debug("tradingDayControlPanel = {}", tradingDayControlPanel);

		} else if (paymentPoint != null && paymentPoint.isNotNew()) {

            Stub<PaymentPoint> stub = stub(paymentPoint);
            paymentPoint = paymentPointService.read(stub);
            if (paymentPoint == null) {
                log.warn("Can't get payment point with id {} from DB", stub.getId());
                return SUCCESS;
            } else if (paymentPoint.isNotActive()) {
                log.warn("Payment point with id {} is disabled", stub.getId());
                return SUCCESS;
            }

            if (command == COMMAND_CLOSE_ALL_CASHBOXES) {

                log.debug("Close all payment point cashboxes trayding day ({})", command);

                paymentPointTradingDayService.stopTradingDay(paymentPoint);

            } else {
                log.debug("Command value is incorrect ({}). Skip", command);
            }

        } else if (paymentCollector != null && paymentCollector.isNotNew()) {

            if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
                log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
                return SUCCESS;
            }

            PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) getUserPreferences();
            Long paymentCollectorId = userPreferences.getPaymentCollectorId();
            if (paymentCollectorId == null) {
                log.error("PaymentCollectorId is not defined in preferences of User {} (id = {})", userPreferences.getUsername(), userPreferences.getId());
                return SUCCESS;
            }
            if (!paymentCollectorId.equals(paymentCollector.getId())) {
                log.error("PaymentCollector.id is not valid for this user (id = {})", paymentCollector.getId());
                return SUCCESS;
            }

            Stub<PaymentCollector> stub = stub(paymentCollector);
            paymentCollector = paymentCollectorService.read(stub);
            if (paymentCollector == null) {
                log.warn("Can't get payment collector with id {} from DB", stub.getId());
                return SUCCESS;
            } else if (paymentCollector.isNotActive()) {
                log.warn("Payment collector with id {} is disabled", stub.getId());
                return SUCCESS;
            }

            if (command == COMMAND_CLOSE) {
                disableTradingDay(paymentCollector);
            } else if (command == COMMAND_OPEN) {
                enableTradingDay(paymentCollector);
            } else {
                log.debug("Command value is incorrect ({}). Skip", command);
            }

		} else {
			log.warn("Incorrect cashbox id, payment point id and payment collector id ({}, {}, {})", new Object[] {cashbox, paymentPoint, paymentCollector});
			return SUCCESS;
		}

		return SUCCESS;
	}

    private void preparePanel(String actor) {

        if (tradingDayControlPanel == null) {
            tradingDayControlPanel = new TradingDayControlPanel(processManager, cashboxTradingDayService, actor, log);
        } else {
            tradingDayControlPanel.setProcessManager(processManager);
            tradingDayControlPanel.setActor(actor);
            tradingDayControlPanel.setUserLog(log);
        }

    }

    @Secured(Roles.TRADING_DAY_ADMIN_ACTION)
    private void enableTradingDay(PaymentCollector paymentCollector) throws Exception {

        log.debug("Trying to enable trading day for payment collector {}", paymentCollector.getId());

        paymentCollectorTradingDayService.startTradingDay(paymentCollector);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Secured (Roles.TRADING_DAY_ADMIN_ACTION)
    private void disableTradingDay(PaymentCollector paymentCollector) throws FlexPayException {

        log.debug("Trying to disable trading day for payment collector {}", paymentCollector.getId());

        paymentCollectorTradingDayService.stopTradingDay(paymentCollector);
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

    public void setPaymentCollector(PaymentCollector paymentCollector) {
        this.paymentCollector = paymentCollector;
    }

    public void setTradingDayControlPanel(TradingDayControlPanel tradingDayControlPanel) {
		this.tradingDayControlPanel = tradingDayControlPanel;
	}

    public void setCommand(short command) {
        this.command = command;
    }

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

    @Required
    public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
        this.paymentCollectorService = paymentCollectorService;
    }

    @Required
	public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
		this.cashboxTradingDayService = cashboxTradingDayService;
	}

    @Required
    public void setPaymentPointTradingDayService(TradingDay<PaymentPoint> paymentPointTradingDayService) {
        this.paymentPointTradingDayService = paymentPointTradingDayService;
    }

    @Required
    public void setPaymentCollectorTradingDayService(TradingDay<PaymentCollector> paymentCollectorTradingDayService) {
        this.paymentCollectorTradingDayService = paymentCollectorTradingDayService;
    }
}
