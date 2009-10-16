package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;

public abstract class MessageValidatorWithContext<T> extends MessageValidator<T>{

    protected ValidationContext context;

    public MessageValidatorWithContext(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess);
        this.context = context;
    }
}
