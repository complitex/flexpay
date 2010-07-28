package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

public class Document extends DomainObject {

	private BigDecimal sum;

	private Operation operation;

	private Organization creditorOrganization;
	private String creditorId;
	private Service service;

	private Organization debtorOrganization;
	private String debtorId;

	private RegistryRecord registryRecord;

	private Document referenceDocument;
	private Set<Document> referencedDocuments = Collections.emptySet();
	private DocumentType documentType;
	private DocumentStatus documentStatus;

	private String address;
	private String payerFIO;

	// creditor internal data
	private String firstName;
	private String middleName;
	private String lastName;
	private String country;
	private String region;
    private String townType;
	private String townName;
    private String streetType;
	private String streetName;
	private String buildingNumber;
	private String buildingBulk;
	private String apartmentNumber;

	private BigDecimal debt;

	private Set<DocumentAddition> additions = Collections.emptySet();

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(@NotNull Operation operation) {
		this.operation = operation;
	}

	public RegistryRecord getRegistryRecord() {
		return registryRecord;
	}

	public void setRegistryRecord(RegistryRecord registryRecord) {
		this.registryRecord = registryRecord;
	}

	public Document getReferenceDocument() {
		return referenceDocument;
	}

	public void setReferenceDocument(Document referenceDocument) {
		this.referenceDocument = referenceDocument;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(@NotNull DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(@NotNull DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Organization getCreditorOrganization() {
		return creditorOrganization;
	}

	public void setCreditorOrganization(Organization creditorOrganization) {
		this.creditorOrganization = creditorOrganization;
	}

	public String getCreditorId() {
		return creditorId;
	}

	public void setCreditorId(String creditorId) {
		this.creditorId = creditorId;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Stub<Service> getServiceStub() {
		return stub(service);
	}

	public Organization getDebtorOrganization() {
		return debtorOrganization;
	}

	public void setDebtorOrganization(Organization debtorOrganization) {
		this.debtorOrganization = debtorOrganization;
	}

	public String getDebtorId() {
		return debtorId;
	}

	public void setDebtorId(String debtorId) {
		this.debtorId = debtorId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPayerFIO() {
		return payerFIO;
	}

	public void setPayerFIO(String payerFIO) {
		this.payerFIO = payerFIO;
	}

	public DocumentAddition getAddition(int typeCode) {

		for (DocumentAddition addition : additions) {
			if (addition.getAdditionType().getCode() == typeCode) {
				return addition;
			}
		}

		return null;
	}

	public Set<DocumentAddition> getAdditions() {
		return additions;
	}

	public void setAdditions(Set<DocumentAddition> additions) {
		this.additions = additions;
	}

	public void addAddition(DocumentAddition addition) {

		DocumentAddition toDelete = null;
		for (DocumentAddition old : additions) {
			if (old.getAdditionType().equals(addition.getAdditionType())) {
				toDelete = old;
				break;
			}
		}
		additions.remove(toDelete);

		addition.setDocument(this);
		additions.add(addition);
	}

	public Set<Document> getReferencedDocuments() {
		return referencedDocuments;
	}

	public void setReferencedDocuments(Set<Document> referencedDocuments) {
		this.referencedDocuments = referencedDocuments;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCreditorFio() {
		return lastName + " " + firstName + " " + middleName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownType() {
        return townType;
    }

    public void setTownType(String townType) {
        this.townType = townType;
    }

    public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getBuildingBulk() {
		return buildingBulk;
	}

	public void setBuildingBulk(String buildingBulk) {
		this.buildingBulk = buildingBulk;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public BigDecimal getDebt() {
		return debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("sum", sum).
				append("registryRecord", registryRecord).
				append("documentType", documentType).
				append("documentStatus", documentStatus).
				append("firstName", firstName).
				append("middleName", middleName).
				append("lastName", lastName).
				append("country", country).
				append("region", region).
                append("townType", townType).
				append("townName", townName).
                append("streetType", streetType).
				append("streetName", streetName).
				append("buildingNumber", buildingNumber).
				append("buildingBulk", buildingBulk).
				append("apartmentNumber", apartmentNumber).
				toString();
	}
}
