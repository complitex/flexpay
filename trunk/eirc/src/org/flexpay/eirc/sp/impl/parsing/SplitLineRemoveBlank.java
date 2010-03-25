package org.flexpay.eirc.sp.impl.parsing;

import org.apache.commons.lang.StringUtils;
import org.flexpay.eirc.sp.impl.MessageLevel;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parse data.
 */
public class SplitLineRemoveBlank extends SplitLine {

    private String stripChars = null;

    /**
     * <p>Parse line to string array.</p>
     *
     * <p>Strips whitespace from the start and end of every String in an array.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * @param line String
     * @param messenger Messenger
     * @return the stripped Strings
     */
    @NotNull
	@Override
    public String[] parse(@NotNull String line, @Nullable Messenger messenger) {
        String[] fields = super.parse(line, messenger);
        String[] stripFields = StringUtils.stripAll(fields, stripChars);
        if (messenger != null) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].length() != stripFields[i].length()) {
                    messenger.addMessage("Line: \"{}\"\n content stripped field \"{}\" to \"{}\"", new Object[]{line, fields[i], stripFields[i]}, MessageLevel.WARN);
                }
            }
        }
        return stripFields;
    }

    /**
     * Set characters for parse
     *
     * @param stripChars the characters to remove, null treated as whitespace
     */
    public void setStripChars(String stripChars) {
        this.stripChars = stripChars;
    }

}
