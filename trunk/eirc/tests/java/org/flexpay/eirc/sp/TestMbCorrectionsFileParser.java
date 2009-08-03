package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.sp.impl.validation.MbCorrectionsFileValidator;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestMbCorrectionsFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier ("mbCorrectionsFileParser")
	private FileParser parser;
	@Autowired
	private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryService registryService;

	@Autowired
	private ClassToTypeRegistry typeRegistry;
	@Autowired
	private MbCorrectionsFileValidator validator;
	@Autowired
	private RegistryProcessor registryProcessor;

	private boolean ignoreInvalidLinesNumber;

	@Test
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.ls");

		List<Registry> registries = parser.parse(newFile);
		assertNotNull("Registry parse failed", registries);

		registryProcessor.registriesProcess(registries);
	}

	@Before
	public void setupIgnoreInvalidLinesNumber() {
		ignoreInvalidLinesNumber = validator.isIgnoreInvalidLinesNumber();
		validator.setIgnoreInvalidLinesNumber(true);
	}

	@After
	public void restoreIgnoreInvalidLinesNumber() {
		validator.setIgnoreInvalidLinesNumber(ignoreInvalidLinesNumber);
	}

}
