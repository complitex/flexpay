package net.sourceforge.navigator.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MenuContextListener implements ServletContextListener {

	private Logger log = LoggerFactory.getLogger(getClass());

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        MenuRepository repository = (MenuRepository) applicationContext.getBean(MenuRepository.MENU_REPOSITORY_KEY);
        repository.setApplicationContext(applicationContext);
        repository.setIndex(repository.getMenu());
        sc.setAttribute(MenuRepository.MENU_REPOSITORY_KEY, repository);
        log.debug("Struts-menu initialization successfull");
    }

    public void contextDestroyed(ServletContextEvent sce) {
		log.debug("destroying struts-menu...");
        sce.getServletContext().removeAttribute(MenuRepository.MENU_REPOSITORY_KEY);
    }

}
