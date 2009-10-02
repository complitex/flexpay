package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

public class OrganizationNameValidator extends MessageValidator<String> {
    private static final long NAME_LENGTH = 20;

    public OrganizationNameValidator(@NotNull Messenger mess) {
        super(mess);
    }

    public boolean validate(@NotNull String o) {
        if (o.length() > NAME_LENGTH) {
			addErrorMessage("Organization name length can't be more {} symbols", NAME_LENGTH);
            return false;
		}
        return true;
    }
}
