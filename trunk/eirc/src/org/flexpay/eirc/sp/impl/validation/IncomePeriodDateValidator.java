package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class IncomePeriodDateValidator extends MessageValidator<String> {
    private final String INCOME_PERIOD_DATE_FORMAT = "MMyy";

    public IncomePeriodDateValidator(@NotNull Messenger mess) {
        super(mess);
    }

    public boolean validate(@NotNull String o) {
        try {
			new SimpleDateFormat(INCOME_PERIOD_DATE_FORMAT).parse(o);
		} catch (Exception e) {
			addErrorMessage("Can't parse income period {}", o);
            return false;
		}
        return true;
    }
}
