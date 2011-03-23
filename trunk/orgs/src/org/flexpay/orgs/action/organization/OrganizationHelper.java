package org.flexpay.orgs.action.organization;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;

public class OrganizationHelper {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private OrganizationService organizationService;

	public String getName(@NotNull OrganizationInstance<?, ?> instance, @NotNull Locale locale) {

		Organization org = organizationService.readFull(instance.getOrganizationStub());
		if (org == null) {
			log.info("No organisation found for instance {}", instance);
			return null;
		}

		return org.getName(locale);
	}

	public String getName(@NotNull Organization organizationStub, @NotNull Locale locale) {

		Organization org = organizationService.readFull(stub(organizationStub));
		if (org == null) {
			log.info("No organisation for stub {}", organizationStub);
			return null;
		}

		return org.getName(locale);
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

}
