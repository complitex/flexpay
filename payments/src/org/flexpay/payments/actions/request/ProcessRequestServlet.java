package org.flexpay.payments.actions.request;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.actions.request.data.DebtInfo;
import org.flexpay.payments.actions.request.data.DebtsRequest;
import org.flexpay.payments.actions.request.data.PayDebt;
import org.flexpay.payments.actions.request.data.ServicePayDetails;
import org.flexpay.payments.persistence.quittance.InfoRequest;
import org.flexpay.payments.persistence.quittance.PayInfoResponse;
import org.flexpay.payments.persistence.quittance.PayRequest;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.service.QuittancePayer;
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

import static org.flexpay.payments.actions.request.data.DebtsRequest.*;
import static org.flexpay.payments.actions.request.util.RequestUtil.*;
import static org.flexpay.payments.actions.request.util.ResponseUtil.*;

public class ProcessRequestServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());

    protected QuittancePayer quittancePayer;
    protected QuittanceDetailsFinder quittanceDetailsFinder;

    @Override
    public void init() throws ServletException {

        super.init();

        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        quittancePayer = (QuittancePayer) context.getBean("quittancePayer");
        quittanceDetailsFinder = (QuittanceDetailsFinder) context.getBean("jmsQuittanceDetailsFinder");
    }

    @Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();
        httpServletResponse.setCharacterEncoding("utf8");

        String requestId = null;
        int reqType = SEARCH_QUITTANCE_DEBT_REQUEST;
		try {
			DebtsRequest request = parseRequest(httpServletRequest);
            reqType = request.getDebtRequestType();
            requestId = reqType == PAY_DEBT_REQUEST ? request.getPayDebt().getRequestId() : request.getDebtInfo().getRequestId();
            processRequest(request, writer);
		} catch (Exception e) {
            log.error("Error", e);
            try {
                writer.write(buildResponse(requestId, reqType, "9", "Unknown request"));
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
		}

	}

    private void processRequest(DebtsRequest request, Writer writer) throws IOException {

        int reqType = request.getDebtRequestType();
        String requestId = reqType == PAY_DEBT_REQUEST ? request.getPayDebt().getRequestId() : request.getDebtInfo().getRequestId();
        log.debug("reqType = {}, requestId = {}", reqType, requestId);

        try {

            log.debug("Authenticate start");
            if (!authenticate(request)) {
                writer.write(buildResponse(requestId, reqType, "8", "Bad authentication data"));
                log.warn("Bad authentication data");
                return;
            }
            log.debug("Authenticate finish");

            // validating request
            log.debug("Validating start");
            if (!validate(request)) {
                writer.write(buildResponse(requestId, reqType, "9", "Unknown request"));
                log.warn("Unknown request");
                return;
            }
            log.debug("Validating finish");

            if (reqType == SEARCH_DEBT_REQUEST || reqType == SEARCH_QUITTANCE_DEBT_REQUEST) {
                InfoRequest infoRequest = convertRequest(request);
                log.debug("Parsing search request: {}", infoRequest);
                QuittanceDetailsResponse quittanceDetailsResponse = quittanceDetailsFinder.findQuittance(infoRequest);
                writer.write(buildSearchResponse(quittanceDetailsResponse, requestId, request.getDebtRequestType(), "1", "OK"));
            } else if (reqType == PAY_DEBT_REQUEST) {
                PayRequest payRequest = createPayRequest(request);
                log.debug("Parsing pay request: {}", payRequest);
                PayInfoResponse payInfoResponse = quittancePayer.quittancePay(payRequest);
                writer.write(buildPayResponse(payInfoResponse, requestId, "1", "OK"));
            } else {
                log.warn("Can't parse request: Unknown request");
                throw new FlexPayException("Unknown request");
            }

        } catch (FlexPayException e) {
            try {
                writer.write(buildResponse(requestId, reqType, "14", "Internal error"));
                log.error("Internal error", e);
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
        }

        log.info("Building response finish");

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
        digester.addBeanPropertySetter("*/searchType", "searchType");
        digester.addBeanPropertySetter("*/searchCriteria", "searchCriteria");
        digester.addBeanPropertySetter("*/requestId", "requestId");
        digester.addBeanPropertySetter("request/payDebt/totalPaySum", "totalPaySum");
        digester.addObjectCreate("request/payDebt/servicePayDetails", ServicePayDetails.class);
        digester.addSetNext("request/payDebt/servicePayDetails", "addServicePayDetails");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/serviceId", "serviceId");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/serviceProviderAccount", "serviceProviderAccount");
        digester.addBeanPropertySetter("request/payDebt/servicePayDetails/paySum", "paySum");
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
