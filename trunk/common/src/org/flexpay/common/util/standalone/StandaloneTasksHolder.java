package org.flexpay.common.util.standalone;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

import static org.flexpay.common.service.Roles.*;

/**
 * Holder for a list of task to run in a standalone application
 */
public class StandaloneTasksHolder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static StandaloneTasksHolder instance = new StandaloneTasksHolder();

	private static List<Scheduler> schedulers = CollectionUtils.list();

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
		List<GrantedAuthority> authorities = SecurityUtil.auths(
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
	public void setScheduler(Scheduler scheduler) {
		schedulers.add(scheduler);
	}

	public void stopSchedulers() throws Exception {
		for (Scheduler scheduler : schedulers) {
			if (scheduler != null) {
				scheduler.shutdown();
			}
		}
	}
}
