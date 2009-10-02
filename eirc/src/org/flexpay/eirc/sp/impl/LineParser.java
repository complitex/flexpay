package org.flexpay.eirc.sp.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LineParser {
    @NotNull
    String[] parse(@NotNull String line);

    @NotNull
    String[] parse(@NotNull String line, @Nullable Messenger messenger);
}
