package org.flexpay.payments.actions.request;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.request.data.request.*;
import org.flexpay.payments.actions.request.data.request.data.DebtInfo;
import org.flexpay.payments.actions.request.data.request.data.PayDebt;
import org.flexpay.payments.actions.request.data.request.data.ReversalPay;
import org.flexpay.payments.actions.request.data.request.data.ServicePayDetails;
import org.flexpay.payments.actions.request.data.response.PayInfoResponse;
import org.flexpay.payments.actions.request.data.response.QuittanceDetailsResponse;
import org.flexpay.payments.actions.request.data.response.SimpleResponse;
import org.flexpay.payments.actions.request.data.response.Status;
import org.flexpay.payments.service.OuterRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import static org.flexpay.payments.actions.request.util.RequestUtil.*;
import static org.flexpay.payments.actions.request.util.ResponseUtil.*;

public class ProcessRequestServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());

    protected OuterRequestService outerRequestService;

    @Override
    public void init() throws ServletException {

        super.init();

        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        outerRequestService = (OuterRequestService) context.getBean("outerRequestService");
    }

    @Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();
        httpServletResponse.setCharacterEncoding("utf8");

        String requestId = null;
        RequestType reqType = RequestType.SEARCH_QUITTANCE_DEBT_REQUEST;
        Locale locale = httpServletRequest.getLocale();
        httpServletResponse.setLocale(locale);
		try {
			DebtsRequest request = parseRequest(httpServletRequest);
            reqType = request.getRequestType();
            requestId = getRequestId(request);
            processRequest(request, writer, locale);
		} catch (Exception e) {
            log.error("Error", e);
            try {
                writer.write(buildResponse(requestId, reqType, Status.UNKNOWN_REQUEST, locale));
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
		}

	}

    private void processRequest(DebtsRequest request, Writer writer, Locale locale) throws IOException, FlexPayException {

        RequestType reqType = request.getRequestType();
        String requestId = getRequestId(request);
        log.debug("reqType = {}, requestId = {}", reqType, requestId);

        try {

            log.debug("Authenticate start");
            if (!authenticate(request)) {
                writer.write(buildResponse(requestId, reqType, Status.INCORRECT_AUTHENTICATION_DATA, locale));
                log.warn("Bad authentication data");
                return;
            }
            log.debug("Authenticate finish");

            // validating request
            log.debug("Validating start");
            if (!validate(request)) {
                writer.write(buildResponse(requestId, reqType, Status.UNKNOWN_REQUEST, locale));
                log.warn("Unknown request");
                return;
            }
            log.debug("Validating finish");

            if (reqType == RequestType.SEARCH_DEBT_REQUEST || reqType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
                InfoRequest infoRequest = createSearchRequest(request, locale);
                log.debug("Parsing search request: {}", infoRequest);
                QuittanceDetailsResponse response = outerRequestService.findQuittance(infoRequest);
                writer.write(buildSearchResponse(response, requestId, response.getStatus(), reqType, locale));
            } else if (reqType == RequestType.PAY_DEBT_REQUEST) {
                PayRequest payRequest = createPayRequest(request, locale);
                log.debug("Parsing pay request: {}", payRequest);
                PayInfoResponse response = outerRequestService.quittancePay(payRequest);
                writer.write(buildPayResponse(response, requestId, response.getStatus(), locale));
            } else if (reqType == RequestType.REVERSAL_PAY_REQUEST) {
                ReversalPayRequest reversalPayRequest = createReversalPayRequest(request);
                log.debug("Parsing reversal pay request: {}", reversalPayRequest);
                try {
                    SimpleResponse response = outerRequestService.refund(reversalPayRequest);
                    writer.write(buildReversalPayResponse(response, requestId, response.getStatus(), locale));
                } catch (FlexPayException e) {
                    log.error("Refund is not possible", e);
                    writer.write(buildResponse(requestId, reqType, Status.REFUND_IS_NOT_POSSIBLE, locale));
                }
            } else {
                log.warn("Can't parse request: Unknown request");
                throw new FlexPayException("Unknown request");
            }

        } catch (Exception e) {
            try {
                writer.write(buildResponse(requestId, reqType, Status.INTERNAL_ERROR, locale));
                log.error("Internal error", e);
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
        }

        log.info("Building response finish");

    }

    private String getRequestId(DebtsRequest request) throws FlexPayException {
        RequestType reqType = request.getRequestType();
        if (reqType == RequestType.SEARCH_DEBT_REQUEST || reqType == RequestType.SEARCH_QUITTANCE_DEBT_REQUEST) {
            return request.getDebtInfo().getRequestId();
        } else if (reqType == RequestType.PAY_DEBT_REQUEST) {
            return request.getPayDebt().getRequestId();
        } else if (reqType == RequestType.REVERSAL_PAY_REQUEST) {
            return request.getReversalPay().getRequestId();
        } else {
            log.warn("Unknown request. Response error");
            throw new FlexPayException("Unknown request");
        }
    }

    private DebtsRequest parseRequest(HttpServletRequest httpServletRequest) throws FlexPayException {

        Digester digester = new Digester();

        digester.addObjectCreate("request", DebtsRequest.class);
        digester.addObjectCreate("request/getQuittanceDebtInfo", DebtInfo.class);
        digester.addSetNext("request/getQuittanceDebtInfo", "setQuittanceDebtInfo");
        digester.addObjectCreate("request/getDebtInfo", DebtInfo.class);
        digester.addSetNext("request/getDebtInfo", "setDebtInfo");
        digester.addObjectCreate("request/payDebt", PayDebt.class);
        digester.addSetNext("request/payDebt", "setPayDebt");
        digester.addObjectCreate("request/reversalPay", ReversalPay.class);
        digester.addSetNext("request/reversalPay", "setReversalPay");
        digester.addBeanPropertySetter("*/searchType", "searchType");
        digester.addBeanPropertySetter("*/searchCriteria", "searchCriteria");
        digester.addBeanPropertySetter("*/requestId", "requestId");
        digester.addBeanPropertySetter("request/payDebt/totalPaySum", "totalPaySum");
        digester.addObjectCreate("request/payDebt/servicePayDetails", ServicePayDetails.class);
        digester.addSetNext("request/payDebt/servicePayDetails", "addServicePayDetails");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/serviceId", "serviceId");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/serviceProviderAccount", "serviceProviderAccount");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/paySum", "paySum");
        digester.addBeanPropertySetter("request/reversalPay/operationId", "operationId");
        digester.addBeanPropertySetter("request/reversalPay/totalPaySum", "totalPaySum");
        digester.addBeanPropertySetter("request/login", "login");
        digester.addBeanPropertySetter("request/signature", "signature");

        try {
            return (DebtsRequest) digester.parse(httpServletRequest.getInputStream());
        } catch (Exception e) {
            log.error("Error parsing request", e);
            throw new FlexPayException("Error parsing request", e);
        }
    }

}
