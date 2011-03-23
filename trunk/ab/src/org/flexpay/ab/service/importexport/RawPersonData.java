package org.flexpay.ab.service.importexport;

import org.flexpay.ab.persistence.Person;
import org.flexpay.common.service.importexport.RawData;

import java.util.Collection;
import java.util.Date;

import static org.flexpay.common.util.CollectionUtils.set;

public class RawPersonData extends RawData<Person> {

	private static final Collection<String> possibleNames = set();

	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_MIDDLE_NAME = "middleName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_BIRTH_DATE = "birthDate";
	public static final String FIELD_DOCUMENT_TYPE = "documentType";
	public static final String FIELD_DOCUMENT_SERIA = "documentSeria";
	public static final String FIELD_DOCUMENT_NUMBER = "documentNumber";
	public static final String FIELD_DOCUMENT_FROM_DATE = "documentFromDate";
	public static final String FIELD_DOCUMENT_EXPIRE_DATE = "documentExpireDate";
	public static final String FIELD_DOCUMENT_ORGANIZATION = "documentOrganization";

	static {
		possibleNames.add("id");
		possibleNames.add(FIELD_FIRST_NAME);
		possibleNames.add(FIELD_MIDDLE_NAME);
		possibleNames.add(FIELD_LAST_NAME);
		possibleNames.add(FIELD_BIRTH_DATE);
		possibleNames.add(FIELD_DOCUMENT_TYPE);
		possibleNames.add(FIELD_DOCUMENT_SERIA);
		possibleNames.add(FIELD_DOCUMENT_NUMBER);
		possibleNames.add(FIELD_DOCUMENT_FROM_DATE);
		possibleNames.add(FIELD_DOCUMENT_EXPIRE_DATE);
		possibleNames.add(FIELD_DOCUMENT_ORGANIZATION);
	}

	/**
	 * Get set of valid attribute names
	 *
	 * @return Set of attribute names;
	 */
	@Override
	public Collection<String> getPossibleNames() {
		return possibleNames;
	}

	public String getFirstName() {
		return getParam(FIELD_FIRST_NAME);
	}

	public String getMiddleName() {
		return getParam(FIELD_MIDDLE_NAME);
	}

	public String getLastName() {
		return getParam(FIELD_LAST_NAME);
	}

	public Date getBirthDate() {
		return getDateParam(FIELD_BIRTH_DATE);
	}

	public String getDocumentType() {
		return getParam(FIELD_DOCUMENT_TYPE);
	}

	public String getDocumentSeria() {
		return getParam(FIELD_DOCUMENT_SERIA);
	}

	public String getDocumentNumber() {
		return getParam(FIELD_DOCUMENT_NUMBER);
	}

	public Date getDocumentFromDate() {
		return getDateParam(FIELD_DOCUMENT_FROM_DATE);
	}

	public Date getDocumentExpireDate() {
		return getDateParam(FIELD_DOCUMENT_EXPIRE_DATE);
	}

	public String getDocumentOrganization() {
		return getParam(FIELD_DOCUMENT_ORGANIZATION);
	}

}
