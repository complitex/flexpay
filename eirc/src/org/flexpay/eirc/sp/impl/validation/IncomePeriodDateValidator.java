package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomePeriodDateValidator extends MessageValidatorWithContext<String> {
    private final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

    public IncomePeriodDateValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    @Override
    public boolean validate(@NotNull String o) {
        try {
			Date incomePeriodDate = new SimpleDateFormat(INCOME_PERIOD_DATE_FORMAT).parse(o);
			context.getParam().put(ValidationConstants.INCOME_PERIOD_DATE, incomePeriodDate);
		} catch (Exception e) {
			addErrorMessage("Can't parse income period {}", o);
            return false;
		}
        return true;
    }
}
