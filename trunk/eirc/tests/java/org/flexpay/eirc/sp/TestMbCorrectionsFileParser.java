package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.service.RegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class TestMbCorrectionsFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier("mbCorrectionsFileParser")
	private FileParser parser;
	@Autowired
	private RegistryService registryService;

	@Test
	@Transactional
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/01033_122008.kor");

		try {
			Registry registry = (Registry) parser.parse(newFile);
//			registryService.deleteRecords(new Stub<Registry>(registry));
//			registryService.delete(registry);
		} catch (Exception e) {
			log.error("Error with parsing file", e);
		}

		deleteFile(newFile);
	}

}
