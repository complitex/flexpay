package org.flexpay.payments.process.export.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.flexpay.common.exception.FlexPayException;

import java.io.*;

public class RegistryWriter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private BufferedOutputStream bos;

    private File file;

    private char separator;

    private char quotechar;

    private char escapechar;

    private String lineEnd;

    private String fileEncoding;

    public static final char DEFAULT_ESCAPE_CHARACTER = '"';

    public static final char DEFAULT_SEPARATOR = ',';

    public static final char DEFAULT_QUOTE_CHARACTER = '"';

    public static final char NO_QUOTE_CHARACTER = '\u0000';

    public static final char NO_ESCAPE_CHARACTER = '\u0000';

    public static final String DEFAULT_LINE_END = "\n";


    public RegistryWriter(@NotNull File file) throws FileNotFoundException {
        this(file, DEFAULT_SEPARATOR);
    }

    public RegistryWriter(@NotNull File file, char separator) throws FileNotFoundException {
        this(file, separator, DEFAULT_QUOTE_CHARACTER);
    }

    public RegistryWriter(@NotNull File file, char separator, char quotechar) throws FileNotFoundException {
        this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
    }

    public RegistryWriter(@NotNull File file, char separator, char quotechar, char escapechar) throws FileNotFoundException {
        this(file, separator, quotechar, escapechar, DEFAULT_LINE_END);
    }

    public RegistryWriter(@NotNull File file, char separator, char quotechar, @NotNull String lineEnd) throws FileNotFoundException {
        this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
    }

    public RegistryWriter(@NotNull File file, char separator, char quotechar, char escapechar, @NotNull String lineEnd) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(file);
        this.file = file;
        this.bos = new BufferedOutputStream(fos);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
    }

    public void writeLine(@Nullable String[] nextLine) throws IOException {

        if (nextLine == null)
            return;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nextLine.length; i++) {

            if (i != 0) {
                sb.append(separator);
            }

            String nextElement = nextLine[i];
            if (nextElement == null)
                continue;
            appendCell(sb, nextElement);
        }

        sb.append(lineEnd);

        log.debug("Write line:" + sb.toString());
        bos.write(sb.toString().getBytes(getFileEncoding()));

    }

    public void writeLine(@Nullable String nextLine) throws IOException {

        if (nextLine == null)
            return;

        StringBuffer sb = new StringBuffer();

        appendCell(sb, nextLine);

        sb.append(lineEnd);
        log.debug("Write line:" + sb.toString());
        bos.write(sb.toString().getBytes(getFileEncoding()));

    }

    public void writeLine(@Nullable byte[] nextLine) throws IOException {

        if (nextLine == null)
            return;

        log.debug("Write line:{}", nextLine);
        bos.write(nextLine);
        bos.write(lineEnd.getBytes(getFileEncoding()));
    }

    public void writeCharToLine(char ch, int count) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        if (quotechar != NO_QUOTE_CHARACTER)
            sb.append(quotechar);

        sb.append(lineEnd);
        log.debug("Write line:" + sb.toString());
        bos.write(sb.toString().getBytes(getFileEncoding()));
    }

    private void appendCell(@NotNull StringBuffer sb, @NotNull String nextLine) {
        if (quotechar != NO_QUOTE_CHARACTER)
            sb.append(quotechar);
        for (int j = 0; j < nextLine.length(); j++) {
            char nextChar = nextLine.charAt(j);
            if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
                sb.append(escapechar).append(nextChar);
            } else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
                sb.append(escapechar).append(nextChar);
            } else {
                sb.append(nextChar);
            }
        }
        if (quotechar != NO_QUOTE_CHARACTER)
            sb.append(quotechar);
    }

    public long getFileSize() throws FlexPayException {
        flush();
        return file.length();
    }

    public void flush() throws FlexPayException {
        try {
            log.info("flush stream");
            bos.flush();
        } catch (IOException e) {
            throw new FlexPayException(e);
        }
    }

    public void close() throws FlexPayException {
        try {
            log.info("flush and close stream");
            bos.flush();
            bos.close();
        } catch (IOException e) {
            throw new FlexPayException(e);
        }
    }

    public void setFileEncoding(@NotNull String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    @NotNull
    public String getFileEncoding() {
        return fileEncoding != null ? fileEncoding : "Cp866";
    }
}
