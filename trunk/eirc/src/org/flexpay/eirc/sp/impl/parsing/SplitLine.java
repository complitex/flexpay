package org.flexpay.eirc.sp.impl.parsing;

import org.flexpay.eirc.sp.impl.LineParser;
import org.jetbrains.annotations.NotNull;

public class SplitLine implements LineParser {
    private String delimiter = "=";

    @NotNull
    public String[] parse(@NotNull String line) {
        return line.split(delimiter);
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
