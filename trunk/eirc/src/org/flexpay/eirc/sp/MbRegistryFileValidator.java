package org.flexpay.eirc.sp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MbRegistryFileValidator implements Validator {

	public final static String LAST_FILE_STRING_BEGIN = "999999999";
	public final static String REGISTRY_FILE_ENCODING = "Cp866";
	public final static DateFormat OPERATION_DATE_FORMAT = new SimpleDateFormat("MMyy");
	public final static String FIRST_FILE_STRING =
			"                                                                                                    "
			+ "                                                                                                    "
			+ "                                                                                                    ";

	public void validate(FPFile spFile) throws FlexPayException {

		File file = spFile.getFile();
		if (file == null) {
			throw new FlexPayException("For FPFile (id = " + spFile.getId() + ") not found temp file: " + spFile.getNameOnServer());
		}

		FileValues fileValues = new FileValues();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), REGISTRY_FILE_ENCODING), 500);

			for (int lineNum = 0;;lineNum++) {
				String l = reader.readLine();
				if (l == null) {
					throw new FlexPayException("Can't read file line");
				}
				String line = new String(l.getBytes("UTF-8"));
				if (lineNum == 0) {
					if (!FIRST_FILE_STRING.equals(line)) {
						throw new FlexPayException("First line must be equals 300 spaces");
					}
				} else if (lineNum == 1) {
					validateHeader(line);
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					fileValues.setLines(lineNum - 2);
					validateFooter(line, fileValues);
					break;
				} else {
					validateRecord(line, fileValues);
				}
			}
		} catch (IOException e) {
			throw new FlexPayException("Error with reading file", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// do nothing
			}
		}

	}

	private void validateHeader(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Incorrect header line (not 4 fields)");
		}
		try {
			Long.parseLong(fields[1]);
			Long.parseLong(fields[2]);
			Long.parseLong(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse numeric field in header", e);
		}
	}

	private void validateRecord(String line, FileValues fileValues) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 6) {
			throw new FlexPayException("Incorrect record in file (not 6 fields)");
		}
		try {
			fileValues.addIncome(Long.parseLong(fields[1]));
		} catch (Exception e) {
			throw new FlexPayException("Incorrect record in file (can't parse summ " + fields[1] + ")");
		}
		try {
			fileValues.addSaldo(Long.parseLong(fields[2]));
		} catch (Exception e) {
			throw new FlexPayException("Incorrect record in file (can't parse saldo summ " + fields[2] + ")");
		}
		try {
			OPERATION_DATE_FORMAT.parse(fields[5]);
		} catch (Exception e) {
			throw new FlexPayException("Incorrect record in file (can't parse operation date " + fields[5] + ")");
		}
	}

	private void validateFooter(String line, FileValues fileValues) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 4) {
			throw new FlexPayException("Incorrect footer line (not 4 fields)");
		}
		if (!fields[0].equals(LAST_FILE_STRING_BEGIN)) {
			throw new FlexPayException("Incorrect footer line (first field must be equals 999999999)");
		}
		try {
			if (fileValues.getIncomeSumm() != Long.parseLong(fields[1])) {
				throw new FlexPayException("Invalid data in file (total income summ in footer not equals with summ of incomes in all lines - " + fields[1] + ", but were " + fileValues.getIncomeSumm() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total income summ " + fields[1] + ")");
		}
		try {
			if (fileValues.getSaldoSumm() != Long.parseLong(fields[2])) {
				throw new FlexPayException("Invalid data in file (total saldo summ in footer not equals with summ of saldos in all lines - " + fields[2] + ", but were" + fileValues.getSaldoSumm() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total saldo summ " + fields[2] + ")");
		}
		try {
			if (fileValues.getLines() != Integer.parseInt(fields[3])) {
				throw new FlexPayException("Invalid data in file (incorrect records number in file - " + fields[3] + ", but were " + fileValues.getLines() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Incorrect footer line (can't parse total amount of lines in file - " + fields[3] + ")");
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
