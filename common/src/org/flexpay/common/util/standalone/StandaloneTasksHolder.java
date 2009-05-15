package org.flexpay.common.util.standalone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;
import org.flexpay.common.util.SecurityUtil;
import static org.flexpay.common.service.Roles.BASIC;
import static org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW;
import static org.flexpay.common.service.Roles.PROCESS_READ;

import java.util.Collections;
import java.util.List;

/**
 * Holder for a list of task to run in a standalone application
 */
public class StandaloneTasksHolder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static StandaloneTasksHolder instance = new StandaloneTasksHolder();

	private StandaloneTasksHolder() {

	}

	private List<StandaloneTask> standaloneTasks = Collections.emptyList();

	/**
	 * Execute tasks
	 */
	public void executeTasks() {

		log.debug("About to execute {} tasks", standaloneTasks.size());

		authenticateStandaloneUser();

		for (StandaloneTask task : standaloneTasks) {
			task.execute();
		}
	}

	public static void authenticateStandaloneUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				BASIC,
				PROCESS_DEFINITION_UPLOAD_NEW,
				PROCESS_READ
		);
		User user = new User("standalone", "standalone", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public static StandaloneTasksHolder getInstance() {
		return instance;
	}

	public void setStandaloneTasks(List<StandaloneTask> standaloneTasks) {
		this.standaloneTasks = standaloneTasks;
	}

	@Required
	public void setScheduler(Scheduler scheduler) throws Exception {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}
}
