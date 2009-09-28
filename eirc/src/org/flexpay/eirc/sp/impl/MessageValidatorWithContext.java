package org.flexpay.eirc.sp.impl;

import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class MessageValidatorWithContext<T> extends MessageValidator<T>{
    protected ValidationContext context;

    public MessageValidatorWithContext(@NotNull Messager mess, @NotNull ValidationContext context) {
        super(mess);
        this.context = context;
    }
}
