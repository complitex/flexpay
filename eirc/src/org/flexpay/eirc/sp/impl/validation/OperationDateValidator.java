package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperationDateValidator extends MessageValidatorWithContext<String> {
    private static final String OPERATION_DATE_FORMAT = "MMyy";

    public OperationDateValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    @Override
    public boolean validate(@Nullable String o) {
        try {
			Date operationDate;
			if (o != null) {
				operationDate = new SimpleDateFormat(OPERATION_DATE_FORMAT).parse(o);
			} else {
				operationDate = (Date)context.getParam().get(ValidationConstants.INCOME_PERIOD_DATE);
			}
            context.getParam().put(ValidationConstants.MODIFICATIONS_BEGIN_DATE, operationDate);
		} catch (Exception e) {
			addErrorMessage("Can't parse operation date {}", o);
            return false;
		}
        return true;
    }
}
