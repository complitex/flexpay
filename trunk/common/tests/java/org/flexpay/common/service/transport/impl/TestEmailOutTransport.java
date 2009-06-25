package org.flexpay.common.service.transport.impl;

import org.junit.Test;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.io.WriterCallback;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Writer;
import java.io.IOException;

public class TestEmailOutTransport extends SpringBeanAwareTestCase {

	@Autowired
	private EmailOutTransport transport;
	@Autowired
	private FPFileService fileService;

	@Test
	public void testSend() throws Exception {

		transport.setEmail("mfedko" + "@" + "gmail.com");

		FPFile fpFile = new FPFile();
		fpFile.setOriginalName("EmailOutTransportTestData.txt");
		fpFile.setModule(fileService.getModuleByName("common"));
		FPFileUtil.createEmptyFile(fpFile);
		fpFile.withWriter("cp1251", new WriterCallback() {
			public void write(Writer w) throws IOException {
				w.write("Hello from TestEmailOutTransport");
			}
		});
		transport.send(fpFile);
	}
}
