package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.eirc.util.config.ApplicationConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;

import java.io.InputStream;

public class TestPrintTicketAction extends EircSpringBeanAwareTestCase {

	@Test
	@NotTransactional
	public void testGetQuittanceTemplates() {
		InputStream s1 = null;
		InputStream s2 = null;
		try {
			s1 = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/ticketPattern.pdf");
			s2 = ApplicationConfig.getResourceAsStream("/resources/eirc/pdf/titlePattern.pdf");

			assertNotNull("TicketPattern not found", s1);
			assertNotNull("TitlePattern not found", s2);
		} finally {
			IOUtils.closeQuietly(s1);
			IOUtils.closeQuietly(s2);
		}
	}
}
