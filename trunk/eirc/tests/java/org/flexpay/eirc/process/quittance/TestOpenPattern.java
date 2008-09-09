package org.flexpay.eirc.process.quittance;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.io.IOUtils;
import org.flexpay.eirc.util.config.ApplicationConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestOpenPattern {

	@Test
	public void testOpenPdfPatterns() throws Exception {

		InputStream ticketPattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/ticketPattern.pdf");

		try {
			assertNotNull("ticketPattern not found", ticketPattern);

//		InputStream titlePattern = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/titlePattern.pdf");
//		assertNotNull("titlePattern not found", titlePattern);

			OutputStream os = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(new PdfReader(ticketPattern), os);

			assertNotNull("Stamper is null", stamper);

		} finally {
			IOUtils.closeQuietly(ticketPattern);
//			IOUtils.closeQuietly(titlePattern);
		}
	}
}
