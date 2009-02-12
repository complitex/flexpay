package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.PermissionsAdapter;
import net.sf.navigator.util.MessageResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Abstract implementation of <code>MenuDisplayer</code> that can be used as
 * a basis for other menu displayers
 */
public abstract class AbstractMenuDisplayer implements MenuDisplayer {

	protected Logger log = LoggerFactory.getLogger(getClass());

    protected String name;
    protected MessageResources displayStrings;
    protected JspWriter out;
    protected String target;
	protected Integer levelBegin;
	protected Integer levelEnd;

    /** Holds value of property permissionsAdapter. */
    protected PermissionsAdapter permissionsAdapter;
    protected MenuDisplayerMapping mapping;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        String config = null;

        if (displayStrings != null) {
            config = displayStrings.getConfig();
        }

        return config;
    }

    public void setConfig(String config) {
        displayStrings = MessageResources.getMessageResources(config);
    }

    public String getTarget() {
        return target;
    }

    /**
     * Convenience method that will first return the target for the displayer
     * if it is not null.  If the displayer target is null, then it will
     * return <code>menu.getTarget()</code>
     *
     * @param menu menu
     * @return the target for the menu link
     */
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

	/**
     * Getter for property permissionsAdapter
	 *
     * @return Value of property permissionsAdapter
     */
    public PermissionsAdapter getPermissionsAdapter() {
        return this.permissionsAdapter;
    }

    /**
     * Setter for property permissionsAdapter
	 *
     * @param permissionsAdapter New value of property permissionsAdapter
     */
    public void setPermissionsAdapter(PermissionsAdapter permissionsAdapter) {
        this.permissionsAdapter = permissionsAdapter;
    }

    /**
     * Lifecycle method that should be called when the <code>MenuDisplayer</code>
     * is being prepared for use
     *
     * @param pageContext The JSP pageContext to give the displayer access
     *              	  to any resources it may need
     * @param mapping The menu displayer mapping used to embody the xml
     *                definition
     */
    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        this.out = pageContext.getOut();
        this.mapping = mapping;
    }

    public abstract void display(MenuComponent menu) throws JspException, IOException;

    public void end(PageContext pageContext) {

    }

    /**
     * Returns <code>true</code> if the specified component is usable.
     * If <code>permissionsAdapter</code> is not defined, this method will
     * return <code>true</code>.  Otherwise, the adapter will be used to check
     * permissions on the menu
     *
     * @param menu The menu component
	 * @return <code>true</code> if the menu component is usable
     */
    public boolean isAllowed(MenuComponent menu) {
        return permissionsAdapter == null || permissionsAdapter.isAllowed(menu);
    }

}
