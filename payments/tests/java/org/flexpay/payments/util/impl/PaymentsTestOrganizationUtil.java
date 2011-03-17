package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.OrganizationDescription;
import org.flexpay.orgs.persistence.OrganizationName;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.util.TestOrganizationUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PaymentsTestOrganizationUtil implements TestOrganizationUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("languageService")
	private LanguageService languageService;

    @Autowired
    @Qualifier ("organizationService")
	private OrganizationService organizationService;

    @NotNull
    @Override
    public Organization create(@NotNull String individualTaxNumber) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        Organization organization = new Organization();
		organization.setStatus(ObjectWithStatus.STATUS_ACTIVE);
		organization.setIndividualTaxNumber("112");
		organization.setKpp("224");
		organization.setJuridicalAddress("Kharkov");
		organization.setPostalAddress("Kharkov");

		OrganizationName organizationName = new OrganizationName();
		organizationName.setLang(lang);
		organizationName.setName("test register organization name");
		organization.setName(organizationName);

		OrganizationDescription organizationDescription = new OrganizationDescription();
		organizationDescription.setLang(lang);
		organizationDescription.setName("test register organization description");
		organization.setDescription(organizationDescription);

        try {
            return organizationService.create(organization);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create organization", flexPayExceptionContainer);
        }
        return null;
    }

    @Override
    public void delete(@NotNull Organization organization) {
        organizationService.delete(Stub.stub(organization));
    }
}
