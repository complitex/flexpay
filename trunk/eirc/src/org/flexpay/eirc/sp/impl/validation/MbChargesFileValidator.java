package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.sp.impl.MbFileValidator;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MbChargesFileValidator extends MbFileValidator {

	private final String OPERATION_DATE_FORMAT = "MMyy";
	private final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

	protected boolean validateFile(@NotNull FPFile spFile) throws FlexPayException {

		FileValues fileValues = new FileValues();

		BufferedReader reader = null;
		boolean ret = true;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(spFile.getInputStream(), REGISTRY_FILE_ENCODING));
			ValidationContext context = new ValidationContext();

			for (int lineNum = 0; ; lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					throw new FlexPayException("Can't read file line");
				}
				if (lineNum == 0) {
					if (!FIRST_FILE_STRING.equals(line)) {
						throw new FlexPayException("First line must be equals 300 spaces");
					}
				} else if (lineNum == 1) {
					try {
						validateHeader(line, context);
					} catch (Exception e) {
						log.warn("Incorrect header in file. Line number = {}, error: {}\n{}\nLine = {}",
								new Object[] {lineNum, e.getMessage(), e.getStackTrace(), line});
						ret = false;
					}
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					fileValues.setLines(lineNum - 2);
					try {
						validateFooter(line, fileValues);
					} catch (Exception e) {
						log.warn("Incorrect footer in file. Line number = {}, error: {}\nLine = {}",
								new Object[] {lineNum, e.getMessage(), line});
						ret = false;
					}
					log.debug("Validated {} records in file", lineNum - 2);
					break;
				} else {
					try {
						validateRecord(line, fileValues, context);
					} catch (Exception e) {
						log.warn("Incorrect record in file. Line number = {}, error: {}\n{}\nLine = {}",
								new Object[] {lineNum, e.getMessage(), e.getStackTrace(), line});
						ret = false;
					}
				}
			}
		} catch (IOException e) {
			throw new FlexPayException("Error with reading file", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return ret;

	}

	private void validateHeader(String line, ValidationContext context) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Not 4 fields");
		}
		validateFields(fields);
		if (fields[0].length() > 20) {
			throw new FlexPayException("Organization name length can't be more 20 symbols");
		}
		// check if provider correction exists
		Stub<ServiceProvider> providerStub = correctionsService.findCorrection(
				fields[1], ServiceProvider.class, megabankSD);
		if (providerStub == null) {
			throw new FlexPayException("No service provider correction with id " + fields[1]);
		}
		context.setServiceProviderId(providerStub.getId());

		try {
			new SimpleDateFormat(INCOME_PERIOD_DATE_FORMAT).parse(fields[2]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse income period " + fields[2]);
		}
		try {
			new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse file creation date " + fields[3]);
		}
	}

	private void validateRecord(String line, FileValues fileValues, ValidationContext context) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 6) {
			throw new FlexPayException("Expected 6 fields");
		}
		validateFields(fields);
		try {
			fileValues.addIncome(Long.parseLong(fields[1]));
		} catch (Exception e) {
			throw new FlexPayException("Can't parse charges summ " + fields[1]);
		}
		try {
			fileValues.addSaldo(Long.parseLong(fields[2]));
		} catch (Exception e) {
			throw new FlexPayException("Can't parse balance summ " + fields[2]);
		}

		Date operationDate;
		try {
			operationDate = new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(fields[5]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse operation date " + fields[5]);
		}

		String serviceTypeCode = fields[3];
		if (serviceTypesMapper.getInternalType(serviceTypeCode) == null) {
			throw new FlexPayException("Cannot map service type code " + serviceTypeCode);
		}
		// ensure services can be found
		findInternalServices(context.getServiceProviderStub(), serviceTypeCode, operationDate);
	}

	private List<Service> findInternalServices(Stub<ServiceProvider> providerStub, String mbCode, Date date)
			throws FlexPayException {

		Stub<ServiceType> typeStub = serviceTypesMapper.getInternalType(mbCode);
		List<Service> services = spService.findServices(providerStub, typeStub, date);
		if (services.isEmpty()) {
			throw new FlexPayException("No service found by internal type " + typeStub + ", mb code=" + mbCode);
		}

		return services;
	}
	

	private void validateFooter(String line, FileValues fileValues) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Not 4 fields");
		}
		validateFields(fields);
		if (!fields[0].equals(LAST_FILE_STRING_BEGIN)) {
			throw new FlexPayException("First field must be equals " + LAST_FILE_STRING_BEGIN);
		}
		try {
			if (fileValues.getIncomeSumm() != Long.parseLong(fields[1])) {
				throw new FlexPayException("Invalid data in file (total income summ in footer not equals with summ of incomes in all lines - " + fields[1] + ", but were " + fileValues.getIncomeSumm() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Can't parse total income summ " + fields[1]);
		}
		try {
			if (fileValues.getSaldoSumm() != Long.parseLong(fields[2])) {
				throw new FlexPayException("Invalid data in file (total saldo summ in footer not equals with summ of saldos in all lines - " + fields[2] + ", but were " + fileValues.getSaldoSumm() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Can't parse total saldo summ " + fields[2]);
		}
		try {
			if (fileValues.getLines() != Integer.parseInt(fields[3])) {
				throw new FlexPayException("Invalid data in file (incorrect records number in file - " + fields[3] + ", but were " + fileValues.getLines() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Can't parse total amount of lines in file - " + fields[3]);
		}
	}

	private class FileValues {

		private long incomeSumm = 0;
		private long saldoSumm = 0;
		private int lines = 0;

		public long getIncomeSumm() {
			return incomeSumm;
		}

		public long getSaldoSumm() {
			return saldoSumm;
		}

		public int getLines() {
			return lines;
		}

		public void setLines(int lines) {
			this.lines = lines;
		}

		public void addSaldo(long saldo) {
			this.saldoSumm += saldo;
		}

		public void addIncome(long income) {
			this.incomeSumm += income;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
					append("incomeSumm", incomeSumm).
					append("saldoSumm", saldoSumm).
					append("lines", lines).toString();
		}

	}

}
