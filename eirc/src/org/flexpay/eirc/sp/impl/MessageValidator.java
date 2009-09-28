package org.flexpay.eirc.sp.impl;

import org.flexpay.eirc.sp.impl.Messager;
import org.jetbrains.annotations.NotNull;

public abstract class MessageValidator<T> implements Validator<T> {
    private Messager messager;

    private MessageValidator() {
    }

    public MessageValidator(@NotNull Messager mess) {
        messager = mess;
    }

    protected void addErrorMessage(@NotNull String message) {
        messager.addMessage(message);
    }

    protected void addErrorMessage(@NotNull String message, Object o) {
        messager.addMessage(message, o);
    }

    protected void addErrorMessage(@NotNull String message, @NotNull Object[] o) {
        messager.addMessage(message, o);
    }
}
