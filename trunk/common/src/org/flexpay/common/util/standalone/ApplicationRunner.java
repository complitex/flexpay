package org.flexpay.common.util.standalone;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Set;

public class ApplicationRunner {

	public static void main(String[] argv) {

		ApplicationContext context = new FileSystemXmlApplicationContext(
				"WEB-INF/appicationContext.xml");

		Set tasksHolders = context
				.getBeansOfType(StandaloneTasksHolder.class).entrySet();
		for (Object obj : tasksHolders) {
			StandaloneTasksHolder holder = (StandaloneTasksHolder) obj;
			holder.executeTasks();
		}
	}
}
