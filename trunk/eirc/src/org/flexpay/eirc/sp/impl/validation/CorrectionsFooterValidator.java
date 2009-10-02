package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.*;
import org.jetbrains.annotations.NotNull;

public class CorrectionsFooterValidator extends MessageValidatorWithContext<String> {

    private static final long FIELDS_LENGTH = 2;
    private FieldsValidator fieldsValidator;

    public CorrectionsFooterValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
        fieldsValidator = new FieldsValidator(mess);
    }

    public boolean validate(@NotNull String line) {
        String[] fields = context.getLineParser().parse(line, messenger);
		if (fields.length != FIELDS_LENGTH) {
			addErrorMessage("Not {} fields", FIELDS_LENGTH);
            return false;
		}
		if (!fieldsValidator.validate(fields)) {
            return false;
        }
		if (!fields[0].equals(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
			addErrorMessage("First field must be equals " + MbParsingConstants.LAST_FILE_STRING_BEGIN);
            return false;
		}
        Integer countLines = (Integer)context.getParam().get(ValidationConstants.COUNT_LINE);
		try {
			if (countLines != Integer.parseInt(fields[1])) { // TODO && !ignoreInvalidLinesNumber) {
				addErrorMessage("Wrong records number expected {}, but was {}", new Object[]{fields[1], countLines});
                return false;
			}
		} catch (NumberFormatException e) {
			addErrorMessage("Can't parse total amount of lines in file - {}", fields[1]);
            return false;
		}
        return true;
    }
}
