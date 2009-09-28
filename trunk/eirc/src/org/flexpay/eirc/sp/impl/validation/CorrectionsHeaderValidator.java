package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messager;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

public class CorrectionsHeaderValidator extends MessageValidatorWithContext<String> {
    private static final long FIELDS_LENGTH = 3;

    private FieldsValidator fieldsValidator;
    private ServiceProviderValidator serviceProviderValidator;
    private FileCreationDateValidator fileCreationDateValidator;
    private OrganizationNameValidator organizationNameValidator;

    public CorrectionsHeaderValidator(@NotNull Messager mess, @NotNull ValidationContext context) {
        super(mess, context);
        fieldsValidator = new FieldsValidator(mess);
        serviceProviderValidator = new ServiceProviderValidator(mess, context);
        fileCreationDateValidator = new FileCreationDateValidator(mess, context);
        organizationNameValidator = new OrganizationNameValidator(mess);
    }

    public boolean validate(@NotNull String line) {
        String[] fields = line.split("=");
		if (fields.length != FIELDS_LENGTH) {
			addErrorMessage("Not {} fields", FIELDS_LENGTH);
            return false;
		}

        return fieldsValidator.validate(fields) && organizationNameValidator.validate(fields[0]) &&
                serviceProviderValidator.validate(fields[1]) && fileCreationDateValidator.validate(fields[2]);
    }
}
