package org.flexpay.eirc.process.quittance;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestStressLoadQuittanceFinderHttp {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	@Test
	public void testMeasureLoad() throws Exception {

		File file = new File(System.getProperty("java.io.tmpdir"), "QuittanceNumbers.txt");
		URL baseUrl = new URL("http://localhost:8080");
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
		try {
			String number;
			StopWatch watch = new StopWatch();
			watch.start();

			// prepare nasic auth
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			String userpassword = "vld:vld";
			String encodedAuthorization = enc.encode(userpassword.getBytes());

			while ((number = reader.readLine()) != null) {
				URL url = new URL(baseUrl, "/eirc/eirc/quittancePay.action?quittanceNumber=" + number);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
				InputStream cis = null;
				try {
					cis = connection.getInputStream();
					log.debug("{}, response: {} b", url, connection.getContentLength());
//					IOUtils.copy(c9is, System.out);
					IOUtils.copy(cis, NullOutputStream.NULL_OUTPUT_STREAM);
				} finally {
					IOUtils.closeQuietly(cis);
				}

//				if (reader.getLineNumber() == 1) {
//					break;
//				}
				if (reader.getLineNumber() == 100) {
					break;
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
