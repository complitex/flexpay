package net.sf.navigator.menu;

import net.sf.navigator.util.LoadableResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import javax.servlet.ServletException;

/**
 * This loader is available for those that use the Spring Framework.  To 
 * use it, simply configure it as follows in your applicationContext.xml file.
 * </p>
 * <pre>
 * &lt;bean id="menu" class="net.sf.navigator.menu.MenuLoader"&gt;
 *  &lt;property name="menuConfig"&gt;
 *      &lt;value&gt;/WEB-INF/menu-config.xml&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>The menuConfig property is an optional attribute.  It is set to 
 * /WEB-INF/menu-config.xml by default.</p>
 * 
 * @author Matt Raible
 */
public class MenuLoader extends WebApplicationObjectSupport {

	private Logger log = LoggerFactory.getLogger(getClass());

    /** Configuration file for menus */
    private String menuConfig = "/WEB-INF/menu-config.xml";

    /**
     * Set the Menu configuration file
     * 
     * @param menuConfig the file containing the Menus/Items
     */
    public void setMenuConfig(String menuConfig) {
        this.menuConfig = menuConfig;
    }

    /**
     * Initialization of the Menu Repository
     *
     * @throws org.springframework.context.ApplicationContextException if an error occurs
     */
    protected void initApplicationContext() throws ApplicationContextException {
        try {
			log.debug("Starting struts-menu initialization");

            MenuRepository repository = new MenuRepository();
            repository.setLoadParam(menuConfig);
            repository.setServletContext(getServletContext());

            try {
                repository.load();
                getServletContext().setAttribute(MenuRepository.MENU_REPOSITORY_KEY, repository);
				log.debug("struts-menu initialization successful");
            } catch (LoadableResourceException lre) {
                throw new ServletException("Failure initializing struts-menu: " + lre.getMessage());
            }
        } catch (Exception ex) {
            throw new ApplicationContextException("Failed to initialize Struts Menu repository",ex);
        }
    }

}
