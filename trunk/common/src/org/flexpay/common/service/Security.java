package org.flexpay.common.service;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;

import java.util.List;

/**
 * Helper class that defines fictive process users and their roles
 */
public abstract class Security {

	/**
	 * History sync process user
	 */
	public static String USER_HISTORY_SYNCER = "history-syncer";

	/**
	 * Set of authorities names for syncer process user
	 */
	public static List<String> USER_HISTORY_SYNCER_AUTHORITIES = CollectionUtils.list(
			Roles.BASIC
	);

	/**
	 * Do syncer user authentication
	 */
	public static void authenticateSyncer() {
		SecurityUtil.authenticate(USER_HISTORY_SYNCER, USER_HISTORY_SYNCER_AUTHORITIES);
	}
}
