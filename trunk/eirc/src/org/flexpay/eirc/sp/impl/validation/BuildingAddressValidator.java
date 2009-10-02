package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.lang.StringUtils;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.MessageValidator;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

public class BuildingAddressValidator extends MessageValidator<String> {
    public BuildingAddressValidator(@NotNull Messenger mess) {
        super(mess);
    }

    public boolean validate(@NotNull String mbBuidingAddress) {
		String[] parts = StringUtils.split(mbBuidingAddress, ' ');
		if (parts.length > 1 && !parts[1].startsWith(MbParsingConstants.BUILDING_BULK_PREFIX)) {
			addErrorMessage("Invalid building bulk value: " + parts[1]);
            return false;
        }
		return true;
	}
}
