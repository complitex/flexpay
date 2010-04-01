package org.flexpay.payments.actions.certificate;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.persistence.Certificate;
import org.flexpay.payments.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CertificatesListAction extends AccountantAWPWithPagerActionSupport {

	private List<Certificate> certificates = CollectionUtils.list();

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

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
