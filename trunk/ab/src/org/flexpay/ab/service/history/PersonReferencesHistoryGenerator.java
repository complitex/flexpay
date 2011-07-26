package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.common.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class PersonReferencesHistoryGenerator implements ReferencesHistoryGenerator<Person> {

	private IdentityTypeHistoryGenerator identityTypeHistoryGenerator;
	private ApartmentHistoryGenerator apartmentHistoryGenerator;
	private DiffService diffService;

	@Override
	public void generateReferencesHistory(@NotNull Person obj) {

		if (!diffService.allObjectsHaveDiff(IdentityType.class)) {
			for (PersonIdentity identity : obj.getPersonIdentities()) {
				identityTypeHistoryGenerator.generateFor(identity.getIdentityType());
			}
		}

		for (PersonRegistration registration : obj.getPersonRegistrations()) {
			if (!diffService.hasDiffs(registration.getApartment())) {
				apartmentHistoryGenerator.generateFor(registration.getApartment());
			}
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        identityTypeHistoryGenerator.setJpaTemplate(jpaTemplate);
        apartmentHistoryGenerator.setJpaTemplate(jpaTemplate);
        diffService.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setIdentityTypeHistoryGenerator(IdentityTypeHistoryGenerator identityTypeHistoryGenerator) {
		this.identityTypeHistoryGenerator = identityTypeHistoryGenerator;
	}

	@Required
	public void setApartmentHistoryGenerator(ApartmentHistoryGenerator apartmentHistoryGenerator) {
		this.apartmentHistoryGenerator = apartmentHistoryGenerator;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

}
