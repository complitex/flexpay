package org.flexpay.eirc.sp;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestMbRegistryFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier ("mbRegistryFileParser")
	private FileParser parser;
	@Autowired
	private EircRegistryService eircRegistryService;

	@Test
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/01033_122008.nac");

		List<Registry> registries = parser.parse(newFile);
		for (Registry registry : registries) {
			eircRegistryService.deleteRecords(stub(registry));
			eircRegistryService.delete(registry);
		}

		deleteFile(newFile);
	}

}
