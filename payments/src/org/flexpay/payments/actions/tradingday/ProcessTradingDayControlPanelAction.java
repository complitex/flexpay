package org.flexpay.payments.actions.tradingday;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.process.handlers.PaymentCollectorAssignmentHandler.PAYMENT_COLLECTOR;

public class ProcessTradingDayControlPanelAction extends OperatorAWPActionSupport {

    public static final short COMMAND_STOP = 0;
    public static final short COMMAND_START = 1;

    private short command;
	private Cashbox cashbox = new Cashbox();
	private PaymentPoint paymentPoint = new PaymentPoint();
    private PaymentCollector paymentCollector = new PaymentCollector();
	private TradingDayControlPanel tradingDayControlPanel = new TradingDayControlPanel();

    private PaymentCollectorService paymentCollectorService;
    private ProcessManager processManager;
	private TradingDay<Cashbox> cashBoxTradingDayService;
    private TradingDay<PaymentPoint> paymentPointTradingDayService;
    private TradingDay<PaymentCollector> paymentCollectorTradingDayService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (cashbox != null && cashbox.isNotNew()) {

			String actor = PAYMENT_COLLECTOR;

			Stub<Cashbox> stub = stub(cashbox);
			cashbox = cashboxService.read(stub);
			if (cashbox == null) {
				log.warn("Can't get cashbox with id {} from DB", stub.getId());
				return SUCCESS;
			} else if (cashbox.isNotActive()) {
				log.warn("Cashbox with id {} is disabled", stub.getId());
				return SUCCESS;
			}

            log.debug("actor = {}, cashbox = {}", actor, cashbox);

            if (tradingDayControlPanel == null) {
                tradingDayControlPanel = new TradingDayControlPanel(processManager, cashBoxTradingDayService, actor, log);
            } else {
                tradingDayControlPanel.setProcessManager(processManager);
                tradingDayControlPanel.setActor(actor);
                tradingDayControlPanel.setUserLog(log);
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

            if (command == COMMAND_STOP) {
                log.debug("Stopping payment point trayding day");
                paymentPointTradingDayService.stopTradingDay(paymentPoint);
            } else if (command == COMMAND_START) {
                log.debug("Starting payment point trayding day");
                paymentPointTradingDayService.startTradingDay(paymentPoint);
            } else {
                log.debug("command value is null or incorrect. Skip");
            }

        } else if (paymentCollector != null && paymentCollector.isNotNew()) {

            Stub<PaymentCollector> stub = stub(paymentCollector);
            paymentCollector = paymentCollectorService.read(stub);
            if (paymentCollector == null) {
                log.warn("Can't get payment collector with id {} from DB", stub.getId());
                return SUCCESS;
            } else if (paymentCollector.isNotActive()) {
                log.warn("Payment collector with id {} is disabled", stub.getId());
                return SUCCESS;
            }

            if (command == COMMAND_STOP) {
                log.debug("Stopping payment collector trayding day");
                paymentCollectorTradingDayService.stopTradingDay(paymentCollector);
            } else if (command == COMMAND_START) {
                log.debug("Starting payment collector trayding day");
                paymentCollectorTradingDayService.startTradingDay(paymentCollector);
            } else {
                log.debug("command value is null or incorrect. Skip");
            }

		} else {
			log.warn("Incorrect cashbox id, payment point id and payment collector id ({}, {}, {})", new Object[] {cashbox, paymentPoint, paymentCollector});
			return SUCCESS;
		}

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
	public void setCashBoxTradingDayService(TradingDay<Cashbox> cashBoxTradingDayService) {
		this.cashBoxTradingDayService = cashBoxTradingDayService;
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
