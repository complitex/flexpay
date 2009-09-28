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
import java.lang.reflect.Constructor;

public class FileValidator extends MessageValidator<FPFile> {
    protected Logger log = LoggerFactory.getLogger(getClass());

    private Messager messager;
    private ServiceValidationFactory serviceValidationFactory;
    private FileValidationSchema schema;

    public FileValidator(@NotNull Messager mess, @NotNull ServiceValidationFactory serviceValidationFactory, @NotNull FileValidationSchema schema) {
        super(mess);
        this.messager = mess;
        this.serviceValidationFactory = serviceValidationFactory;
        this.schema = schema;
    }

    public boolean validate(@NotNull FPFile spFile) {
        ValidationContext context = new ValidationContext(serviceValidationFactory);

        MessageValidatorWithContext headerValidator = getNewInstanceValidator(schema.getHeaderValidator(), context);
        if (headerValidator == null) {
            return false;
        }

        MessageValidatorWithContext footerValidator = getNewInstanceValidator(schema.getFooterValidator(), context);
        if (footerValidator == null) {
            return false;
        }

        MessageValidatorWithContext recordValidator = getNewInstanceValidator(schema.getRecordValidator(), context);
        if (recordValidator == null) {
            return false;
        }

        BufferedReader reader = null;
		boolean ret = true;

		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(spFile.getInputStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));

			for (int lineNum = 0; ; lineNum++) {
				String line = reader.readLine();
				if (line == null) {
					addErrorMessage("Can't read file line");
                    return false;
				}
				if (lineNum == 0) {
					if (!MbParsingConstants.FIRST_FILE_STRING.equals(line)) {
						addErrorMessage("First line must be equals 300 spaces");
                        return false;
					}
				} else if (lineNum == 1) {
					if (!headerValidator.validate(line)) {
						addErrorMessage("Incorrect header in file. Line number = {}\nLine = {}",
								new Object[] {lineNum, line});
						ret = false;
						break;
					}
				} else if (line.startsWith(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
					context.getParam().put(ValidationConstants.COUNT_LINE, lineNum - 2);
					if (!footerValidator.validate(line)) {
					    addErrorMessage("Incorrect footer in file. Line number = {}\nLine = {}", new Object[]{lineNum, line});
						ret = false;
					}
					//TODO log.debug("Validated {} records in file", lineNum - 2);
					break;
				} else {
					if (!recordValidator.validate(line)) {
						addErrorMessage("Incorrect record in file. Line number = {}\n Line = {}", new Object[]{lineNum, line});
						ret = false;
					}
				}
			}
		} catch (IOException e) {
			addErrorMessage("Error with reading file: {}", e);
            ret = false;
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return ret;
    }

    private MessageValidatorWithContext getNewInstanceValidator(@NotNull Class<MessageValidatorWithContext> cls, @NotNull ValidationContext context) {
        try {
            Constructor<MessageValidatorWithContext> headerValidatorConstructor = cls.getConstructor(Messager.class, ValidationContext.class);
            return headerValidatorConstructor.newInstance(messager, context);
        } catch (Throwable th) {
            log.error("Missing validator class", th);
            messager.addMessage("Inner error");
        }
        return null;
    }
}
