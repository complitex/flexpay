package org.flexpay.payments.actions.outerrequest;

import org.apache.commons.digester.Digester;
import org.flexpay.common.exception.*;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.payments.actions.outerrequest.request.*;
import org.flexpay.payments.actions.outerrequest.request.response.Status;
import org.flexpay.payments.service.OuterRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class OuterRequestHandlerServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String ENCODING = "UTF-8";

    protected OuterRequestService outerRequestService;
    protected CertificateService certificateService;

    @Override
    public void init() throws ServletException {

        super.init();

        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        outerRequestService = (OuterRequestService) context.getBean("outerRequestService");
        certificateService = (CertificateService) context.getBean("certificateService");

    }

    @Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

		Writer writer = httpServletResponse.getWriter();
        httpServletResponse.setCharacterEncoding(ENCODING);
        Locale locale = httpServletRequest.getLocale();
        httpServletResponse.setLocale(locale);

        log.debug("Parsing outer request started");

        OuterRequest outerRequest = parseRequest(httpServletRequest);
        if (outerRequest == null) {
            log.error("Can't parse outer request!");
            return;
        }

        log.debug("Parsing outer request finished");

        outerRequest.getRequest().setLocale(locale);
		try {
            log.debug("outerRequest = {}", outerRequest);
            log.debug("Processing outer request started");
            processRequest(outerRequest, writer);
            log.debug("Processing outer request finished");
		} catch (Exception e) {
            log.error("Error", e);
            try {
                writer.write(outerRequest.getRequest().buildResponse(Status.UNKNOWN_REQUEST));
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
		}

	}

    private void processRequest(OuterRequest outerRequest, Writer writer) throws IOException {

        Request<?> request = outerRequest.getRequest();

        try {

            log.debug("Authenticate start");
            try {
                if (!request.authenticate(certificateService)) {
                    writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                    log.warn("Bad authentication data");
                    return;
                }
            } catch (UsernameNotFoundException e) {
                writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                return;
            } catch (CertificateNotFoundException e) {
                writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                return;
            } catch (CertificateBlockedException e) {
                writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                return;
            } catch (CertificateExpiredException e) {
                writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                return;
            } catch (InvalidVerifySignatureException e) {
                writer.write(request.buildResponse(Status.INCORRECT_AUTHENTICATION_DATA));
                return;
            }
            log.debug("Authenticate finish");

            log.debug("Validating start");
            if (!request.validate()) {
                writer.write(request.buildResponse(Status.UNKNOWN_REQUEST));
                log.warn("Unknown request");
                return;
            }
            log.debug("Validating finish");

            if (request instanceof GetQuittanceDebtInfoRequest) {
                log.debug("Processing get quittance debt info request: {}", request);
                outerRequestService.getQuittanceDeftInfo((GetQuittanceDebtInfoRequest) request);
            } else if (request instanceof GetDebtInfoRequest) {
                log.debug("Processing get debt info request: {}", request);
                outerRequestService.getDeftInfo((GetDebtInfoRequest) request);
            } else if (request instanceof PayDebtRequest) {
                log.debug("Processing pay request: {}", request);
                outerRequestService.quittancePay((PayDebtRequest) request);
            } else if (request instanceof ReversalPayRequest) {
                log.debug("Processing reversal pay request: {}", request);
                try {
                    outerRequestService.reversalPay((ReversalPayRequest) request);
                } catch (FlexPayException e) {
                    log.error("Pay can't be reverse", e);
                    writer.write(request.buildResponse(Status.REVERSE_IS_NOT_POSSIBLE));
                    return;
                }
            } else if (request instanceof RegistryCommentRequest) {
                log.debug("Processing registry comment request: {}", request);
                outerRequestService.addRegistryComment((RegistryCommentRequest) request);
            } else if (request instanceof GetRegistryListRequest) {
                log.debug("Processing get registry list request: {}", request);
                outerRequestService.getRegistryList((GetRegistryListRequest) request);
            } else if (request instanceof GetServiceListRequest) {
                log.debug("Processing get service list request: {}", request);
                outerRequestService.getServiceList((GetServiceListRequest) request);
            } else {
                log.warn("Can't process request: Unknown request");
                throw new FlexPayException("Unknown request");
            }

            writer.write(request.buildResponse());

        } catch (Exception e) {
            try {
                writer.write(request.buildResponse(Status.INTERNAL_ERROR));
                log.error("Internal error", e);
            } catch (FlexPayException e1) {
                log.error("Error building response", e1);
            }
        } finally {
            log.info("Building response finish");
        }

    }

    private OuterRequest parseRequest(HttpServletRequest httpServletRequest) {

        Digester digester = new Digester();

        digester.addObjectCreate("request", OuterRequest.class);

        GetQuittanceDebtInfoRequest.configParser(digester);
        GetDebtInfoRequest.configParser(digester);
        PayDebtRequest.configParser(digester);
        ReversalPayRequest.configParser(digester);
        RegistryCommentRequest.configParser(digester);
        GetRegistryListRequest.configParser(digester);
        GetServiceListRequest.configParser(digester);

        digester.addBeanPropertySetter("request/login", "login");
        digester.addBeanPropertySetter("request/signature", "signature");

        try {
            return (OuterRequest) digester.parse(httpServletRequest.getInputStream());
        } catch (Exception e) {
            log.error("Error parsing request", e);
        }

        return null;

    }

}
