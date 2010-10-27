package org.flexpay.payments.actions.tradingday;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.service.Roles.PROCESS_DELETE;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.orgs.service.Roles.PAYMENT_COLLECTOR_READ;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.*;

public class ConfirmationTradingDayServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ConfirmationTradingDayServlet.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String PARAM_PAYMENT_POINT_ID = "paymentPointId";
    private static final String PARAM_DATE = "data";
    
    /**
     * Set of authorities names for payments registry
     */
    protected static final List<String> USER_CONFIRM_TRADING_DAY_AUTHORITIES = list(
            PROCESS_READ,
            PROCESS_DELETE,
            PAYMENT_COLLECTOR_READ
    );

    private static final String USER_CONFIRM_TRADING_DAY = "confirm-trading-day";

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        authenticateConfirTradingDay();

        String paymentPointId = httpServletRequest.getParameter(PARAM_PAYMENT_POINT_ID);
        if (isEmpty(paymentPointId)) {
            log.error("Parameter {} missed", PARAM_PAYMENT_POINT_ID);
            httpServletResponse.sendError(420, "Parameter " + PARAM_PAYMENT_POINT_ID + " missed");
            return;
        }
        String date = httpServletRequest.getParameter(PARAM_DATE);
        if (isEmpty(date)) {
            log.error("Parameter {} missed", PARAM_DATE);
            httpServletResponse.sendError(420, "Parameter " + PARAM_DATE + " missed");
            return;
        }

        try {
            Long ppId = Long.parseLong(paymentPointId);
            Date startDate = DateUtil.truncateDay(dateFormat.parse(date));
            Date finishDate = DateUtil.getEndOfThisDay(startDate);

            ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		    PaymentPointService paymentPointService = (PaymentPointService) context.getBean("paymentPointService");

            PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(ppId));
            if (paymentPoint == null) {
                log.error("Payment point with id={} not found", paymentPointId);
                httpServletResponse.sendError(520, "Payment point not found");
                return;
            }

            log.debug("Got payment point with id={}", paymentPointId);

            Long processId = paymentPoint.getTradingDayProcessInstanceId();
            if (processId == null) {
                log.error("Process does not set for payment point with id={}", paymentPointId);
                httpServletResponse.sendError(530, "Trading day is not opened");
                return;
            }

            ProcessManager processManager = (ProcessManager) context.getBean("processManager");

            Process process = processManager.getProcessInstanceInfo(processId);
            if (process == null) {
                log.error("Process instance with id={} not found", processId);
                httpServletResponse.sendError(500, "Internal Server Error");
                return;
            }
            log.debug("Process instance with id={} found for payment point with id={}", processId, paymentPointId);
            if (process.getProcessStartDate().before(startDate)) {
                log.error("Trading day started, but day {} in the future", dateFormat.format(startDate));
                httpServletResponse.sendError(530, "Trading day is not opened");
                return;
            }

			Serializable processStatus = process.getParameters().get(PROCESS_STATUS);
			if (process.getProcessStartDate().before(finishDate) && !Statuses.CLOSED.equals(processStatus)) {
                if (Statuses.WAIT_APPROVE.equals(processStatus)) {
                    log.debug("Try close trading day");
                    TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processId, Transitions.CONFIRM_CLOSING_DAY.getTransitionName(), log);
                    process = processManager.getProcessInstanceInfo(processId);
                    Statuses newProcessStatus = (Statuses) process.getParameters().get(PROCESS_STATUS);
                    if (!Statuses.CLOSED.equals(newProcessStatus)) {
                        log.error("Day is not closed. Current status '{}'", newProcessStatus);
                        httpServletResponse.sendError(500, "Internal Server Error");
                        return;
                    }
                    log.debug("Trading day is closed");
                } else {
                    log.error("Missing process status '{}'", processStatus);
                    httpServletResponse.sendError(540, "Incorrect Trading day status");
                    return;
                }
            } else {
                log.debug("Trading day is closed already");
            }

            httpServletResponse.sendError(HttpServletResponse.SC_OK, "Ok");

        } catch (NumberFormatException ex) {
            log.error("Unknown parameter format: name = {}, value = {}", PARAM_PAYMENT_POINT_ID, paymentPointId);
            httpServletResponse.sendError(430, "Unknown parameter format");
        } catch (ParseException e) {
            log.error("Unknown parameter format: name = {}, value = {}", PARAM_DATE, date);
            httpServletResponse.sendError(430, "Unknown parameter format");
        }
    }

    /**
	 * Do payments registry user authentication
	 */
	private static void authenticateConfirTradingDay() {
		SecurityUtil.authenticate(USER_CONFIRM_TRADING_DAY, USER_CONFIRM_TRADING_DAY_AUTHORITIES);
	}
}
