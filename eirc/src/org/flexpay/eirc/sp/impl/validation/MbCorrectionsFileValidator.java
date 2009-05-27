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
import java.util.ArrayList;
import java.util.List;

public class MbCorrectionsFileValidator extends MbFileValidator {

	public static final String ACCOUNT_CLOSED = "ЛИЦЕВОЙ ЗАКРЫТ";
	public static final DateFormat MODIFICATIONS_BEGIN_DATE_FORMAT = new SimpleDateFormat("ddMMyy");

	protected boolean validateFile(@NotNull FPFile spFile) throws FlexPayException {

		FileValues fileValues = new FileValues();

		BufferedReader reader = null;
		boolean ret = true;

		List<String> services = new ArrayList<String>();

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
						log.debug("Incorrect footer in file. Line number = {}, error: {}\nLine = {}", new Object[] {lineNum, e.getMessage(), line});
						ret = false;
//						throw new FlexPayException("Incorrect footer in file. Line number = " + lineNum, e);
					}
					log.debug("Validated {} records in file", lineNum - 2);
					break;
				} else {
					try {
						validateRecord(line, services);
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

//		log.debug("Services: {}", services);

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

	private void validateRecord(String line, List<String> services) throws FlexPayException {
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
/*
		for (String s : fields[20].split(";")) {
			if (!services.contains(s) && !s.equals("0")) {
				services.add(s);
			}
		}
		if (fields[20].equals("0")) {
			log.debug("{}", line);
		}
		if (fields[2].equals(ACCOUNT_CLOSED)) {
			log.debug("{} - {}", fields[2], fields[20]);
		}
*/
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
