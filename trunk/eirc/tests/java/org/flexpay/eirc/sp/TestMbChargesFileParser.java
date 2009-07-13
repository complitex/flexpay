package org.flexpay.eirc.sp;

import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestMbChargesFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier ("mbChargesFileParser")
	private FileParser parser;
	@Autowired
	private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryProcessor registryProcessor;
    @Autowired
	private RegistryService registryService;

	@Test
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.nch");

		List<Registry> registries = parser.parse(newFile);
		registryProcessor.registriesProcess(registries);

		for (Registry registry : registries) {
			registryService.deleteRecords(stub(registry));
			registryService.delete(registry);
		}

		deleteFile(newFile);
	}
}
