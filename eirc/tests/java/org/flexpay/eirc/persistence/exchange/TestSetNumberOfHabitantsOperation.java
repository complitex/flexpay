package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.service.EircRegistryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestSetNumberOfHabitantsOperation extends SpringBeanAwareTestCase {

	@Autowired
	private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryFileService registryFileService;
	@Autowired
	private ServiceOperationsFactory factory;

	@Test
	public void testProcess() throws FlexPayException {
		Registry registry = eircRegistryService.read(new Stub<Registry>(13L));
		Page<RegistryRecord> pager = new Page<RegistryRecord>();
		pager.setTotalElements(1);
		List<RegistryRecord> records = registryFileService.getRecordsForProcessing(stub(registry), pager, new Long[] {null, null});
		for (RegistryRecord record : records) {
			List<String> containers = new ArrayList<String>();
			for (RegistryRecordContainer c : record.getContainers()) {
				List<String> data = StringUtil.splitEscapable(c.getData(), Operation.CONTAINER_DATA_DELIMITER, Operation.ESCAPE_SYMBOL);
				if (data.get(0).equals("4")) {
					containers = data;
					break;
				}
			}
			SetNumberOfHabitantsOperation op = new SetNumberOfHabitantsOperation(factory, containers);
			op.process(registry, record);
		}
	}

}
