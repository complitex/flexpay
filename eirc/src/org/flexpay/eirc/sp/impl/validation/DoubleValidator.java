package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.lang.StringUtils;
import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

public class DoubleValidator extends MessageValidator<String> {

    public DoubleValidator(@NotNull Messenger mess) {
        super(mess);
    }

    @Override
    public boolean validate(@NotNull String o) {
        try {
			if (StringUtils.isEmpty(o)) {
				Double.parseDouble(o);
			}
		} catch (Exception e) {
			addErrorMessage("Can't parse Double {}", o);
            return false;
		}
        return true;
    }
}
