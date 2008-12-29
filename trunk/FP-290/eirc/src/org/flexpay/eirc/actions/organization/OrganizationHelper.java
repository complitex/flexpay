package org.flexpay.eirc.actions.organization;

import org.flexpay.eirc.persistence.Organization;
import org.flexpay.eirc.persistence.OrganizationInstance;
import org.flexpay.eirc.service.OrganizationService;
import static org.flexpay.common.persistence.Stub.stub;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

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
