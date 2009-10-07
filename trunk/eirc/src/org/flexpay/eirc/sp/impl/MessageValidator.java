package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;

public abstract class MessageValidator<T> implements Validator<T> {
    protected Messenger messenger;

    private MessageValidator() {
    }

    public MessageValidator(@NotNull Messenger mess) {
        messenger = mess;
    }

    protected void addErrorMessage(@NotNull String message) {
        messenger.addMessage(message);
    }

    protected void addErrorMessage(@NotNull String message, Object o) {
        messenger.addMessage(message, o);
    }

    protected void addErrorMessage(@NotNull String message, @NotNull Object[] o) {
        messenger.addMessage(message, o);
    }
}
