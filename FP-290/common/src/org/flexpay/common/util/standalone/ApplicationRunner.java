package org.flexpay.common.util.standalone;

import org.jetbrains.annotations.NonNls;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationRunner {

	public static void main(String[] argv) {

		@NonNls ApplicationContext context = new FileSystemXmlApplicationContext(
				"WEB-INF/applicationContext.xml");


		StandaloneTasksHolder holder = StandaloneTasksHolder.getInstance();
		holder.executeTasks();
	}
}
