package org.flexpay.payments.service.registry;

import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.security.Signature;
import java.security.SignatureException;

public interface RegistryWriter {

	char NO_QUOTE_CHARACTER = '\u0000';
	char NO_ESCAPE_CHARACTER = '\u0000';
	String DEFAULT_LINE_END = "\n";

	void writeLine(@Nullable String[] nextLine) throws IOException, SignatureException;

	void write(@NotNull byte[] bytes, int off, int len) throws SignatureException, IOException;

	void write(@NotNull byte[] bytes) throws SignatureException, IOException;

	void write(CharSequence cs) throws IOException, SignatureException;

	void writeLine(@Nullable String nextLine) throws IOException, SignatureException;

	void writeLine(@Nullable byte[] nextLine) throws IOException, SignatureException;

	void writeCharToLine(char ch, int count) throws IOException, SignatureException;

	byte[] getSign() throws SignatureException;

	void setSignature(Signature signature);

	long getFileSize() throws FlexPayException;

	void close() throws FlexPayException;

	String getFileEncoding();
}
