package org.flexpay.payments.actions.certificate;

import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.Certificate;
import org.flexpay.payments.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class CertificateDeleteAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = set();

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (!objectIds.isEmpty()) {
			for (Long id : objectIds) {
				Certificate certificate = certificateService.readFull(new Stub<Certificate>(id));
				if (certificate != null) {
					certificateService.delete(certificate);
				}
			}
		}
		
		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
