package net.sourceforge.navigator.displayer;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import net.sourceforge.navigator.menu.MenuComponent;
import net.sourceforge.navigator.util.MessageResources;
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

	@Override
	public Collection<?> getRolesGranted() {
		return rolesGranted;
	}

	@Override
	public void setRolesGranted(Collection<?> rolesGranted) {
		this.rolesGranted = rolesGranted;
	}

	@Override
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	@Override
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	@Override
	public MenuComponent getActiveMenu() {
		return activeMenu;
	}

	@Override
	public void setActiveMenu(MenuComponent activeMenu) {
		this.activeMenu = activeMenu;
		setActiveMenuBranch(activeMenu);
	}

	@Override
    public Set<String> getActiveMenuBranch() {
        return activeMenuBranch;
    }

	@Override
    public void addToActiveMenuBranch(MenuComponent activeMenu) {
        activeMenuBranch.add(activeMenu.getName());
    }

	@Override
    public void setActiveMenuBranch(MenuComponent activeMenu) {
        activeMenuBranch.add(activeMenu.getName());
        if (activeMenu.getParent() != null) {
            setActiveMenuBranch(activeMenu.getParent());
        }
    }

	@Override
    public String getName() {
        return name;
    }

	@Override
    public void setName(String name) {
        this.name = name;
    }

	@Override
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

	@Override
    public void setTarget(String target) {
        this.target = target;
    }

	@Override
	public void setLevelBegin(Integer levelBegin) {
		this.levelBegin = levelBegin;
	}

	@Override
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

	@Override
    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        this.out = pageContext.getOut();
        this.mapping = mapping;
    }

	@Override
    public abstract void display(MenuComponent menu) throws JspException, IOException;

	@Override
    public void end(PageContext pageContext) {

    }

}
