package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

public class FieldsValidator extends MessageValidator<String[]> {
    public FieldsValidator(@NotNull Messenger mess) {
        super(mess);
    }

    public boolean validate(@NotNull String[] fields) {
        String errorFields = "";
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].startsWith(" ") || (fields[i].endsWith(" ") && i < fields.length - 1)) {
				errorFields += (errorFields.length() > 0 ? ", " : "") + (i + 1);
			}
		}
		if (errorFields.length() > 0) {
			addErrorMessage("Incorrect fields in record. Detect spaces after or before \"=\" (fields: {})", errorFields);
            return false;
		}
        return true;
    }
}
