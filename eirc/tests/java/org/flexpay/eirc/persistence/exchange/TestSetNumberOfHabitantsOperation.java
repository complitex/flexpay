package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.service.exchange.ServiceProviderFileProcessor;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.payments.service.EircRegistryService;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestSetNumberOfHabitantsOperation extends EircSpringBeanAwareTestCase {

	@Autowired
	private ServiceProviderFileProcessor serviceProviderFileProcessor;
	@Autowired
	private EircRegistryService eircRegistryService;
	@Autowired
	private RegistryService registryService;
	@Autowired
	private RegistryFileService registryFileService;
	@Autowired
	private ServiceOperationsFactory factory;

	@Test
	@Ignore
	@Deprecated
	public void testProcess() throws FlexPayException, FlexPayExceptionContainer {
		Registry registry = registryService.read(new Stub<Registry>(13L));
		assertNotNull("Registry not found", registry);
		List<RegistryRecord> records = registryFileService.getRecordsForProcessing(stub(registry), new FetchRange());
		for (RegistryRecord record : records) {
			List<String> containers = list();

			for (RegistryRecordContainer c : record.getContainers()) {
				if (c != null) {
					List<String> data = StringUtil.splitEscapable(c.getData(),
							Operation.CONTAINER_DATA_DELIMITER, Operation.ESCAPE_SYMBOL);
					if ("4".equals(data.get(0))) {
						containers = data;
						break;
					}
				}
			}
			SetNumberOfHabitantsOperation op = new SetNumberOfHabitantsOperation(factory, containers);
			DelayedUpdate update = op.process(null);
			update.doUpdate();
		}
	}
}
