package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;

public interface LineParser {
    @NotNull
    String[] parse(@NotNull String line);
}
