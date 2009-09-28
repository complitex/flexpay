package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messager;
import org.jetbrains.annotations.NotNull;

public class DoubleValidator extends MessageValidator<String> {
    public DoubleValidator(@NotNull Messager mess) {
        super(mess);
    }

    public boolean validate(@NotNull String o) {
        try {
			Double.parseDouble(o);
		} catch (Exception e) {
			addErrorMessage("Can't parse Double {}", o);
            return false;
		}
        return true;
    }
}
