package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PersonReferencesHistoryGenerator implements ReferencesHistoryGenerator<Person> {

	private IdentityTypeHistoryGenerator identityTypeHistoryGenerator;
	private ApartmentHistoryGenerator apartmentHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Person obj) {

		for (PersonIdentity identity : obj.getPersonIdentities()) {
			identityTypeHistoryGenerator.generateFor(identity.getIdentityType());
		}

		for (PersonRegistration registration : obj.getPersonRegistrations()) {
			apartmentHistoryGenerator.generateFor(registration.getApartment());
		}
	}

	@Required
	public void setIdentityTypeHistoryGenerator(IdentityTypeHistoryGenerator identityTypeHistoryGenerator) {
		this.identityTypeHistoryGenerator = identityTypeHistoryGenerator;
	}

	@Required
	public void setApartmentHistoryGenerator(ApartmentHistoryGenerator apartmentHistoryGenerator) {
		this.apartmentHistoryGenerator = apartmentHistoryGenerator;
	}
}
