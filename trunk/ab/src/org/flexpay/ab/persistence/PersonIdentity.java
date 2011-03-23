package org.flexpay.ab.persistence;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Person Identity
 */
public class PersonIdentity extends DomainObjectWithStatus implements Comparable<PersonIdentity> {

	public static final short SEX_UNKNOWN = 0;
	public static final short SEX_MAN = 1;
	public static final short SEX_WOMAN = 2;

	private IdentityType identityType;
	private Person person;
	private Date beginDate;
	private Date endDate;
	private Date birthDate;
	private String organization;
	private String firstName;
	private String lastName;
	private String middleName;
	private String serialNumber;
	private String documentNumber;
	private short sex;
	private boolean isDefault;
	private Set<PersonIdentityAttribute> personIdentityAttributes = Collections.emptySet();

	public PersonIdentity() {
	}

	public IdentityType getIdentityType() {
		return this.identityType;
	}

	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}

	public Stub<IdentityType> getIdentityTypeStub() {
		return stub(identityType);
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {

		if (beginDate == null) {
			beginDate = ApplicationConfig.getPastInfinite();
		}

		this.beginDate = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
		if (this.beginDate.compareTo(ApplicationConfig.getFutureInfinite()) > 0) {
			this.beginDate = ApplicationConfig.getFutureInfinite();
		}
	}

	public void setBeginDateStr(String date) {
		setBeginDate(DateUtil.parseBeginDate(date));
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {

		if (endDate == null) {
			endDate = ApplicationConfig.getFutureInfinite();
		}

		this.endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
		if (this.endDate.compareTo(ApplicationConfig.getFutureInfinite()) > 0) {
			this.endDate = ApplicationConfig.getFutureInfinite();
		}
	}

	public void setEndDateStr(String date) {
		setEndDate(DateUtil.parseEndDate(date));
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Set<PersonIdentityAttribute> getPersonIdentityAttributes() {
		return this.personIdentityAttributes;
	}

	public void setPersonIdentityAttributes(Set<PersonIdentityAttribute> personIdentityAttributes) {
		this.personIdentityAttributes = personIdentityAttributes;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {

		if (birthDate == null) {
			birthDate = ApplicationConfig.getPastInfinite();
		}

		this.birthDate = DateUtils.truncate(birthDate, Calendar.DAY_OF_MONTH);
		if (this.birthDate.compareTo(ApplicationConfig.getPastInfinite()) < 0) {
			this.birthDate = ApplicationConfig.getPastInfinite();
		}
	}

	public void setBirthDateStr(String date) {
		setBirthDate(DateUtil.parseBeginDate(date));
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

	public boolean isMan() {
		return sex == SEX_MAN;
	}

	public boolean isWoman() {
		return sex == SEX_WOMAN;
	}

	/**
	 * Check if identity attributes are all blank
	 *
	 * @return <code>true</code> if identity is considered blank, or <code>false</code> otherwise
	 */
	public boolean isBlank() {
		Date futureInfinite = ApplicationConfig.getFutureInfinite();
		Date pastInfinite = ApplicationConfig.getPastInfinite();
		return isFIOEmpty() &&
			   beginDate.equals(pastInfinite) &&
			   endDate.equals(futureInfinite) &&
			   birthDate.equals(futureInfinite) &&
			   !isDefault &&
			   StringUtils.isBlank(organization) &&
			   StringUtils.isBlank(serialNumber) &&
			   StringUtils.isBlank(documentNumber);
	}


	/**
	 * Check if first, middle and last names are all blank
	 *
	 * @return <code>true</code> if names are all blank, or <code>false</code> otherwise
	 */
	public boolean isFIOEmpty() {
		return StringUtils.isBlank(firstName) &&
			   StringUtils.isBlank(middleName) &&
			   StringUtils.isBlank(lastName);
	}

	/**
	 * Copy PersonIdentity attributes
	 *
	 * @param pi Identity to copy
	 */
	public void copy(PersonIdentity pi) {
		firstName = pi.getFirstName();
		middleName = pi.getMiddleName();
		lastName = pi.getLastName();
		beginDate = pi.getBeginDate();
		endDate = pi.getEndDate();
		birthDate = pi.getBirthDate();
		isDefault = pi.isDefault();
		organization = pi.getOrganization();
		serialNumber = pi.getSerialNumber();
		documentNumber = pi.getDocumentNumber();
	}

	/**
	 * Check if this identity has the save first-middle-last names as <code>identity</code>
	 *
	 * @param identity Other identity
	 * @return <code>true</code> if first=middle-last names are equal
	 */
	public boolean isSameFIO(PersonIdentity identity) {
		return new EqualsBuilder()
				.append(firstName, identity.getFirstName())
				.append(middleName, identity.getMiddleName())
				.append(lastName, identity.getLastName())
				.append(birthDate, identity.getBirthDate())
				.append(sex, identity.getSex())
				.isEquals();
	}

	public static PersonIdentity newCopy(PersonIdentity oldIdentity) {
		PersonIdentity identity = new PersonIdentity();
		identity.setIdentityType(oldIdentity.getIdentityType());
		identity.setDefault(oldIdentity.isDefault());
		identity.setBirthDate(oldIdentity.getBirthDate());
		identity.setBeginDate(oldIdentity.getBeginDate());
		identity.setEndDate(oldIdentity.getEndDate());
		identity.setSerialNumber(oldIdentity.getSerialNumber());
		identity.setDocumentNumber(oldIdentity.getDocumentNumber());
		identity.setOrganization(oldIdentity.getFirstName());
		identity.setFirstName(oldIdentity.getFirstName());
		identity.setMiddleName(oldIdentity.getMiddleName());
		identity.setLastName(oldIdentity.getLastName());

		Person person = oldIdentity.getPerson();
		identity.setPerson(person);

		person.addIdentity(identity);
		return identity;
	}

	@Override
	public int compareTo(PersonIdentity o) {
		return beginDate.compareTo(o.beginDate);
	}

	public boolean sameNumber(@NotNull PersonIdentity o) {
		return ObjectUtils.equals(identityType, o.identityType) &&
			   StringUtils.equals(serialNumber, o.serialNumber) &&
			   StringUtils.equals(documentNumber, o.documentNumber);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("status", getStatus())
				.append("firstName", firstName)
				.append("middleName", middleName)
				.append("lastName", lastName)
				.append("beginDate", beginDate != null ? DateUtil.format(beginDate) : null)
				.append("endDate", endDate != null ? DateUtil.format(endDate) : null)
				.append("birthDate", birthDate != null ? DateUtil.format(birthDate) : null)
				.append("organization", organization)
				.append("serialNumber", serialNumber)
				.append("documentNumber", documentNumber)
				.toString();
	}

}
