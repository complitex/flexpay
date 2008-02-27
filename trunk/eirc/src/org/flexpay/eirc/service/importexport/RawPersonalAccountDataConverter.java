package org.flexpay.eirc.service.importexport;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.persistence.PersonalAccount;
import org.flexpay.eirc.persistence.AccountStatus;
import org.flexpay.eirc.service.AccountStatusService;

import java.util.HashSet;
import java.util.Set;

public class RawPersonalAccountDataConverter implements DataConverter<PersonalAccount, RawPersonalAccountData> {

	private IdentityTypeService identityTypeService;
	private AccountStatusService accountStatusService;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	public PersonalAccount fromRawData(
			RawPersonalAccountData rawData, DataSourceDescription dataSourceDescription,
			CorrectionsService correctionsService) throws FlexPayException {

		PersonalAccount account = new PersonalAccount();
		account.setCreationDate(DateIntervalUtil.now());

		Apartment apartment = (Apartment) correctionsService.findCorrection(
				rawData.getApartmentId(), Apartment.class, dataSourceDescription);
		if (apartment == null) {
			throw new FlexPayException("Cannot find apartment correction");
		}
		account.setApartment(apartment);

		IdentityType type = identityTypeService.getType(IdentityType.TYPE_PASSPORT);
		if (type == null) {
			throw new RuntimeException("Cannot get identity type: passport");
		}

		Person responsiblePerson = new Person();
		account.setResponsiblePerson(responsiblePerson);

		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName(rawData.getFirstName());
		identity.setMiddleName(rawData.getMiddleName());
		identity.setLastName(rawData.getLastName());
		identity.setBeginDate(ApplicationConfig.getInstance().getPastInfinite());
		identity.setEndDate(ApplicationConfig.getInstance().getFutureInfinite());
		identity.setSerialNumber("");
		identity.setDocumentNumber("");
		identity.setOrganization("");
		identity.setDefault(true);
		identity.setBirthDate(ApplicationConfig.getInstance().getPastInfinite());
		identity.setPerson(responsiblePerson);
		identity.setIdentityType(type);

		Set<PersonIdentity> identities = new HashSet<PersonIdentity>();
		identities.add(identity);
		responsiblePerson.setPersonIdentities(identities);

		AccountStatus accountStatus = accountStatusService.getStatus(AccountStatus.STATUS_ACTIVE);
		if (accountStatus == null) {
			throw new RuntimeException("Cannot find active status, was DB initialized?");
		}
		account.setStatus(accountStatus);

		return account;
	}

	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

	public void setAccountStatusService(AccountStatusService accountStatusService) {
		this.accountStatusService = accountStatusService;
	}
}
