package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class SplitLineRemoveBlank extends SplitLine {
    @NotNull
    public String[] parse(@NotNull String line) {
        return StringUtils.stripAll(super.parse(line));
    }
}
