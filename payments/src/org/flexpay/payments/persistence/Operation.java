package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Financial operation
 */
public class Operation extends DomainObject {

    private String address;
    private String payerFIO;
    private String cashierFio;

	private BigDecimal operationSum;
	private BigDecimal operationInputSum;
	private BigDecimal change;
    private Boolean canReturn = true;

	private Date creationDate;
	private String creatorUserName;
	private Organization creatorOrganization;
	private PaymentPoint paymentPoint;

	private Date registerDate;
	private String registerUserName;
	private Organization registerOrganization;

	private RegistryRecord registryRecord;

	private OperationType operationType;
	private OperationLevel operationLevel;
	private OperationStatus operationStatus;

	private Set<Document> documents = set();

	private Operation referenceOperation;
	private Set<Operation> referencedOperations = set();

    private Cashbox cashbox;

	private Set<OperationAddition> additions = set();

	/**
	 * Constructs a new DomainObject.
	 */
	public Operation() {
	}

	public Operation(@NotNull Long id) {
		super(id);
	}

	public Operation(@NotNull Stub<Operation> stub) {
		super(stub.getId());
	}

	public BigDecimal getOperationSum() {
		return operationSum;
	}

	public void setOperationSum(BigDecimal operationSum) {
		this.operationSum = operationSum;
	}

	public BigDecimal getOperationInputSum() {
		return operationInputSum;
	}

	public void setOperationInputSum(BigDecimal operationInputSum) {
		this.operationInputSum = operationInputSum;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

    public Boolean getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(Boolean canReturn) {
        this.canReturn = canReturn;
    }

    public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterUserName() {
		return registerUserName;
	}

	public void setRegisterUserName(String registerUserName) {
		this.registerUserName = registerUserName;
	}

	public Organization getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(Organization creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public Stub<Organization> getCreatorOrganizationStub() {
		return stub(creatorOrganization);
	}

	public Organization getRegisterOrganization() {
		return registerOrganization;
	}

	public void setRegisterOrganization(Organization registerOrganization) {
		this.registerOrganization = registerOrganization;
	}

	public RegistryRecord getRegistryRecord() {
		return registryRecord;
	}

	public void setRegistryRecord(RegistryRecord registryRecord) {
		this.registryRecord = registryRecord;
	}

	public OperationLevel getOperationLevel() {
		return operationLevel;
	}

	public void setOperationLevel(OperationLevel operationLevel) {
		this.operationLevel = operationLevel;
	}

	public OperationStatus getOperationStatus() {
		return operationStatus;
	}	

	public void setOperationStatus(OperationStatus operationStatus) {
		this.operationStatus = operationStatus;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public void addDocument(Document doc) {

		doc.setOperation(this);
		documents.add(doc);
	}

	public Operation getReferenceOperation() {
		return referenceOperation;
	}

	public void setReferenceOperation(Operation referenceOperation) {
		this.referenceOperation = referenceOperation;
	}

	public Set<Operation> getReferencedOperations() {
		return referencedOperations;
	}

	public void setReferencedOperations(Set<Operation> referencedOperations) {
		this.referencedOperations = referencedOperations;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(@NotNull OperationType operationType) {
		this.operationType = operationType;
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

	public String getCashierFio() {
		return cashierFio;
	}

	public void setCashierFio(String cashierFio) {
		this.cashierFio = cashierFio;
	}

	public Set<OperationAddition> getAdditions() {
		return additions;
	}

	public void setAdditions(Set<OperationAddition> additions) {
		this.additions = additions;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

	public Stub<PaymentPoint> getPaymentPointStub() {
		return stub(paymentPoint);
	}

    public Cashbox getCashbox() {
        return cashbox;
    }

    public void setCashbox(Cashbox cashbox) {
        this.cashbox = cashbox;
    }

	public Stub<Cashbox> getCashboxStub() {
		return stub(cashbox);
	}

    public BigDecimal documentsSum() {

        BigDecimal sum = new BigDecimal("0.00");
        for (Document document : documents) {
            sum = sum.add(document.getSum());
        }

        return sum;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("operationInputSum", operationInputSum).
				append("operationSum", operationSum).
				append("change", change).
                append("canReturn", canReturn).
				append("creationDate", creationDate).
				append("creatorUserName", creatorUserName).
				append("registerDate", registerDate).
				append("registerUserName", registerUserName).
				append("operationLevel", operationLevel).
				append("operationStatus", operationStatus).
				toString();
	}

}
