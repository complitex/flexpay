package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;

public interface Messenger {
    void addMessage(@NotNull String message);

    void addMessage(@NotNull String message, @NotNull MessageLevel level);

    void addMessage(@NotNull String message, Object o);

    void addMessage(@NotNull String message, Object o, @NotNull MessageLevel level);

    void addMessage(@NotNull String message, Object[] o);

    void addMessage(@NotNull String message, Object[] o, @NotNull MessageLevel level);
}
