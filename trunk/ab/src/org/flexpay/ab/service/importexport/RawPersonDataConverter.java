package org.flexpay.ab.service.importexport;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.DataConverter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawPersonDataConverter implements DataConverter<Person, RawPersonData> {

	private IdentityTypeService identityTypeService;

	/**
	 * Convert raw data to domain object
	 *
	 * @param rawData			   RawData
	 * @param dataSourceDescription Data source description
	 * @param correctionsService	CorrectionsService
	 * @return DomainObject
	 * @throws FlexPayException if failure occurs
	 */
	@Override
	public Person fromRawData(
			RawPersonData rawData, DataSourceDescription dataSourceDescription,
			CorrectionsService correctionsService) throws FlexPayException {

		String typeName = StringUtils.isEmpty(rawData.getDocumentType()) ?
						  IdentityType.TYPE_NAME_PASSPORT : rawData.getDocumentType();
		IdentityType type = identityTypeService.findTypeByName(typeName);
		if (type == null) {
			throw new RuntimeException("Cannot get identity type, was DB inited?");
		}

		Person person = new Person();

		PersonIdentity identity = new PersonIdentity();
		identity.setFirstName(rawData.getFirstName());
		identity.setMiddleName(rawData.getMiddleName());
		identity.setLastName(rawData.getLastName());
		identity.setBeginDate(
				DateUtil.isValid(rawData.getDocumentFromDate()) ?
				rawData.getDocumentFromDate() :
				ApplicationConfig.getPastInfinite());
		identity.setEndDate(
				DateUtil.isValid(rawData.getDocumentExpireDate()) ?
				rawData.getDocumentExpireDate() :
				ApplicationConfig.getFutureInfinite());
		identity.setSerialNumber(rawData.getDocumentSeria());
		identity.setDocumentNumber(rawData.getDocumentNumber());
		identity.setOrganization(rawData.getDocumentOrganization());
		identity.setDefault(true);
		identity.setBirthDate(
				DateUtil.isValid(rawData.getBirthDate()) ?
				rawData.getBirthDate() :
				ApplicationConfig.getPastInfinite());
		identity.setPerson(person);
		identity.setIdentityType(type);

		person.setPersonIdentities(set(identity));

		return person;
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

}
