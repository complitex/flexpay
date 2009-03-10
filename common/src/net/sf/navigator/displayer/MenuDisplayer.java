package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public interface MenuDisplayer {

    public static final String DEFAULT_CONFIG = "net.sf.navigator.displayer.menuStrings";

    void display(MenuComponent menu) throws JspException, IOException;

	MenuComponent getActiveMenu();

	void setActiveMenu(MenuComponent activeMenu);

    void addToActiveMenuBranch(MenuComponent activeMenu);

    Set<String> getActiveMenuBranch();

    void setActiveMenuBranch(MenuComponent activeMenu);

    String getName();

    void setName(String name);

    String getTarget();

    void setTarget(String target);

	void setLevelBegin(Integer levelBegin);

	void setLevelEnd(Integer levelEnd);

    void init(PageContext pageContext, MenuDisplayerMapping mapping);

    void end(PageContext pageContext);

	UserPreferences getUserPreferences();

	void setUserPreferences(UserPreferences userPreferences);

	Collection<?> getRolesGranted();

	void setRolesGranted(Collection<?> rolesGranted);

}
