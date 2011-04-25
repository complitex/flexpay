package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class RegistryType extends DomainObject {

	private static final Map<Integer, String> typeToName = new HashMap<Integer, String>();

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
	public static final int TYPE_QUITTANCE = 11;
	public static final int TYPE_BANK_PAYMENTS = 12;

	static {
		typeToName.put(TYPE_UNKNOWN, "common.registry_type.unknown");
		typeToName.put(TYPE_SALDO, "common.registry_type.saldo");
		typeToName.put(TYPE_INCOME, "common.registry_type.income");
		typeToName.put(TYPE_MESSAGE, "common.registry_type.message");
		typeToName.put(TYPE_CLOSED_ACCOUNTS, "common.registry_type.closed_accounts");
		typeToName.put(TYPE_INFO, "common.registry_type.info");
		typeToName.put(TYPE_CORRECTIONS, "common.registry_type.corrections");
		typeToName.put(TYPE_CASH_PAYMENTS, "common.registry_type.cash_payment");
		typeToName.put(TYPE_CASHLESS_PAYMENTS, "common.registry_type.cashless_payment");
		typeToName.put(TYPE_REPAYMENT, "common.registry_type.repayment");
		typeToName.put(TYPE_ERRORS, "common.registry_type.errors");
		typeToName.put(TYPE_QUITTANCE, "common.registry_type.quittance");
		typeToName.put(TYPE_BANK_PAYMENTS, "common.registry_type.bank_payments");
	}

	private int code;

	public RegistryType() {
	}

	public RegistryType(Long id) {
		super(id);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int typeId) {
		this.code = typeId;
	}

	public String getI18nName() {
		return typeToName.get(code);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("code", code).
				append("i18nName", getI18nName()).
				toString();
	}

	public boolean isPayments() {
		return code == TYPE_CASH_PAYMENTS || code == TYPE_CASHLESS_PAYMENTS;
	}

	public boolean isCashPayments() {
		return code == TYPE_CASH_PAYMENTS;
	}

	public boolean isCashlessPayments() {
		return code == TYPE_CASHLESS_PAYMENTS;
	}
}
