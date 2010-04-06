package org.flexpay.admin.action.certificate;

import com.opensymphony.xwork2.Action;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.CertificateService;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class CertificateDeleteAction extends FPActionSupport {

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
		
		return Action.SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return Action.SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
}
