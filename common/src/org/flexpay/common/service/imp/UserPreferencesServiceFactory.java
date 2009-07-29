package org.flexpay.common.service.imp;

public class UserPreferencesServiceFactory {

	private UserPreferencesServiceImpl service = new UserPreferencesServiceImpl();

	public UserPreferencesServiceImpl getInstance() {
		return service;
	}
}
