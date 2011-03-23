package org.flexpay.eirc.sp.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class ParseContext {

	private Logger plog;
	private Registry registry;
	private List<RegistryRecord> records = CollectionUtils.list();
	private List<RegistryRecord> lastAccountRecords = CollectionUtils.list();

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public List<RegistryRecord> getRecords() {
		return records;
	}

	public List<RegistryRecord> getLastAccountRecords() {
		return lastAccountRecords;
	}

	public void flushLastAccountRecords() {
		records.addAll(lastAccountRecords);
		lastAccountRecords.clear();
	}

	public void addLastRecord(RegistryRecord record) {
		if (sameLastAccount(record)) {
			lastAccountRecords.add(record);
		} else {
			records.addAll(lastAccountRecords);
			lastAccountRecords.clear();
			lastAccountRecords.add(record);
		}
	}

	private boolean sameLastAccount(RegistryRecord record) {
		return !lastAccountRecords.isEmpty() &&
			   record.getPersonalAccountExt().equals(lastAccountRecords.get(0).getPersonalAccountExt());
	}

	public Stub<ServiceProvider> getServiceProviderStub() {
		EircRegistryProperties properties = (EircRegistryProperties) registry.getProperties();
		return properties.getServiceProviderStub();
	}

	@Nullable
	public RegistryRecord findLastRecordByService(String accountNumber, Service service) {
		for (RegistryRecord record : lastAccountRecords) {
			if (accountNumber.equals(record.getPersonalAccountExt()) && service.equals(getRecordService(record))) {
				return record;
			}
		}

		return null;
	}

	private Service getRecordService(RegistryRecord record) {
		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		return props.getService();
	}

	public Logger getPlog() {
		return plog;
	}

	public void setPlog(Logger plog) {
		this.plog = plog;
	}
}
