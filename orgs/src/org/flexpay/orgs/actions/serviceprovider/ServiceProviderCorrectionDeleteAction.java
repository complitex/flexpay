package org.flexpay.orgs.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.importexport.impl.ClassToTypeRegistryOrgs;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class ServiceProviderCorrectionDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

    private CorrectionsService correctionsService;
    private ClassToTypeRegistryOrgs classToTypeRegistryOrgs;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        correctionsService.delete(objectIds, classToTypeRegistryOrgs.getType(ServiceProvider.class));

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    @Required
    public void setClassToTypeRegistryOrgs(ClassToTypeRegistryOrgs classToTypeRegistryOrgs) {
        this.classToTypeRegistryOrgs = classToTypeRegistryOrgs;
    }

}
