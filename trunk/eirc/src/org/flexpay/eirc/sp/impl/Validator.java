package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;

public interface Validator<T> {
    boolean validate(@NotNull T o);
}
