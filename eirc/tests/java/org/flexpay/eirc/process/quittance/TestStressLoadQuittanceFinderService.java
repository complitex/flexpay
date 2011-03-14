package org.flexpay.eirc.process.quittance;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.eirc.action.quittance.QuittancePayAction;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

public class TestStressLoadQuittanceFinderService extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittancePayAction quittancePayAction;

	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	@Test
	public void testMeasureLoad() throws Exception {

		File file = new File(System.getProperty("java.io.tmpdir"), "QuittanceNumbers.txt");
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
		try {
			String number;
			StopWatch watch = new StopWatch();
			watch.start();
			while ((number = reader.readLine()) != null) {
				quittancePayAction.setQuittance(new Quittance());
				quittancePayAction.setQuittanceNumber(number);
				quittancePayAction.execute();
			}
			watch.stop();

			System.out.println("Time spent for " + reader.getLineNumber() + " quittances lookup: " + watch);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
		}
	}
}
