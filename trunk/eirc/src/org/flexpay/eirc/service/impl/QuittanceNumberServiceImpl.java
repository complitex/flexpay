package org.flexpay.eirc.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.Luhn;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class QuittanceNumberServiceImpl implements QuittanceNumberService {

	private static final char PARTS_SEPARATOR = '-';

	/**
	 * Get quittance number.
	 * <p/>
	 * Quittance number is: [Acc-number]-[Month]-[Number][ControlDigit]
	 *
	 * @param q Quittance to get number for
	 * @return Quittance number
	 */
    @Override
	public String getNumber(Quittance q) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(q.getDateTill());

		StringBuilder digits = new StringBuilder()
				.append(q.getEircAccount().getAccountNumber())
				.append(new SimpleDateFormat("MMyyyy").format(q.getDateTill()))
				.append(String.format("%02d", q.getOrderNumber()));
		String controlDigit = Luhn.controlDigit(digits.toString());

		return new StringBuilder()
				.append(q.getEircAccount().getAccountNumber())
				.append(PARTS_SEPARATOR).append(new SimpleDateFormat("MM/yyyy").format(q.getDateTill()))
				.append(PARTS_SEPARATOR).append(String.format("%02d", q.getOrderNumber()))
				.append(controlDigit)
				.toString();
	}

	/**
	 * Parse generated quittance number
	 *
	 * @param quittanceNumber Generated with {@link #getNumber(org.flexpay.eirc.persistence.account.Quittance)} number
	 * @return QuittanceNumberInfo instance
	 * @throws org.flexpay.common.exception.FlexPayException if quittanceNumber has invalid format
	 */
	@NotNull
    @Override
	public QuittanceNumberInfo parseInfo(String quittanceNumber) throws FlexPayException {

		String[] parts = StringUtils.split(quittanceNumber, PARTS_SEPARATOR);
		if (parts.length != 3 || StringUtils.isBlank(parts[0])
			|| parts[1].length() != "MM/yyyy".length() || parts[1].indexOf('/') == -1
			|| parts[2].length() != "ddd".length()) {
			throw new FlexPayException("Invalid number", "eirc.error.quittance.invalid_number", quittanceNumber);
		}

		// check control digit
		String controlDigit = parts[2].substring(2);
		int slashPos = parts[1].indexOf('/');
		StringBuilder sb = new StringBuilder()
			.append(parts[0])
			.append(parts[1].substring(0, slashPos))
			.append(parts[1].substring(slashPos + 1))
			.append(parts[2].substring(0, 2));

		if (!controlDigit.equals(Luhn.controlDigit(sb.toString()))) {
			throw new FlexPayException("Invalid control digit", "eirc.error.quittance.invalid_number", quittanceNumber);
		}

		String accountNumber = parts[0];
		Date month;
		try {
			month = new SimpleDateFormat("MM/yyyy").parse(parts[1]);
		} catch (ParseException e) {
			throw new FlexPayException("Invalid number (date)", "eirc.error.quittance.invalid_number", quittanceNumber);
		}

		int number = Integer.parseInt(parts[2].substring(0, 2));
		return new QuittanceNumberInfo(accountNumber, month, number);
	}
}
