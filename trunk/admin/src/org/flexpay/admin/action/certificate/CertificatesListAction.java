package org.flexpay.admin.action.certificate;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class CertificatesListAction extends FPActionWithPagerSupport<Certificate> {

	private List<Certificate> certificates = list();

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		certificates = certificateService.listAllCertificates();

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public boolean hasExpired(Date endDate) {
		return endDate.before(new Date());
	}

	public boolean isExpiring(Date endDate) {
		return endDate.before(DateUtils.addDays(new Date(), ApplicationConfig.getCertificateExpirationWarningPeriod())) && endDate.after(new Date());
	}

    @Required
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

}
