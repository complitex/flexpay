package org.flexpay.admin.action.certificate;

import com.opensymphony.xwork2.Action;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CertificatesListAction extends FPActionWithPagerSupport<Certificate> {

	private List<Certificate> certificates = CollectionUtils.list();

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		certificates = certificateService.listAllCertificates();

		return Action.SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {

		return Action.SUCCESS;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
