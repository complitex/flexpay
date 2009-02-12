package net.sf.navigator.taglib;

import net.sf.navigator.displayer.MenuDisplayer;
import net.sf.navigator.displayer.MenuDisplayerMapping;
import net.sf.navigator.displayer.MessageResourcesMenuDisplayer;
import net.sf.navigator.menu.MenuRepository;
import net.sf.navigator.menu.PermissionsAdapter;
import net.sf.navigator.menu.RolesPermissionsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is the main tag of Struts Menu and can be used in a JSP as follows:</p>
 * <pre>
 *  &lt;menu:useMenuDisplayer name="ListMenu"&gt;
 *     &lt;menu:displayMenu name="MyMenu"/&gt;
 *  &lt;/menu:useMenuDisplayer&gt;
 * </pre>
 */
public class UseMenuDisplayerTag extends TagSupport {

	protected Logger log = LoggerFactory.getLogger(getClass());
    public static final String PRIVATE_REPOSITORY = "net.sf.navigator.repositoryKey";
    public static final String DISPLAYER_KEY = "net.sf.navigator.taglib.DISPLAYER";
    public static final String ROLES_ADAPTER = "rolesAdapter";
    public static final String MENU_ID = "net.sf.navigator.MENU_ID";
    
    protected static ResourceBundle messages = ResourceBundle.getBundle("net.sf.navigator.taglib.LocalStrings");

    protected MenuDisplayer menuDisplayer;
    protected String localeKey;
    protected String name;
    protected String bundleKey;
    private String config = MenuDisplayer.DEFAULT_CONFIG;
    private String permissions;
    private String repository;
    protected ResourceBundle rb; // used to allow setting of ResourceBundle 
                                 // from JSTL in EL tag

    public String getBundle() {
        return bundleKey;
    }

    public void setBundle(String bundle) {
        this.bundleKey = bundle;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
		log.debug("setting config to: {}", config);
        this.config = config;
    }

    public String getLocale() {
        return localeKey;
    }

    public void setLocale(String locale) {
        this.localeKey = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepository() {
        return repository;
    }

    /**
     * This method allows users to override the key used to lookup the
     * repository.  If not specified - the default repository is used, which is
     * "net.sf.navigator.MENU_REPOSITORY" or
     * UseMenuDisplayerTag.MENU_REPOSITORY_KEY.
	 *
     * @param repository repository
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /** Getter for property permissions.
     * @return Value of property permissions.
     */
    public String getPermissions() {
        return this.permissions;
    }

    /**
	 * Setter for property permissions.
	 *
     * @param permissions New value of property permissions.
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int doStartTag() throws JspException {
        if (repository == null) {
            repository = MenuRepository.MENU_REPOSITORY_KEY;
        }

		log.debug("Looking for repository named '{}'", repository);

        // get the menu repository
        MenuRepository rep = (MenuRepository) pageContext.findAttribute(this.repository);

        if (rep == null) {
            throw new JspException(messages.getString("menurepository.not.found"));
        } else {
            // set repository as a pageContext variable so that DisplayMenuTag
            // can grab it.
			log.debug("stuffing repository into pageContext...");
            pageContext.setAttribute(PRIVATE_REPOSITORY, rep);
        }

        // get the displayer mapping
        MenuDisplayerMapping displayerMapping = rep.getMenuDisplayerMapping(this.name);

        if (displayerMapping == null) {
            throw new JspException(messages.getString("displayer.mapping.not.found"));
        }

        PermissionsAdapter permissionAdapter = getPermissionsAdapter();

        //get an instance of the menu displayer
        MenuDisplayer displayerInstance;

        try {
            displayerInstance = (MenuDisplayer) Class.forName(displayerMapping.getType()).newInstance();
            menuDisplayer = displayerInstance;
            // default to use the config on the mapping
            if (displayerMapping.getConfig() != null) {
                // this value (config) is set on the displayer below
                setConfig(displayerMapping.getConfig());
            }
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
        
        if (bundleKey == null) {
            this.bundleKey = "org.apache.struts.action.MESSAGE";
        }
        
        // setup the displayerInstance
        // if the displayer is a MessageResourceMenuDisplayer
        // and a bundle is specified, then pass it the bundle (message resources) and
        // the locale
        if (bundleKey != null && !"".equals(bundleKey) && displayerInstance instanceof MessageResourcesMenuDisplayer) {
            MessageResourcesMenuDisplayer mrDisplayerInstance = (MessageResourcesMenuDisplayer) displayerInstance;
            Locale locale;

            if (localeKey == null) {
                // default to Struts locale
                locale = (Locale) pageContext.findAttribute("org.apache.struts.action.LOCALE");
                if (locale == null) {
                    locale = pageContext.getRequest().getLocale();
                }
            } else {
                locale = (Locale) pageContext.findAttribute(localeKey);
            }
            mrDisplayerInstance.setLocale(locale);
            
            if (rb != null) {
                mrDisplayerInstance.setMessageResources(rb);
            } else {
                Object resources = pageContext.findAttribute(bundleKey);
                
                if (resources == null) {
                    // try a simple ResourceBundle
                    try {
                        rb = ResourceBundle.getBundle(bundleKey, locale);
                        mrDisplayerInstance.setMessageResources(rb);
                    } catch (MissingResourceException mre) {
                        log.error(mre.getMessage());
                    }
                } else {
                     mrDisplayerInstance.setMessageResources(resources);   
                }
            }
        }

        displayerInstance.setConfig(config);
        if (id != null) {
            pageContext.setAttribute("menuId", id);
        }

        displayerInstance.init(pageContext, displayerMapping);
        displayerInstance.setPermissionsAdapter(permissionAdapter);

        pageContext.setAttribute(DISPLAYER_KEY, displayerInstance);

        return EVAL_BODY_INCLUDE;
    }

    protected PermissionsAdapter getPermissionsAdapter() throws JspException {
        PermissionsAdapter adapter = null;

        if (permissions != null) {
            // If set to "rolesAdapter", then create automatically
            if (permissions.equalsIgnoreCase(ROLES_ADAPTER)) {
                adapter = new RolesPermissionsAdapter((HttpServletRequest) pageContext.getRequest());
            } else {
                adapter = (PermissionsAdapter) pageContext.findAttribute(permissions);

                if (adapter == null) {
                    throw new JspException(messages.getString("permissions.not.found"));
                }
            }
        }

        return adapter;
    }

    public int doEndTag() throws JspException {
        menuDisplayer.end(pageContext);
        pageContext.removeAttribute(DISPLAYER_KEY);
        pageContext.removeAttribute(PRIVATE_REPOSITORY);
        return EVAL_PAGE;
    }

    public void release() {
		log.debug("release() called");

        this.menuDisplayer = null;
        this.bundleKey = null;
        this.config = MenuDisplayer.DEFAULT_CONFIG;
        this.localeKey = null;
        this.name = null;
        this.menuDisplayer = null;
        this.repository = null;
        this.permissions = null;
        this.rb = null;
    }

}
