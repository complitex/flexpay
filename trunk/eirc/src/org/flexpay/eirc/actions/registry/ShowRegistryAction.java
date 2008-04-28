package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.SpRegistryRecordService;

public class ShowRegistryAction extends FPActionSupport {

	private SpRegistryService registryService;
	private SpRegistryRecordService registryRecordService;

	private SpRegistry registry = new SpRegistry();
	private Page<SpRegistryRecord> pager;

	public String execute() throws Exception {
		registry = registryService.read(registry.getId());
//		registryRecordService.listRecords(registry, pager);

		return super.execute();
	}
}
