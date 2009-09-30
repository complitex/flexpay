package org.flexpay.eirc.sp.impl;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.sp.impl.messager.CompositMessager;
import org.flexpay.eirc.sp.impl.messager.ErrorMessager;
import org.flexpay.eirc.sp.impl.messager.WarningMessager;
import org.flexpay.eirc.sp.impl.validation.FileValidator;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Constructor;

public class ServiceValidationFactory {
    private Logger log = LoggerFactory.getLogger(getClass());

    private ServiceTypesMapper serviceTypesMapper;
    private SPService spService;
    private CorrectionsService correctionsService;
    private DataSourceDescription megabankSD;

    public ServiceValidationFactory() {
    }

    public ServiceTypesMapper getServiceTypesMapper() {
        return serviceTypesMapper;
    }

    @Required
    public void setServiceTypesMapper(ServiceTypesMapper serviceTypesMapper) {
        this.serviceTypesMapper = serviceTypesMapper;
    }

    public SPService getSpService() {
        return spService;
    }

    @Required
    public void setSpService(SPService spService) {
        this.spService = spService;
    }

    public CorrectionsService getCorrectionsService() {
        return correctionsService;
    }

    @Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    public DataSourceDescription getMegabankSD() {
        return megabankSD;
    }

    @Required
    public void setMegabankSD(DataSourceDescription megabankSD) {
        this.megabankSD = megabankSD;
    }

    @NotNull
    public FileValidator createFileValidator(@NotNull FileValidationSchema schema, @NotNull LineParser lineParser, @Nullable Logger log) {
        List<Messager> listMessagers = new ArrayList<Messager>();
        listMessagers.add(new WarningMessager(LoggerFactory.getLogger(FileValidator.class)));
        if (log != null) {
            listMessagers.add(new ErrorMessager(log));
        }
        CompositMessager messager = new CompositMessager(listMessagers);

        return new FileValidator(messager, this, schema, lineParser);
    }

    public MessageValidatorWithContext getNewInstanceValidator(@NotNull Class<MessageValidatorWithContext> cls, @NotNull Messager messager, @NotNull ValidationContext context) {
        try {
            Constructor<MessageValidatorWithContext> headerValidatorConstructor = cls.getConstructor(Messager.class, ValidationContext.class);
            return headerValidatorConstructor.newInstance(messager, context);
        } catch (Throwable th) {
            log.error("Missing validator class", th);
            messager.addMessage("Inner error");
        }
        return null;
    }
}