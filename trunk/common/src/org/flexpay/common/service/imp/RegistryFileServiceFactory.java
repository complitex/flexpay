package org.flexpay.common.service.imp;

public class RegistryFileServiceFactory {

	private final RegistryFileServiceImpl impl = new RegistryFileServiceImpl();

	public RegistryFileServiceImpl getRegistryFileService() {
		return impl;
	}
}
