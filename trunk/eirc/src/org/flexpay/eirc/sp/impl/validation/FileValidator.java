package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.eirc.sp.impl.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileValidator extends MessageValidator<FPFile> {
	protected Logger log = LoggerFactory.getLogger(getClass());

	private ServiceValidationFactory serviceValidationFactory;
	private FileValidationSchema schema;
	private LineParser lineParser;

	public FileValidator(@NotNull Messenger mess, @NotNull ServiceValidationFactory serviceValidationFactory, @NotNull FileValidationSchema schema,
						 @NotNull LineParser lineParser) {
		super(mess);
		this.serviceValidationFactory = serviceValidationFactory;
		this.schema = schema;
		this.lineParser = lineParser;
	}

	@Override
	public boolean validate(@NotNull FPFile spFile) {
		ValidationContext context = new ValidationContext(serviceValidationFactory, lineParser);

		MessageValidatorWithContext<String> headerValidator = serviceValidationFactory
				.getNewInstanceValidator(schema.getHeaderValidator(), messenger, context);
		if (headerValidator == null) {
			return false;
		}

		MessageValidatorWithContext<String> footerValidator = serviceValidationFactory
				.getNewInstanceValidator(schema.getFooterValidator(), messenger, context);
		if (footerValidator == null) {
			return false;
		}

		MessageValidatorWithContext<String> recordValidator = serviceValidationFactory
				.getNewInstanceValidator(schema.getRecordValidator(), messenger, context);
		if (recordValidator == null) {
			return false;
		}

		BufferedReader reader = null;
		boolean ret = true;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(
					spFile.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
			char[] startLine = new char[MbParsingConstants.FIRST_FILE_STRING_SIZE];

			if (reader.read(startLine, 0, MbParsingConstants.FIRST_FILE_STRING_SIZE) < MbParsingConstants.FIRST_FILE_STRING_SIZE) {
				addErrorMessage("Incorrect start line in file");
				return false;
			}
			String firstLineLastSymbols = reader.readLine();
			if (firstLineLastSymbols == null || firstLineLastSymbols.length() > 0) {
				addErrorMessage("Incorrect start line in file");
				return false;
			}

			for (int lineNum = 1; ; lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					addErrorMessage("Can't read file line");
					return false;
				}
				if (lineNum == 1) {
					if (!headerValidator.validate(line)) {
						addErrorMessage("Incorrect header in file. Line number = {}\nLine = {}",
								new Object[]{lineNum, line});
						ret = false;
						break;
					}
				} else if (line.startsWith(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
					context.getParam().put(ValidationConstants.COUNT_LINE, lineNum - 2);
					if (!footerValidator.validate(line)) {
						addErrorMessage("Incorrect footer in file. Line number = {}\nLine = {}",
								new Object[]{lineNum, line});
						ret = false;
					}
					messenger.addMessage("Validated {} records in file", lineNum - 2, MessageLevel.INFO);
					break;
				} else {
					if (!recordValidator.validate(line)) {
						addErrorMessage("Incorrect record in file. Line number = {}\n Line = {}",
								new Object[]{lineNum, line});
						ret = false;
					}
				}
			}
		} catch (IOException e) {
			messenger.addMessage("Error with reading file: {}", e, MessageLevel.ERROR);
			ret = false;
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return ret;
	}
}
