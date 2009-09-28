package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messager;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperationDateValidator extends MessageValidatorWithContext<String> {
    private static final String OPERATION_DATE_FORMAT = "MMyy";

    public OperationDateValidator(@NotNull Messager mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    public boolean validate(@NotNull String o) {
        try {
			Date operationDate = new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(o);
            context.getParam().put(ValidationConstants.MODIFICATIONS_BEGIN_DATE, operationDate);
		} catch (Exception e) {
			addErrorMessage("Can't parse operation date {}", o);
            return false;
		}
        return true;
    }
}
