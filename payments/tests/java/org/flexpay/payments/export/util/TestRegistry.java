package org.flexpay.payments.export.util;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class TestRegistry extends PaymentsSpringBeanAwareTestCase {
    @Autowired
	private FPFileService fpFileService;
    @Autowired
    @Qualifier("registryFPFileTypeService")
    private RegistryFPFileTypeService registryFPFileTypeService;

    @Autowired
	private RegistryService registryService;
    @Autowired
    @Qualifier("registryStatusService")
	private RegistryStatusService registryStatusService;
	@Autowired
	private RegistryTypeService registryTypeService;
    @Autowired
	@Qualifier ("registryArchiveStatusService")
	private RegistryArchiveStatusService registryArchiveStatusService;
    @Autowired
    private RegistryFileService registryFileService;

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Test
    public void testRegistryFPFileType() throws FlexPayException {
        RegistryFPFileType fptype = registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT);
        assertNotNull("FP file not found", fptype);

        Registry registry = new Registry();

        registry.setRecipientCode((long) 1);
		registry.setSenderCode((long) 2);
		registry.setCreationDate(new Date());
		registry.setRegistryType(registryTypeService.findByCode(RegistryType.TYPE_CASH_PAYMENTS));
		registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
		registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
		registry.setFromDate(new Date(0));
		registry.setTillDate(new Date());
        registry.setProperties(propertiesFactory.newRegistryProperties());

        registry = registryService.create(registry);
        assertNotNull("Create nullable registry", registry);

        FPFile file = new FPFile();
        file.setModule(fpFileService.getModuleByName("payments"));
        file.setNameOnServer("testFile");
        file.setOriginalName("testFile");
        file.setUserName("testUser");
        file.setCreationDate(new Date());

        file = fpFileService.create(file);
        assertNotNull("Creating nullable file", file);

        registry.getFiles().put(fptype, file);
        registryService.update(registry);

        registry = registryService.read(Stub.stub(registry));
        assertNotNull("Load nullable object registry", registry);
        assertNotNull("Nullable registry`s files", registry.getFiles());
        assertEquals("Must be not empty map files", 1, registry.getFiles().size());
        file = registry.getFiles().get(fptype);
        assertNotNull("Null file in map", file);
        file = fpFileService.read(Stub.stub(file));
        assertNotNull("Missing file in map", file);
        System.out.println("File is " + file);

        List<Registry> registries = registryFileService.getRegistries(file);
        assertNotNull("registryFileService.getRegistries(file) return null", registries);
        assertNotSame("registryFileService.getRegistries(file) return list with '0' size", 0, registries.size());

        Page<Registry> page = new Page<Registry>();
        registries = registryService.findObjects(page, file.getId());
        assertNotNull("registryService.findObjects(page, file) return null", registries);
        assertNotSame("registryService.findObjects(page, file) return list with '0' size", 0, registries.size());
    }
}
