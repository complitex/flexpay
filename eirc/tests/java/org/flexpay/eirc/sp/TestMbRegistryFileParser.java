package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

public class TestMbRegistryFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier("mbRegistryFileParser")
	private FileParser parser;
	@Autowired
	private EircRegistryService eircRegistryService;

	@Test
	@NotTransactional
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/01033_122008.nac");

		try {
			Registry registry = parser.parse(newFile);
			eircRegistryService.deleteRecords(new Stub<Registry>(registry.getId()));
			eircRegistryService.delete(registry);
		} catch (Exception e) {
			log.error("Error with parsing file", e);
		}

		deleteFile(newFile);
	}

}
