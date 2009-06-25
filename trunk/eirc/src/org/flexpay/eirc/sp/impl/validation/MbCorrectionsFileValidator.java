package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.eirc.sp.impl.MbFileValidator;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MbCorrectionsFileValidator extends MbFileValidator {

	private final DateFormat MODIFICATIONS_BEGIN_DATE_FORMAT = new SimpleDateFormat("ddMMyy");

	private boolean ignoreInvalidLinesNumber = false;

	protected boolean validateFile(@NotNull FPFile spFile) throws FlexPayException {

		FileValues fileValues = new FileValues();

		BufferedReader reader = null;
		boolean ret = true;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(spFile.getInputStream(), REGISTRY_FILE_ENCODING));

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
						validateHeader(line);
					} catch (Exception e) {
						log.warn("Incorrect header in file. Line number = {}, error: {}\nLine = {}",
								new Object[]{lineNum, e.getMessage(), line});
						ret = false;
					}
				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					fileValues.setLines(lineNum - 2);
					try {
						validateFooter(line, fileValues);
					} catch (Exception e) {
						log.warn("Incorrect footer in file. Line number = {}, error: {}\nLine = {}",
								new Object[]{lineNum, e.getMessage(), line});
						ret = false;
					}
					log.debug("Validated {} records in file", lineNum - 2);
					break;
				} else {
					try {
						validateRecord(line);
					} catch (Exception e) {
						log.warn("Incorrect record in file. Line number = {}, error: {}\nLine = {}",
								new Object[]{lineNum, e.getMessage(), line});
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

	private void validateHeader(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 3) {
			throw new FlexPayException("Not 3 fields");
		}
		validateFields(fields);
		if (fields[0].length() > 20) {
			throw new FlexPayException("Organization name length can't be more 20 symbols (was " + fields[0].length() + ", " + fields[0] + ")");
		}
		try {
			Long.parseLong(fields[1]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse organization code " + fields[1]);
		}
		try {
			new SimpleDateFormat(FILE_CREATION_DATE_FORMAT).parse(fields[2]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse file creation date " + fields[2]);
		}
	}

	private void validateRecord(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 28) {
			throw new FlexPayException("Not 28 fields");
		}
		validateFields(fields);
		try {
			Long.parseLong(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse city id " + fields[3]);
		}
		try {
			Long.parseLong(fields[4]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse ERC street id " + fields[4]);
		}
		try {
			Long.parseLong(fields[5]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse organization street id " + fields[5]);
		}
		try {
			Double.parseDouble(fields[10]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse full square " + fields[10]);
		}
		try {
			Double.parseDouble(fields[11]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse living space " + fields[11]);
		}
		try {
			Double.parseDouble(fields[12]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse balcony square " + fields[12]);
		}
		try {
			Double.parseDouble(fields[13]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse loggia square " + fields[13]);
		}
		try {
			Long.parseLong(fields[14]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse registered persons quantity " + fields[14]);
		}
		try {
			Long.parseLong(fields[15]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse residents persons quantity " + fields[15]);
		}
		try {
			Long.parseLong(fields[16]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse privileged persons quantity " + fields[16]);
		}
		try {
			Long.parseLong(fields[17]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse privileged persons quantity " + fields[17]);
		}
		try {
			MODIFICATIONS_BEGIN_DATE_FORMAT.parse(fields[19]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse modifications begin date " + fields[19]);
		}

		if (!fields[21].equals("0") && !fields[21].equals("1")) {
			throw new FlexPayException("Invalid sign of lift availability " + fields[21]);
		}
		try {
			Long.parseLong(fields[22]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse rooms quantity " + fields[22]);
		}
		if (!fields[23].equals("0") && !fields[23].equals("1")) {
			throw new FlexPayException("Invalid sign of floor electric furnaces availability " + fields[23]);
		}
		if (!fields[24].equals("0") && !fields[24].equals("1")
			&& !fields[24].equals("2") && !fields[24].equals("3")) {
			throw new FlexPayException("Invalid view property " + fields[24]);
		}
		if (!fields[25].equals("0") && !fields[25].equals("1")
			&& !fields[25].equals("2") && !fields[25].equals("3")) {
			throw new FlexPayException("Invalid sign of well " + fields[25]);
		}
	}

	private void validateFooter(String line, FileValues fileValues) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 2) {
			throw new FlexPayException("Not 2 fields)");
		}
		validateFields(fields);
		if (!fields[0].equals(LAST_FILE_STRING_BEGIN)) {
			throw new FlexPayException("First field must be equals " + LAST_FILE_STRING_BEGIN);
		}
		try {
			if (fileValues.getLines() != Integer.parseInt(fields[1]) && !ignoreInvalidLinesNumber) {
				throw new FlexPayException("Incorrect records number in file - " + fields[1] + ", but were " + fileValues.getLines() + ")");
			}
		} catch (NumberFormatException e) {
			throw new FlexPayException("Can't parse total amount of lines in file - " + fields[1]);
		}
	}

	private class FileValues {

		private int lines = 0;

		public int getLines() {
			return lines;
		}

		public void setLines(int lines) {
			this.lines = lines;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
					append("lines", lines).toString();
		}

	}

	public boolean isIgnoreInvalidLinesNumber() {
		return ignoreInvalidLinesNumber;
	}

	public void setIgnoreInvalidLinesNumber(boolean ignoreInvalidLinesNumber) {
		this.ignoreInvalidLinesNumber = ignoreInvalidLinesNumber;
	}
}
