package net.sourceforge.navigator.taglib;

import net.sourceforge.navigator.displayer.MenuDisplayer;
import net.sourceforge.navigator.menu.MenuComponent;
import net.sourceforge.navigator.menu.MenuRepository;
import net.sourceforge.navigator.util.MenuUtils;
import org.apache.struts2.views.util.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class DisplayMenuTag extends TagSupport {

	protected Logger log = LoggerFactory.getLogger(getClass());

    private String name;
    private String target;
	private Integer levelBegin;
	private Integer levelEnd;

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(String target) {
        this.target = target;
    }

	public void setLevelBegin(Integer levelBegin) {
		this.levelBegin = levelBegin;
	}

	public void setLevelEnd(Integer levelEnd) {
		this.levelEnd = levelEnd;
	}

	@Override
	public int doStartTag() throws JspException {

        MenuDisplayer displayer = (MenuDisplayer) pageContext.getAttribute(UseMenuDisplayerTag.DISPLAYER_KEY);
        if (displayer == null) {
            throw new JspException("Could not retrieve the menu displayer");
        }

        MenuRepository repository = (MenuRepository) pageContext.getAttribute(UseMenuDisplayerTag.PRIVATE_REPOSITORY);
		ApplicationContext applicationContext = repository.getApplicationContext();
        MenuComponent menu = repository.getMenu();

        if (menu != null) {
            try {
                // use the overridden target
                if (target != null) {
                    displayer.setTarget(target);
                }

                if (levelBegin != null) {
                    displayer.setLevelBegin(levelBegin);
                }

                if (levelEnd != null) {
                    displayer.setLevelEnd(levelEnd);
                }

                // set the location value to use
                // the context relative page attribute
                // if specified in the menu
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                try {
                    setPageLocation(menu);
                } catch (MalformedURLException e) {
                    log.error("Incorrect action or forward", e);
                    log.warn("Menu '{}' location set to #", menu.getName());
                    menu.setLocation("#");
                }

				String curMenuComponentName = (String) WebUtils.getSessionAttribute(request, MenuComponent.ACTIVE_MENU);
				MenuComponent activeMenuComponent;
                if (curMenuComponentName == null) {
                    String curAction = (String) WebUtils.getSessionAttribute(request, "currentAction");
                    activeMenuComponent = MenuUtils.getMenuByAction(curAction, applicationContext);
                    if (activeMenuComponent != null) {
                        curMenuComponentName = activeMenuComponent.getName();
                        WebUtils.setSessionAttribute(request, MenuComponent.ACTIVE_MENU, curMenuComponentName);
                    }
                }
                MenuComponent curMenu = MenuUtils.getMenuByName(curMenuComponentName, menu);
                if (curMenu != null) {
                    displayer.setActiveMenu(curMenu);
                } else {
					displayer.setActiveMenu(menu.getComponents().get(menu.getComponents().size() - 1));
				}
                displayer.display(menu);
                displayer.setTarget(null);
            } catch (Exception e) {
                log.error("Exception: ", e);
                throw new JspException(e);
            }
        } else {
            String error = "No menu in repository with name: " + name;
            log.warn(error);
            try {
                pageContext.getOut().write(error);
            } catch (IOException e) {
                log.error("Exception: ", e);
                throw new JspException(error);
            }
        }

        return SKIP_BODY;
    }

    protected void setPageLocation(MenuComponent menu) throws MalformedURLException, JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        setLocation(menu);
        String url = menu.getLocation();

        // Check if there are parameters on the value
        if (url != null && url.contains("${")) {
            String queryString = null;

            if (url.contains("?")) {
                queryString = url.substring(url.indexOf("?") + 1);
                url = url.substring(0, url.indexOf(queryString));
            }

            // variable is in the URL
            menu.setUrl(queryString != null ? url + parseString(queryString, request) : parseString(url, request).toString());
        } else {
            menu.setUrl(url);
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        if (menu.getUrl() != null) {
            menu.setUrl(response.encodeURL(menu.getUrl()));
        }

        // do all contained menus
        MenuComponent[] subMenus = menu.getMenuComponents();

        if (subMenus.length > 0) {
            for (MenuComponent subMenu : subMenus) {
                setPageLocation(subMenu);
            }
        }
    }

    @SuppressWarnings ({"unchecked"})
	protected void setLocation(MenuComponent menu) throws MalformedURLException {
        if (menu.getLocation() == null) {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            if (menu.getAction() != null) {
				String menuParam = request.getParameter("menu");

				if (menuParam == null || !menuParam.equals(menu.getName())) {
					request.getParameterMap().put("menu", new String[] {menu.getName()});
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("menu", request.getParameter("menu"));

                menu.setLocation(UrlHelper.buildUrl(menu.getNamespace() + "/" + menu.getAction(), request, response, params));
            }
        }
    }

    private StringBuffer parseString(String str, HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();

        while (str.contains("${")) {
            sb.append(str.substring(0, str.indexOf("${")));

            String variable = str.substring(str.indexOf("${") + 2, str.indexOf("}"));
            String value = String.valueOf(pageContext.findAttribute(variable)); 

            if (value == null) {
                // look for it as a request parameter
                value = request.getParameter(variable);
				// is value still null?!
				if (value == null) {
					log.warn("Value for '{}' not found in pageContext or as a request parameter", variable);
				}
            }

            sb.append(value);
            str = str.substring(str.indexOf("}") + 1, str.length());
        }

        return sb.append(str);
    }

	@Override
    public void release() {
        name = null;
        target = null;
		levelBegin = null;
		levelEnd = null;
    }

}
