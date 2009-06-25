package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.DataSourceDescription;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.sp.impl.validation.MbCorrectionsFileValidator;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.service.EircRegistryService;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestMbCorrectionsFileParser extends TestSpFileCreateAction {

	@Autowired
	@Qualifier ("mbCorrectionsFileParser")
	private FileParser parser;
	@Autowired
	private EircRegistryService eircRegistryService;

	@Autowired
	private ClassToTypeRegistry typeRegistry;
	@Autowired
	private MbCorrectionsFileValidator validator;
	@Autowired
	@Qualifier ("megabankDataSourceDescription")
	private DataSourceDescription megabankSD;

	private boolean ignoreInvalidLinesNumber;

	@Test
	public void testParseFile() throws Throwable {

		FPFile newFile = createSpFile("org/flexpay/eirc/sp/01033_122008.kor");

		try {
			Registry registry = parser.parse(newFile);
			assertNotNull("Registry parse failed", registry);

			eircRegistryService.deleteRecords(stub(registry));
			eircRegistryService.delete(registry);
		} catch (Exception e) {
			log.error("Error with parsing file", e);
			throw e;
		}

		deleteFile(newFile);
	}

	@Test
	public void testClassToTypeRegistry() {
		System.out.println("ServiceProvider type id: " + typeRegistry.getType(ServiceProvider.class));
		System.out.println("Megabank SD: " + megabankSD);
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
