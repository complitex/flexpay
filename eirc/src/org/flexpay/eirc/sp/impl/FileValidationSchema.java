package org.flexpay.eirc.sp.impl;

import org.springframework.beans.factory.annotation.Required;

public class FileValidationSchema {
    private Class<MessageValidatorWithContext> headerValidator;
    private Class<MessageValidatorWithContext> recordValidator;
    private Class<MessageValidatorWithContext> footerValidator;

    public Class<MessageValidatorWithContext> getHeaderValidator() {
        return headerValidator;
    }

    @Required
    public void setHeaderValidator(Class<MessageValidatorWithContext> headerValidator) {
        this.headerValidator = headerValidator;
    }

    public Class<MessageValidatorWithContext> getRecordValidator() {
        return recordValidator;
    }

    @Required
    public void setRecordValidator(Class<MessageValidatorWithContext> recordValidator) {
        this.recordValidator = recordValidator;
    }

    public Class<MessageValidatorWithContext> getFooterValidator() {
        return footerValidator;
    }

    @Required
    public void setFooterValidator(Class<MessageValidatorWithContext> footerValidator) {
        this.footerValidator = footerValidator;
    }
}
