package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileCreationDateValidator extends MessageValidatorWithContext<String> {
    public static final String TILL_DATE = "TillDate";
    public static final String FROM_DATE = "FromDate";

    public FileCreationDateValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    @Override
    public boolean validate(@NotNull String o) {
        try {
			Date period = new SimpleDateFormat(MbParsingConstants.FILE_CREATION_DATE_FORMAT).parse(o);
            context.getParam().put(FROM_DATE, period);
            context.getParam().put(TILL_DATE, period);
		} catch (Exception e) {
			addErrorMessage("Can't parse file creation date {}", o);
            return false;
		}
        return true;
    }
}
