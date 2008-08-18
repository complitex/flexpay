package org.flexpay.eirc.dao;

import org.jetbrains.annotations.NotNull;

public interface RegistryFileDaoExt {

	/**
	 * Check if registry file is loaded to database
	 *
	 * @param fileId Registry file id
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	boolean isLoaded(@NotNull Long fileId);
}