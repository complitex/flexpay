package org.flexpay.eirc.sp.validation;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.sp.Validator;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;

public class MbCorrectionsFileValidator implements Validator {

	private Logger log = LoggerFactory.getLogger(getClass());

	public final static DateFormat FILE_CREATION_DATE_FORMAT = new SimpleDateFormat("ddMMyy");
	public final static DateFormat MODIFICATIONS_BEGIN_DATE_FORMAT = new SimpleDateFormat("ddMMyy");
	public final static String LAST_FILE_STRING_BEGIN = "999999999";
	public final static String REGISTRY_FILE_ENCODING = "Cp866";
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
						throw new FlexPayException("Incorrect header in file. Line number = " + lineNum, e);
					}
				} else if (lineNum == 19340 || lineNum == 19439
						|| lineNum == 19450 || lineNum == 19492
						|| lineNum == 25492 || lineNum == 25495
						|| lineNum == 25563 || lineNum == 25581
						|| lineNum == 31679 || lineNum == 31809
						|| lineNum == 38016 || lineNum == 38056
						|| lineNum == 38188 || lineNum == 43834
						|| lineNum == 43837 || lineNum == 43848
						|| lineNum == 43891 || lineNum == 44041
						|| lineNum == 44044 || lineNum == 49607
						|| lineNum == 49609 || lineNum == 49664
						|| lineNum == 49680 || lineNum == 49759
						|| lineNum == 59193 || lineNum == 75116
						|| lineNum == 75298 || lineNum == 75324
						|| lineNum == 105690 || lineNum == 108907
						|| lineNum == 136493 || lineNum == 136695
						|| lineNum == 167513 || lineNum == 167564
						|| lineNum == 167696 || lineNum == 167702
						|| lineNum == 167705 || lineNum == 167769
						|| lineNum == 167781 || lineNum == 167784
						|| lineNum == 167833 || lineNum == 198577
						|| lineNum == 198642 || lineNum == 198648
						|| lineNum == 198669 || lineNum == 229311
						|| lineNum == 229380 || lineNum == 229539
						|| lineNum == 229688 || lineNum == 259886
						|| lineNum == 289980 || lineNum == 290106
						|| lineNum == 290163 || lineNum == 290174
						|| lineNum == 290177 || lineNum == 290281
						|| lineNum == 290302 || lineNum == 290322
						|| lineNum == 290389 || lineNum == 297778
						|| lineNum == 320443 || lineNum == 320518
						|| lineNum == 320529 || lineNum == 320549
						|| lineNum == 320595 || lineNum == 320612
						|| lineNum == 320622 || lineNum == 320710
						|| lineNum == 320727 || lineNum == 355126) {

				} else if (line.startsWith(LAST_FILE_STRING_BEGIN)) {
					fileValues.setLines(lineNum - 2);
					try {
						validateFooter(line, fileValues);
					} catch (Exception e) {
						throw new FlexPayException("Incorrect footer in file. Line number = " + lineNum, e);
					}
					break;
				} else {
					try {
						validateRecord(line);
					} catch (Exception e) {
						log.debug("Incorrect record in file. Line number = {}", lineNum);
						//throw new FlexPayException("Incorrect record in file. Line number = " + lineNum + ". Line = " + line, e);
					}
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
		if (fields.length != 3) {
			throw new FlexPayException("Not 3 fields");
		}
/*
		if (fields[0].length() > 20) {
			throw new FlexPayException("Organization name length can't be more 20 symbols (was " + fields[0].length() + ", " + fields[0] + ")");
		}
*/
		try {
			Long.parseLong(fields[1]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse organization code " + fields[1], e);
		}
		try {
			FILE_CREATION_DATE_FORMAT.parse(fields[2]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse file creation date " + fields[2], e);
		}
	}

	private void validateRecord(String line) throws FlexPayException {
		String[] fields = line.split("=");
		if (fields.length != 28) {
			throw new FlexPayException("Not 28 fields");
		}
		try {
			Long.parseLong(fields[3]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse city id " + fields[3], e);
		}
		try {
			Long.parseLong(fields[4]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse ERC street id " + fields[4], e);
		}
		try {
			Long.parseLong(fields[5]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse organization street id " + fields[5], e);
		}
		try {
			Double.parseDouble(fields[10]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse full square " + fields[10], e);
		}
		try {
			Double.parseDouble(fields[11]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse living space " + fields[11], e);
		}
		try {
			Double.parseDouble(fields[12]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse balcony square " + fields[12], e);
		}
		try {
			Double.parseDouble(fields[13]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse loggia square " + fields[13], e);
		}
		try {
			Long.parseLong(fields[14]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse registered persons quantity " + fields[14], e);
		}
		try {
			Long.parseLong(fields[15]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse residents persons quantity " + fields[15], e);
		}
		try {
			Long.parseLong(fields[16]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse privileged persons quantity " + fields[16], e);
		}
		try {
			Long.parseLong(fields[17]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse privileged persons quantity " + fields[17], e);
		}
		try {
			MODIFICATIONS_BEGIN_DATE_FORMAT.parse(fields[19]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse modifications begin date " + fields[19], e);
		}
		if (!fields[21].equals("0") && !fields[21].equals("1")) {
			throw new FlexPayException("Invalid sign of lift availability " + fields[21]);
		}
		try {
			Long.parseLong(fields[22]);
		} catch (Exception e) {
			throw new FlexPayException("Can't parse rooms quantity " + fields[22], e);
		}
		if (!fields[23].equals("0") && !fields[23].equals("1")) {
			throw new FlexPayException("Invalid sign of lift availability " + fields[23]);
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
		if (!fields[0].equals(LAST_FILE_STRING_BEGIN)) {
			throw new FlexPayException("First field must be equals 999999999");
		}
		try {
			if (fileValues.getLines() != Integer.parseInt(fields[1])) {
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

}
