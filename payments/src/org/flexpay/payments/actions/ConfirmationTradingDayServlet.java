package org.flexpay.payments.actions;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.util.DateUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmationTradingDayServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ConfirmationTradingDayServlet.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String PARAM_PAYMENT_POINT_ID = "paymentPointId";
    private static final String PARAM_DATE = "data";

    private static final String PROCESS_STATUS = "PROCESS_STATUS";
    private static final String STATUS_APROVE = "Ожидает подтверждения";
    private static final String STATUS_CLOSED = "Завершен";

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String paymentPointId = httpServletRequest.getParameter(PARAM_PAYMENT_POINT_ID);
        if (StringUtils.isEmpty(paymentPointId)) {
            httpServletResponse.sendError(420, "Parameter " + PARAM_PAYMENT_POINT_ID + " missed");
            return;
        }
        String date = httpServletRequest.getParameter(PARAM_DATE);
        if (StringUtils.isEmpty(date)) {
            httpServletResponse.sendError(420, "Parameter " + PARAM_DATE + " missed");
            return;
        }

        try {
            Long ppId = Long.parseLong(paymentPointId);
            Date startDate = DateUtil.truncateDay(dateFormat.parse(date));
            Date finishDate = DateUtil.getEndOfThisDay(startDate);

            ApplicationContext context = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(getServletContext());

		    PaymentPointService paymentPointService = (PaymentPointService) context.getBean("paymentPointService");

            PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(ppId));
            if (paymentPoint == null) {
                httpServletResponse.sendError(520, "Payment point not found");
                return;
            }
            Long processId = paymentPoint.getTradingDayProcessInstanceId();

            ProcessManager processManager = (ProcessManager) context.getBean("processManager");

            if (processId == null) {
                httpServletResponse.sendError(530, "Trading day is not opened");
                return;
            }

            org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(processId);
            if (process == null) {
                httpServletResponse.sendError(500, "Internal Server Error");
                return;
            }
            if (process.getProcessStartDate().before(startDate)) {
                httpServletResponse.sendError(530, "Trading day is not opened");
                return;
            }
            
            if (process.getProcessStartDate().before(finishDate) && !STATUS_CLOSED.equals(process.getParameters().get(PROCESS_STATUS))) {
                if (STATUS_APROVE.equals(process.getParameters().get(PROCESS_STATUS))) {
                    TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processId, "Подтвердить закрытие", log);
                    process = processManager.getProcessInstanceInfo(processId);
                    String currentStatus = (String) process.getParameters().get(PROCESS_STATUS);
                    if (!STATUS_CLOSED.equals(currentStatus)) {
                        httpServletResponse.sendError(500, "Internal Server Error");
                        return;
                    }
                } else {
                    httpServletResponse.sendError(540, "Incorrect Trading day status");
                    return;
                }
            }

            httpServletResponse.sendError(HttpServletResponse.SC_OK, "Ok");

        } catch (NumberFormatException ex) {
            httpServletResponse.sendError(430, "Unknown parameter format");
        } catch (ParseException e) {
            httpServletResponse.sendError(430, "Unknown parameter format");
        }
    }
}
