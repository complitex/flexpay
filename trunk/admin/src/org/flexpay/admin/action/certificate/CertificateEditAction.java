package org.flexpay.admin.action.certificate;

import com.opensymphony.xwork2.Action;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.CertificateService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CertificateEditAction extends FPActionSupport {

	private Certificate certificate;

	private String alias;
	private String description;
	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		certificate = certificateService.readFull(new Stub<Certificate>(certificate.getId()));

		if (isSubmit()) {
			certificate.setDescription(description);
			certificateService.update(certificate);
			return FPActionSupport.REDIRECT_SUCCESS;
		} else {
			alias = certificate.getAlias();
			description = certificate.getDescription();
		}

		return Action.INPUT;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return Action.INPUT;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
