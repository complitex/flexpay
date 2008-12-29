package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

/**
 * Full and Partially payed quittance statuses
 */
public class QuittancePaymentStatus extends DomainObject {

	public static final int PAYED_FULL = 1;
	public static final int PAYED_PARTIALLY = 2;

	private String i18nName;
	private int code;

	/**
	 * Constructs a new DomainObject.
	 */
	public QuittancePaymentStatus() {
	}

	public QuittancePaymentStatus(@NotNull Long id) {
		super(id);
	}

	public QuittancePaymentStatus(@NotNull Stub<QuittancePaymentStatus> stub) {
		super(stub.getId());
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		this.i18nName = i18nName;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
