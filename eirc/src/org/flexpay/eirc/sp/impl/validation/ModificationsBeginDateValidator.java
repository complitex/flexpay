package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModificationsBeginDateValidator extends MessageValidatorWithContext<String> {

    private static final String MODIFICATIONS_BEGIN_DATE_FORMAT = "ddMMyy";

    public ModificationsBeginDateValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    @Override
    public boolean validate(@NotNull String o) {
        Date beginDate;
		try {
			beginDate = new SimpleDateFormat(MODIFICATIONS_BEGIN_DATE_FORMAT).parse(o);
		} catch (Exception e) {
			addErrorMessage("Can't parse modifications begin date {}", o);
            return false;
		}
        context.getParam().put(ValidationConstants.MODIFICATIONS_BEGIN_DATE, beginDate);
        return true;
    }
}
