package org.flexpay.common.util.standalone;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationRunner {

	public static void main(String[] argv) throws Exception {

		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
				"WEB-INF/applicationContext.xml");


		StandaloneTasksHolder holder = StandaloneTasksHolder.getInstance();
		holder.stopSchedulers();
		holder.executeTasks();

		context.close();
	}
}
