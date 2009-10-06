package org.flexpay.eirc.sp.impl.parsing;

import org.flexpay.eirc.sp.impl.LineParser;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SplitLine implements LineParser {

    private String delimiter = "=";

    @NotNull
	@Override
    public String[] parse(@NotNull String line) {
        return parse(line, null);
    }

    @NotNull
	@Override
    public String[] parse(@NotNull String line, @Nullable Messenger messenger) {
        return line.split(delimiter);
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

}
