package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;
import java.util.HashMap;

public class SpRegistryType extends DomainObject {

	private static Map<Integer, String> typeToName = new HashMap<Integer, String>();

	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_SALDO = 1;
	public static final int TYPE_INCOME = 2;
	public static final int TYPE_MESSAGE = 3;
	public static final int TYPE_CLOSED_ACCOUNTS = 4;
	public static final int TYPE_INFO = 5;
	public static final int TYPE_CORRECTIONS = 6;
	public static final int TYPE_CASH_PAYMENTS = 7;
	public static final int TYPE_CASHLESS_PAYMENTS = 8;
	public static final int TYPE_REPAYMENT = 9;
	public static final int TYPE_ERRORS = 10;

	static {
		typeToName.put(TYPE_UNKNOWN, "eirc.registry_type.unknown");
		typeToName.put(TYPE_SALDO, "eirc.registry_type.saldo");
		typeToName.put(TYPE_INCOME, "eirc.registry_type.cash_payment");
		typeToName.put(TYPE_MESSAGE, "eirc.registry_type.message");
		typeToName.put(TYPE_CLOSED_ACCOUNTS, "eirc.registry_type.closed_accounts");
		typeToName.put(TYPE_INFO, "eirc.registry_type.info");
		typeToName.put(TYPE_CORRECTIONS, "eirc.registry_type.corrections");
		typeToName.put(TYPE_CASH_PAYMENTS, "eirc.registry_type.cash_payment");
		typeToName.put(TYPE_CASHLESS_PAYMENTS, "eirc.registry_type.cashless_payment");
		typeToName.put(TYPE_REPAYMENT, "eirc.registry_type.repayment");
		typeToName.put(TYPE_ERRORS, "eirc.registry_type.errors");
	}

	private int code;

	/**
	 * Constructs a new DomainObject.
	 */
	public SpRegistryType() {
	}

	public SpRegistryType(Long id) {
		super(id);
	}
	

	/**
	 * Getter for property 'code'.
	 *
	 * @return Value for property 'code'.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Setter for property 'typeId'.
	 *
	 * @param typeId Value to set for property 'typeId'.
	 */
	public void setCode(int typeId) {
		this.code = typeId;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("code: ", code)
				.toString();
	}

	public String getI18nName() {
		return typeToName.get(code);
	}
}
