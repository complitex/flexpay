package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public class QuittanceDetailsQuittance extends DomainObject {

	private QuittanceDetails quittanceDetails;
	private Quittance quittance;

	/**
	 * Constructs a new DomainObject.
	 */
	public QuittanceDetailsQuittance() {
	}

	public QuittanceDetailsQuittance(@NotNull Long id) {
		super(id);
	}

	public QuittanceDetailsQuittance(@NotNull Stub<QuittanceDetailsQuittance> stub) {
		super(stub.getId());
	}

	/**
	 * @return the quittanceDetails
	 */
	public QuittanceDetails getQuittanceDetails() {
		return quittanceDetails;
	}

	/**
	 * @param quittanceDetails the quittanceDetails to set
	 */
	public void setQuittanceDetails(QuittanceDetails quittanceDetails) {
		this.quittanceDetails = quittanceDetails;
	}

	/**
	 * @return the quittance
	 */
	public Quittance getQuittance() {
		return quittance;
	}

	/**
	 * @param quittance the quittance to set
	 */
	public void setQuittance(Quittance quittance) {
		this.quittance = quittance;
	}
}
