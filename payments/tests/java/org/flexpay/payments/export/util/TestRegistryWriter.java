package org.flexpay.payments.export.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.io.ReaderCallback;
import org.flexpay.payments.service.registry.RegistryWriter;
import org.flexpay.payments.service.registry.impl.RegistryWriterImpl;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SignatureException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestRegistryWriter extends PaymentsSpringBeanAwareTestCase {

	private static final String fileName = "org/flexpay/payments/export/util/testFile.dat";
	private static final String FILE_ENCODING = "cp866";

	@Autowired
	private FPFileService fileService;

	private FPFile file;

	@Before
	public void startUp() throws IOException {
		file = createTestFile();
	}

	@After
	public void tearDown() {
		fileService.delete(file);
	}

	@Test
	public void testBase64() {
		String s = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./\n~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?\n";
		String encode = new String(Base64.encodeBase64(s.getBytes()));
		String decode = new String(Base64.decodeBase64(encode.getBytes()));
		assertEquals("Invalid decode", s, decode);
	}

	@Test
	public void testWriteLine1() throws IOException, FlexPayException, SignatureException {
		RegistryWriter rw = new RegistryWriterImpl(file);
		try {
			rw.writeLine("line1");
			rw.writeLine("line2");
			rw.writeLine("line3");
		} finally {
			rw.close();
		}
		assertCountLine(file, 3);
	}

	@Test
	public void testWriteLine2() throws IOException, FlexPayException, SignatureException {
		final char separator = ',';
		RegistryWriter rw = new RegistryWriterImpl(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
		String[] line1 = new String[]{"ceil11", "ceil12"};
		String[] line2 = new String[]{"ceil21", "ceil22", "ceil23"};
		try {
			rw.writeLine(line1);
			rw.writeLine(line2);
		} finally {
			rw.close();
		}
		assertCountLine(file, 2);

		String[] ceils = getLineCeils(file, 1, separator);
		assertNotNull(ceils);
		assertArrayStringEquals(line1, ceils);

		ceils = getLineCeils(file, 2, separator);
		assertNotNull(ceils);
		assertArrayStringEquals(line2, ceils);
	}

	@Test
	public void testWriteLine3() throws IOException, FlexPayException, SignatureException {
		final char separator = ',';
		RegistryWriter rw = new RegistryWriterImpl(file);
		String[] line1 = new String[]{"ceil11", "ceil12"};
		String[] line2 = new String[]{"ceil21", "ceil22", "ceil23"};
		try {
			rw.writeLine(line1);
			rw.writeLine(line2);
		} finally {
			rw.close();
		}
		assertCountLine(file, 2);

		String[] ceils = getLineCeils(file, 1, separator);
		assertNotNull(ceils);
		line1 = new String[]{"\"ceil11\"", "\"ceil12\""};
		assertArrayStringEquals(line1, ceils);

		ceils = getLineCeils(file, 2, separator);
		assertNotNull(ceils);
		line2 = new String[]{"\"ceil21\"", "\"ceil22\"", "\"ceil23\""};
		assertArrayStringEquals(line2, ceils);
	}

	@Test
	public void testWriteLine4() throws IOException, FlexPayException, SignatureException {
		final char separator = ',';
		RegistryWriter rw = new RegistryWriterImpl(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
		final String line1 = "line1";
		final String line2 = "line2";
		try {
			rw.writeLine(line1.getBytes());
			rw.writeLine(line2.getBytes());
		} finally {
			rw.close();
		}
		assertCountLine(file, 2);

		String[] ceils = getLineCeils(file, 1, separator);
		assertNotNull(ceils);
		assertEquals(1, ceils.length);
		assertEquals(line1, ceils[0]);

		ceils = getLineCeils(file, 2, separator);
		assertNotNull(ceils);
		assertEquals(1, ceils.length);
		assertEquals(line2, ceils[0]);
	}

	@Test
	public void testWriteLine5() throws IOException, FlexPayException, SignatureException {
		final char separator = ',';
		RegistryWriter rw = new RegistryWriterImpl(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
		final char ch1 = '_';
		final char ch2 = '+';
		try {
			rw.writeCharToLine(ch1, 10);
			rw.writeCharToLine(ch2, 5);
		} finally {
			rw.close();
		}
		assertCountLine(file, 2);

		String[] ceils = getLineCeils(file, 1, separator);
		assertNotNull(ceils);
		assertEquals(1, ceils.length);
		assertEquals("__________", ceils[0]);

		ceils = getLineCeils(file, 2, separator);
		assertNotNull(ceils);
		assertEquals(1, ceils.length);
		assertEquals("+++++", ceils[0]);
	}

	private static void assertCountLine(FPFile file, final int n) throws IOException {
		file.withReader(FILE_ENCODING, new ReaderCallback() {
			public void read(Reader r) throws IOException {
				@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
				BufferedReader reader = new BufferedReader(r);
				int i = 0;
				while (reader.readLine() != null) {
					i++;
				}
				assertEquals(n, i);
			}
		});
	}

	static private void assertArrayStringEquals(String[] str1, String[] str2) {
		assertEquals(str1.length, str2.length);
		for (int i = 0; i < str1.length; i++) {
			assertEquals(str1[i], str2[i]);
		}
	}

	private static String[] getLineCeils(FPFile file, int lineN, char separator) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream(), FILE_ENCODING));
			int i = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				if (++i == lineN) {
					return line.split(String.valueOf(separator));
				}
			}
			return null;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private FPFile createTestFile() throws IOException {

		FPFile file = new FPFile();
		file.setOriginalName(StringUtil.getFileName(fileName));
		file.setModule(fileService.getModuleByName("payments"));
		FPFileUtil.createEmptyFile(file);
		return file;
	}
}
