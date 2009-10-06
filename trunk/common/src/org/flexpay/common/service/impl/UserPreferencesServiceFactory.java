package org.flexpay.common.service.impl;

public class UserPreferencesServiceFactory {

	private UserPreferencesServiceImpl service = new UserPreferencesServiceImpl();

	public UserPreferencesServiceImpl getInstance() {
		return service;
	}
}
