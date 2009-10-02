package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

public class LongValidator extends MessageValidatorWithContext<String> {
    public static final String LONG_VALUE = "LongValue";

    public LongValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    public boolean validate(@NotNull String o) {
        try {
			Long value = Long.parseLong(o);
            context.getParam().put(LONG_VALUE, value);
		} catch (Exception e) {
			addErrorMessage("Can't parse Long {}", o);
            return false;
		}
        return true;
    }
}
