package org.flexpay.admin.action.certificate;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class CertificateDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private CertificateService certificateService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

        if (objectIds == null) {
            log.warn("Parameter objectIds is null");
            return SUCCESS;
        }

        for (Long id : objectIds) {
            certificateService.delete(new Certificate(id));
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
