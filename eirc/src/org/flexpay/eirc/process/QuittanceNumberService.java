package org.flexpay.eirc.process;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface QuittanceNumberService {

	/**
	 * Get quittance number.
	 * <p/>
	 * Quittance number is: [Acc-number]-[Month]-[Number][ControlDigit]
	 *
	 * @param q Quittance to get number for
	 * @return Quittance number
	 */
	String getNumber(Quittance q);

	/**
	 * Parse generated quittance number
	 *
	 * @param quittanceNumber Generated with {@link #getNumber(Quittance)} number
	 * @return QuittanceNumberInfo instance
	 * @throws FlexPayException if quittanceNumber has invalid format
	 */
	@NotNull
	QuittanceNumberInfo parseInfo(String quittanceNumber) throws FlexPayException;

	/**
	 * Quittance number info used to parse back quittance number and lookup Quittance
	 */
	public static class QuittanceNumberInfo {

		private String accountNumber;
		private Date month;
		private int number;

		public QuittanceNumberInfo(String accountNumber, Date month, int number) {
			this.accountNumber = accountNumber;
			this.month = month;
			this.number = number;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public Date getMonth() {
			return month;
		}

		public int getNumber() {
			return number;
		}
	}

}
