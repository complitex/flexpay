package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.eirc.sp.impl.MbFileValidator;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MbRegistryFileValidator extends MbFileValidator {

	public static final DateFormat OPERATION_DATE_FORMAT = new SimpleDateFormat("MMyy");
	public static final DateFormat INCOME_PERIOD_DATE_FORMAT = new SimpleDateFormat("MMyy");

	protected boolean validateFile(@NotNull FPFile spFile) throws FlexPayException {

		FileValues fileValues = new FileValues();

		BufferedReader reader = null;
		boolean ret = true;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FPFileUtil.getFileOnServer(spFile)), REGISTRY_FILE_ENCODING), 500);

			for (int lineNum = 0;;lineNum++) {
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
						validateHeader(line);
					} catch (Exception e) {
						log.debug("Incorrect header in file. Line number = {}, error: {}\nLine = {}", new Object[] {lineNum, e.getMessage(), line});
						ret = false;
//						throw new FlexPayException("Incorrect header in file. Line number = " + lineNum, e);
					}
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					fileValues.setLines(lineNum - 2);
					try {
						validateFooter(line, fileValues);
					} catch (Exception e) {
						log.debug("Incorrect footer in file. Line number = {}, error: {}\nLine = {}", new Object[] {lineNum, e.getMessage(), line});
						ret = false;
//						throw new FlexPayException("Incorrect footer in file. Line number = " + lineNum, e);
					}
					log.debug("Validated {} records in file", lineNum - 2);
					break;
				} else {
					try {
						validateRecord(line, fileValues);
					} catch (Exception e) {
						log.debug("Incorrect record in file. Line number = {}, error: {}\nLine = {}", new Object[] {lineNum, e.getMessage(), line});
						ret = false;
//						throw new FlexPayException("Incorrect record in file. Line number = " + lineNum + ". Line = " + line, e);
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

	private void validateHeader(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Not 4 fields");
		}
		validateFields(fields);
		if (fields[0].length() > 20) {
			throw new FlexPayException("Organization name length can't be more 20 symbols");
		}
		try {
			Long.parseLong(fields[1]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse organization code " + fields[1]);
		}
		try {
			INCOME_PERIOD_DATE_FORMAT.parse(fields[2]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse income period " + fields[2]);
		}
		try {
			new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse file creation date " + fields[3]);
		}
	}

	private void validateRecord(String line, FileValues fileValues) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 6) {
			throw new FlexPayException("Not 6 fields");
		}
		validateFields(fields);
		try {
			fileValues.addIncome(Long.parseLong(fields[1]));
		} catch (Exception e) {
			throw new FlexPayException("Can't parse summ " + fields[1]);
		}
		try {
			fileValues.addSaldo(Long.parseLong(fields[2]));
		} catch (Exception e) {
			throw new FlexPayException("Can't parse saldo summ " + fields[2]);
		}
		try {
			Integer.parseInt(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse service code " + fields[3]);
		}
		try {
			OPERATION_DATE_FORMAT.parse(fields[5]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse operation date " + fields[5]);
		}
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

		public void setIncomeSumm(long incomeSumm) {
			this.incomeSumm = incomeSumm;
		}

		public long getSaldoSumm() {
			return saldoSumm;
		}

		public void setSaldoSumm(long saldoSumm) {
			this.saldoSumm = saldoSumm;
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
