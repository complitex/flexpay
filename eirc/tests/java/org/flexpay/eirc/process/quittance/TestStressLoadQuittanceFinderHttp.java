package org.flexpay.eirc.process.quittance;

import org.junit.Test;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.time.StopWatch;

import java.net.URL;
import java.net.URLConnection;
import java.io.*;

public class TestStressLoadQuittanceFinderHttp {

	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	@Test
	public void testMeasureLoad() throws Exception {

		File file = new File(System.getProperty("java.io.tmpdir"), "QuittanceNumbers.txt");
		URL baseUrl = new URL("http://localhost:8080/eirc");
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
		try {
			String number;
			StopWatch watch = new StopWatch();
			watch.start();
			while ((number = reader.readLine()) != null) {
				URL url = new URL(baseUrl, "quittancePay.action?quittanceNumber=" + number);
				URLConnection connection = url.openConnection();
				InputStream cis = null;
				try {
					cis = connection.getInputStream();
					IOUtils.copy(cis, NullOutputStream.NULL_OUTPUT_STREAM);
				} finally {
					IOUtils.closeQuietly(cis);
				}
			}
			watch.stop();

			System.out.println("Time spent for " + reader.getLineNumber() + " quittances lookup: " + watch);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
		}
	}
}
