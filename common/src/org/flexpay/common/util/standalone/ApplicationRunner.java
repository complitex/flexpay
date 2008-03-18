package org.flexpay.common.util.standalone;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Collection;

public class ApplicationRunner {

	public static void main(String[] argv) {

		ApplicationContext context = new FileSystemXmlApplicationContext(
				"WEB-INF/applicationContext.xml");

		Collection tasksHolders = context
				.getBeansOfType(StandaloneTasksHolder.class).values();
		for (Object obj : tasksHolders) {
			StandaloneTasksHolder holder = (StandaloneTasksHolder) obj;
			holder.executeTasks();
		}
	}
}
