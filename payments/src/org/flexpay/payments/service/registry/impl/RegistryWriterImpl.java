package org.flexpay.payments.service.registry.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.payments.service.registry.RegistryWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.SignatureException;

public class RegistryWriterImpl implements RegistryWriter {

	private final static Logger log = LoggerFactory.getLogger(RegistryWriterImpl.class);

	private static final char DEFAULT_ESCAPE_CHARACTER = '"';
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE_CHARACTER = '"';

	private BufferedOutputStream bos;
	private long size;
	private char separator;
	private char quotechar;
	private char escapechar;
	private String lineEnd;
	private String fileEncoding;
	private Signature signature;

	public RegistryWriterImpl(@NotNull FPFile file) throws IOException {
		this(file, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
	}

	public RegistryWriterImpl(@NotNull FPFile file, char separator, char quotechar) throws IOException {
		this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	}

	public RegistryWriterImpl(@NotNull FPFile file, char separator, char quotechar, char escapechar) throws IOException {
		this(file, separator, quotechar, escapechar, DEFAULT_LINE_END);
	}

	public RegistryWriterImpl(@NotNull FPFile file, char separator, char quotechar, char escapechar, @NotNull String lineEnd) throws IOException {
		this.bos = new BufferedOutputStream(file.getOutputStream());
		this.separator = separator;
		this.quotechar = quotechar;
		this.escapechar = escapechar;
		this.lineEnd = lineEnd;
		signature = null;
		size = 0;
	}

	public void writeLine(@Nullable String[] nextLine) throws IOException, SignatureException {

		if (nextLine == null) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nextLine.length; i++) {

			if (i != 0) {
				sb.append(separator);
			}

			String nextElement = nextLine[i];
			if (nextElement == null) {
				continue;
			}

			appendCell(sb, nextElement);
		}
		sb.append(lineEnd);

		log.debug("Write line: {}", sb);
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

	public void write(CharSequence cs) throws IOException, SignatureException {

		byte[] bytes = cs.toString().getBytes(getFileEncoding());
		write(bytes);
	}

	public void writeLine(@Nullable String nextLine) throws IOException, SignatureException {

		if (nextLine == null) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		appendCell(sb, nextLine);
		sb.append(lineEnd);

		log.debug("Write line: {}", sb);
		write(sb);
	}

	public void writeLine(@Nullable byte[] nextLine) throws IOException, SignatureException {

		if (nextLine == null) {
			return;
		}

		log.debug("Write line: {}", nextLine);

		write(nextLine);
		write(lineEnd);
	}

	public void writeCharToLine(char ch, int count) throws IOException, SignatureException {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(ch);
		}
		if (quotechar != NO_QUOTE_CHARACTER) {
			sb.append(quotechar);
		}

		sb.append(lineEnd);
		log.debug("Write line: {}", sb);
		write(sb);
	}

	private void appendCell(@NotNull StringBuffer sb, @NotNull String nextLine) {

		if (quotechar != NO_QUOTE_CHARACTER) {
			sb.append(quotechar);
		}

		for (int i = 0; i < nextLine.length(); i++) {

			char nextChar = nextLine.charAt(i);
			if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
				sb.append(escapechar).append(nextChar);
			} else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
				sb.append(escapechar).append(nextChar);
			} else {
				sb.append(nextChar);
			}
		}

		if (quotechar != NO_QUOTE_CHARACTER) {
			sb.append(quotechar);
		}
	}

	public byte[] getSign() throws SignatureException {
		return signature != null ? signature.sign() : null;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public long getFileSize() throws FlexPayException {
		return size;
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
