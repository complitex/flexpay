package net.sf.navigator.displayer;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.util.MessageResources;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractMenuDisplayer implements MenuDisplayer {

	protected Logger log = LoggerFactory.getLogger(getClass());

    protected String name;
    protected MessageResources displayStrings = MessageResources.getMessageResources(MenuDisplayer.DEFAULT_CONFIG);
    protected JspWriter out;
    protected String target;
	protected MenuComponent activeMenu;
    protected Set<String> activeMenuBranch = new HashSet<String>();
	protected Integer levelBegin;
	protected Integer levelEnd;
    protected MenuDisplayerMapping mapping;
	protected UserPreferences userPreferences;
	protected Collection<?> rolesGranted;

	public Collection<?> getRolesGranted() {
		return rolesGranted;
	}

	public void setRolesGranted(Collection<?> rolesGranted) {
		this.rolesGranted = rolesGranted;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public MenuComponent getActiveMenu() {
		return activeMenu;
	}

	public void setActiveMenu(MenuComponent activeMenu) {
		this.activeMenu = activeMenu;
		setActiveMenuBranch(activeMenu);
	}

    public Set<String> getActiveMenuBranch() {
        return activeMenuBranch;
    }

    public void addToActiveMenuBranch(MenuComponent activeMenu) {
        activeMenuBranch.add(activeMenu.getName());
    }

    public void setActiveMenuBranch(MenuComponent activeMenu) {
        activeMenuBranch.add(activeMenu.getName());
        if (activeMenu.getParent() != null) {
            setActiveMenuBranch(activeMenu.getParent());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    protected String getTarget(MenuComponent menu) {
        String theTarget = null;

        if (target == null) {
            if (menu.getTarget() != null) {
                theTarget = menu.getTarget();
            }
        } else {
            theTarget = target;
        }

        return theTarget;
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

	public String getMenuTarget(MenuComponent menu) {
		return target != null ? target : menu.getTarget() != null ? menu.getTarget() : "_self";
	}

	public String getMenuToolTip(MenuComponent menu) {
		return menu.getToolTip() != null ? getText(menu.getToolTip()) : getTitle(menu);
	}

	public String getTitle(MenuComponent menu) {
		return getText(menu.getTitle());
	}

	public String getText(String key) {
		return LocalizedTextUtil.findDefaultText(key, userPreferences.getLocale());
	}

    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        this.out = pageContext.getOut();
        this.mapping = mapping;
    }

    public abstract void display(MenuComponent menu) throws JspException, IOException;

    public void end(PageContext pageContext) {

    }

}
