package org.flexpay.eirc.action;

import org.apache.commons.io.IOUtils;
import static org.flexpay.common.util.config.ApplicationConfig.getResourceAsStream;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.InputStream;

public class TestPrintTicketAction extends EircSpringBeanAwareTestCase {

	@Test
	public void testGetQuittanceTemplates() {
		InputStream s1 = null;
		InputStream s2 = null;
		try {
			s1 = getResourceAsStream("/resources/eirc/pdf/ticketPattern.pdf");
			s2 = getResourceAsStream("/resources/eirc/pdf/titlePattern.pdf");

			assertNotNull("TicketPattern not found", s1);
			assertNotNull("TitlePattern not found", s2);
		} finally {
			IOUtils.closeQuietly(s1);
			IOUtils.closeQuietly(s2);
		}
	}
}
