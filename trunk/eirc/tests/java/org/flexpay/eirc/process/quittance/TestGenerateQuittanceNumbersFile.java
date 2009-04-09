package org.flexpay.eirc.process.quittance;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.apache.commons.io.IOUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;
import java.io.*;

public class TestGenerateQuittanceNumbersFile extends SpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;
	@Autowired
	private QuittanceNumberService numberService;

	private static final Stub<ServiceOrganization> ORGANIZATION_STUB = new Stub<ServiceOrganization>(2L);
	private static final Date dateFrom = new GregorianCalendar(2008, Calendar.JULY, 1).getTime();
	private static final Date dateTill = new GregorianCalendar(2008, Calendar.JULY, 31).getTime();

	@Test
	public void testGenerateQuittanceNumbersFile() throws Exception {

		File tmpFile = new File(System.getProperty("java.io.tmpdir"), "QuittanceNumbers.txt");
		OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile));
		List<Quittance> quittances = quittanceService.getQuittances(ORGANIZATION_STUB, dateFrom, dateTill);

		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
		try {
			for (Quittance quittance : quittances) {
				writer.println(numberService.getNumber(quittance));
			}
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(os);
		}
	}
}
