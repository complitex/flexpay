package org.flexpay.common.service;

import org.flexpay.common.util.SecurityUtil;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Helper class that defines fictive process users and their roles
 */
public abstract class Security {

	/**
	 * touch me to ensure static fields are properly initialised
	 */
	public static void touch() {
		// do nothing
	}

    /**
     * Set of authorities names for syncer process user
     */
    protected static final List<String> USER_MULE_AUTHORITIES = list(
            Roles.BASIC
    );

    private static final String USER_MULER = "muler";

	/**
	 * History sync process user
	 */
	private static final String USER_HISTORY_SYNCER = "history-syncer";

	/**
	 * History sync process user
	 */
	private static final String USER_HISTORY_BROADCASTER = "history-broadcaster";

	/**
	 * Set of authorities names for syncer process user
	 */
	protected static final List<String> USER_HISTORY_SYNCER_AUTHORITIES = list(
			Roles.BASIC
	);

	/**
	 * Set of authorities names for syncer process user
	 */
	protected static final List<String> USER_HISTORY_BROADCASTER_AUTHORITIES = list(
			Roles.BASIC
	);

	/**
	 * Do syncer user authentication
	 */
	public static void authenticateSyncer() {
		SecurityUtil.authenticate(USER_HISTORY_SYNCER, USER_HISTORY_SYNCER_AUTHORITIES);
	}

	/**
	 * Do history broadcast user authentication
	 */
	public static void authenticateHistoryPacker() {
		SecurityUtil.authenticate(USER_HISTORY_BROADCASTER, USER_HISTORY_BROADCASTER_AUTHORITIES);
	}

    public static void authenticateMuler() {
        SecurityUtil.authenticate(USER_MULER, USER_MULE_AUTHORITIES);
    }

}
