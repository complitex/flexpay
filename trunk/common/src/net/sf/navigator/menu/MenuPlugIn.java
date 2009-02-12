package net.sf.navigator.menu;

import net.sf.navigator.util.LoadableResourceException;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Struts plug-in adapter for the menuing module.
 */
public class MenuPlugIn implements PlugIn {

	private Logger log = LoggerFactory.getLogger(getClass());

    private MenuRepository repository;
    private String menuConfig = "/WEB-INF/menu-config.xml";
    private HttpServlet servlet;

    public String getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(String menuConfig) {
        this.menuConfig = menuConfig;
    }

    public void init(ActionServlet servlet, ModuleConfig config)
    throws ServletException {
		log.debug("Starting struts-menu initialization");

        this.servlet = servlet;
        repository = new MenuRepository();
        repository.setLoadParam(menuConfig);
        repository.setServletContext(servlet.getServletContext());

        try {
            repository.load();
            servlet.getServletContext().setAttribute(MenuRepository.MENU_REPOSITORY_KEY, repository);

			log.debug("struts-menu initialization successful");
        } catch (LoadableResourceException lre) {
            throw new ServletException("Failure initializing struts-menu: " +
                lre.getMessage());
        }
    }

    public void destroy() {
        repository = null;
        servlet.getServletContext().removeAttribute(MenuRepository.MENU_REPOSITORY_KEY);
        menuConfig = null;
        servlet = null;
    }

}
