package org.flexpay.payments.export.util;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.payments.process.export.util.RegistryWriter;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NonNls;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestRegistryWriter {
    @NonNls
    private static final String dir = "org/flexpay/payments/export/util";
    @NonNls
    private static final String fileName = "testFile.dat";
    @NonNls
    private static final String FILE_ENCODING = "cp866";

    @Before
    public void startUp() throws IOException, URISyntaxException {
        File file = getTestFile();
        if (file.exists()) {
            assertTrue(file.delete());
        }
        assertTrue(file.createNewFile());
    }

    @After
    public void tearDown() throws URISyntaxException {
        File file = getTestFile();
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testWriteLine1() throws IOException, FlexPayException, URISyntaxException {
        File file = getTestFile();
        RegistryWriter rw = new RegistryWriter(file);
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
    public void testWriteLine2() throws IOException, FlexPayException, URISyntaxException {
        @NonNls final char separator = ',';
        File file = getTestFile();
        RegistryWriter rw = new RegistryWriter(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
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
    public void testWriteLine3() throws IOException, FlexPayException, URISyntaxException {
        @NonNls final char separator = ',';
        File file = getTestFile();
        RegistryWriter rw = new RegistryWriter(file);
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
    public void testWriteLine4() throws IOException, FlexPayException, URISyntaxException {
        @NonNls final char separator = ',';
        File file = getTestFile();
        RegistryWriter rw = new RegistryWriter(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
        @NonNls final String line1 = "line1";
        @NonNls final String line2 = "line2";
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
    public void testWriteLine5() throws IOException, FlexPayException, URISyntaxException {
        @NonNls final char separator = ',';
        File file = getTestFile();
        RegistryWriter rw = new RegistryWriter(file, separator, RegistryWriter.NO_QUOTE_CHARACTER);
        @NonNls final char ch1 = '_';
        @NonNls final char ch2 = '+';
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

    private static void assertCountLine(File file, int n) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODING), 500);
            int i = 0;
            while (reader.readLine() != null) {
                i++;
            }
            assertEquals(n, i);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    static private void assertArrayStringEquals(String[] str1, String[] str2) {
        assertEquals(str1.length, str2.length);
        for (int i=0; i<str1.length; i++) {
            assertEquals(str1[i], str2[i]);
        }
    }

    private static String[] getLineCeils(File file, int lineN, char separator) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODING), 500);
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

    private File getTestFile() throws URISyntaxException {
        URL dirUrl = getClass().getClassLoader().getResource(dir);
        assertNotNull("Test dir does not exist", dirUrl);
        return new File(new File(dirUrl.toURI()), fileName);
    }
}
