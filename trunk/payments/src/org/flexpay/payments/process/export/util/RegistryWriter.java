package org.flexpay.payments.process.export.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.Signature;
import java.security.SignatureException;

public class RegistryWriter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private BufferedOutputStream bos;

    private FPFile file;
    private long size;

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

    private Signature signature;


    public RegistryWriter(@NotNull FPFile file) throws IOException {
        this(file, DEFAULT_SEPARATOR);
    }

    public RegistryWriter(@NotNull FPFile file, char separator) throws IOException {
        this(file, separator, DEFAULT_QUOTE_CHARACTER);
    }

    public RegistryWriter(@NotNull FPFile file, char separator, char quotechar) throws IOException {
        this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
    }

    public RegistryWriter(@NotNull FPFile file, char separator, char quotechar, char escapechar) throws IOException {
        this(file, separator, quotechar, escapechar, DEFAULT_LINE_END);
    }

    public RegistryWriter(@NotNull FPFile file, char separator, char quotechar, @NotNull String lineEnd) throws IOException {
        this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
    }

    public RegistryWriter(@NotNull FPFile file, char separator, char quotechar, char escapechar, @NotNull String lineEnd) throws IOException {
        this.file = file;
        this.bos = new BufferedOutputStream(file.getOutputStream());
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.lineEnd = lineEnd;
        signature = null;
        size = 0;
    }

    public void writeLine(@Nullable String[] nextLine) throws IOException, SignatureException {

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
        write(sb);

    }

    public void write(@NotNull byte[] bytes, int off, int len) throws SignatureException, IOException {
        if (signature != null) {
            signature.update(bytes, off, len);
        }
        size += len;
        bos.write(bytes, off, len);
    }

    public void write(@NotNull byte[] bytes) throws SignatureException, IOException {
        if (signature != null) {
            signature.update(bytes);
        }
        size += bytes.length;
        bos.write(bytes);
    }

    private void write(StringBuffer sb) throws IOException, SignatureException {
        byte[] bytes = sb.toString().getBytes(getFileEncoding());
        write(bytes);
    }

    public void writeLine(@Nullable String nextLine) throws IOException, SignatureException {

        if (nextLine == null)
            return;

        StringBuffer sb = new StringBuffer();

        appendCell(sb, nextLine);

        sb.append(lineEnd);
        log.debug("Write line:" + sb.toString());
        write(sb);

    }

    public void writeLine(@Nullable byte[] nextLine) throws IOException, SignatureException {

        if (nextLine == null)
            return;

        log.debug("Write line:{}", nextLine);
        write(nextLine);
        byte[] bytesLineEnd = lineEnd.getBytes(getFileEncoding());
        write(bytesLineEnd);
    }

    public void writeCharToLine(char ch, int count) throws IOException, SignatureException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        if (quotechar != NO_QUOTE_CHARACTER)
            sb.append(quotechar);

        sb.append(lineEnd);
        log.debug("Write line:" + sb.toString());
        write(sb);
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

    public byte[] getSign() throws SignatureException {
        return signature != null? signature.sign(): null;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public long getFileSize() throws FlexPayException {
        return size;
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
