package org.flexpay.payments.process.export.handler2;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.common.process.handler2.FTPUploadWorkItemHandler;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.util.registries.EndOperationDayRegistryGenerator;
import org.flexpay.payments.util.registries.ExportBankPaymentsRegistry;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

public class GenerateEndOperationDayRegistryHandler extends ProcessInstanceExecuteHandler {

	public final static String RESULT_NO_REGISTRY_CREATED = "No registry created";
    public final static String RESULT_SEND_REGISTRY_BY_EMAIL = "Send registry by email";
    public final static String RESULT_SEND_REGISTRY_BY_FTP = "Send registry by ftp";

    private static final String FTP_PREFIX = "ftp://";
    private static final String FTP_FORMAT = "ftp://user:pass@host[:port]/path";

	private OrganizationService organizationService;
	private PaymentCollectorService paymentCollectorService;
	private EndOperationDayRegistryGenerator registryGenerator;
	private ExportBankPaymentsRegistry exportBankPaymentsRegistry;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		log.info("Start process generating end operation day registry and save it to file...");

		PaymentCollector paymentCollector = getPaymentCollector(parameters);
		if (paymentCollector == null) {
			return RESULT_ERROR;
		}

		log.debug("Payment collector found: {}", paymentCollector);

		Organization organization = getOrganization(parameters);
		if (organization == null) {
			return RESULT_ERROR;
		}

		log.debug("Organization found: {}", organization);

		Date beginDate = (Date) parameters.get(BEGIN_DATE);
		Date endDate = (Date) parameters.get(END_DATE);

		log.debug("Begin date: {}, End date: {}", beginDate, endDate);

		Registry registry = registryGenerator.generate(paymentCollector, organization, beginDate, endDate);
		if (registry == null) {
			return RESULT_NO_REGISTRY_CREATED;
		}

		FPFile file = exportBankPaymentsRegistry.generateAndAttachFile(registry);
		parameters.put(FILE_ID, file.getId());

        String emailOrFTP = paymentCollector.getEmail();

        if (StringUtils.startsWith(emailOrFTP, FTP_PREFIX)) {

            String subFTP = StringUtils.removeStart(emailOrFTP, FTP_PREFIX);
            String[] ftp = StringUtils.split(subFTP, '@');
            if (ftp.length != 2) {
                failedFTPFormat(paymentCollector, emailOrFTP);
                return RESULT_ERROR;
            }
            String[] userAutharization = StringUtils.split(ftp[0], ':');
            if (userAutharization.length != 2) {
                failedFTPFormat(paymentCollector, emailOrFTP);
                return RESULT_ERROR;
            }
            log.debug("ftp: {}", ftp);
            int i = StringUtils.indexOf(ftp[1], '/');
            if (i <= 0) {
                failedFTPFormat(paymentCollector, emailOrFTP);
                return RESULT_ERROR;
            }
            String url1 = StringUtils.substring(ftp[1], 0, i);
            log.debug("url: {}", url1);
            String[] url = StringUtils.split(url1, ':');
            if (url.length > 2) {
                failedFTPFormat(paymentCollector, emailOrFTP);
                return RESULT_ERROR;
            }

            String path = StringUtils.substring(ftp[1], i);
            if (!StringUtils.endsWith(path, "/")) {
                path += "/";
            }
            path += file.getName();

            parameters.put(FTPUploadWorkItemHandler.USER, userAutharization[0]);
            parameters.put(FTPUploadWorkItemHandler.PASSWORD, userAutharization[1]);
            parameters.put(FTPUploadWorkItemHandler.REMOTE_FILE_PATH, path);
            parameters.put(FTPUploadWorkItemHandler.HOST, url[0]);
            if (url.length > 1) {
                parameters.put(FTPUploadWorkItemHandler.PORT, url[1]);
            }

            return RESULT_SEND_REGISTRY_BY_FTP;
        }

		parameters.put(EMAIL, emailOrFTP);

		log.info("ProcessInstance end operation day registry and save it to file finished...");

		return RESULT_SEND_REGISTRY_BY_EMAIL;
	}

    private void failedFTPFormat(PaymentCollector paymentCollector, String emailOrFTP) {
        log.error("Failed ftp: '{}' of payment collector {}. Required format: {}", new Object[]{emailOrFTP, paymentCollector.getId(), FTP_FORMAT});
    }

    private PaymentCollector getPaymentCollector(Map<String, Object> parameters) {
		Long paymentCollectorId = (Long) parameters.get(PAYMENT_COLLECTOR_ID);
        if (paymentCollectorId == null) {
            log.error("Can't find {} paramenter", PAYMENT_COLLECTOR_ID);
            return null;
        }
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

		if (paymentCollector == null) {
			log.error("Payment point was not found (searching by id {})", parameters.get(PAYMENT_COLLECTOR_ID));
		}
		return paymentCollector;
	}

	private Organization getOrganization(Map<String, Object> parameters) {
		Long organizationId = (Long) parameters.get(ORGANIZATION_ID);
        if (organizationId == null) {
            log.error("Can't find {} paramenter", ORGANIZATION_ID);
            return null;
        }
		Organization organization = organizationService.readFull(new Stub<Organization>(organizationId));

		if (organization == null) {
			log.error("Organization was not found (searching by id {})", parameters.get(ORGANIZATION_ID));
		}
		return organization;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryGenerator(EndOperationDayRegistryGenerator registryGenerator) {
		this.registryGenerator = registryGenerator;
	}

	@Required
	public void setExportBankPaymentsRegistry(ExportBankPaymentsRegistry exportBankPaymentsRegistry) {
		this.exportBankPaymentsRegistry = exportBankPaymentsRegistry;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
