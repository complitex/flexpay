package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class OperationType extends DomainObject {

	public static final int SERVICE_CASH_PAYMENT = 1;
	public static final int SERVICE_CASHLESS_PAYMENT = 2;
	public static final int SERVICE_CASH_RETURN = 3;
	public static final int SERVICE_CASHLESS_RETURN = 4;
	public static final int QUITTANCE_CASH_PAYMENT = 5;
	public static final int QUITTANCE_CASHLESS_PAYMENT = 6;
	public static final int QUITTANCE_CASH_RETURN = 7;
	public static final int QUITTANCE_CASHLESS_RETURN = 8;

	private static final Set<Integer> PAYMENT_CODES = CollectionUtils.set(
			QUITTANCE_CASH_PAYMENT,
			QUITTANCE_CASHLESS_PAYMENT,
			SERVICE_CASH_PAYMENT,
			SERVICE_CASHLESS_PAYMENT
	);

	private static final Set<Integer> RETURNS_CODES = CollectionUtils.set(
			QUITTANCE_CASH_RETURN,
			QUITTANCE_CASHLESS_RETURN,
			SERVICE_CASH_RETURN,
			SERVICE_CASHLESS_RETURN
	);

	private int code;
	private Set<OperationTypeTranslation> translations = set();

	public Set<OperationTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<OperationTypeTranslation> translations) {
		this.translations = translations;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static boolean isReturnCode(int code) {
		return RETURNS_CODES.contains(code);
	}

	public static boolean isPaymentCode(int code) {
		return PAYMENT_CODES.contains(code);
	}
}
