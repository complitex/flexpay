package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.PermissionsAdapter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public interface MenuDisplayer {

    public static final String DEFAULT_CONFIG = "net.sf.navigator.displayer.DisplayerStrings";
    public static final String _SELF = "_self";
    public static final String NBSP = "&nbsp;";
    public static final String EMPTY = "";

    public void display(MenuComponent menu) throws JspException, IOException;

    /**
     * Returns the name of the <code>MenuDisplayer</code>
	 *
     * @return Value of property name
     */
    public String getName();

    /**
     * Sets the name of the <code>MenuDisplayer</code>
	 *
     * @param name New value of property name
     */
    public void setName(String name);

    /**
     * Returns the name of the message resources bundle that contains
     * display and/or configuration definitions
     *
     * @return Value of property config
     */
    public String getConfig();

    /**
     * Sets the name of the message resources bundle that contains
     * display and/or configuration definitions
     *
     * @param config New value of property config
     */
    public void setConfig(String config);

    /**
     * Returns the target name for any hrefs that may be generated
     *
     * @return the value for target
     */
    public String getTarget();

    /**
	 * Setter for property target
	 *
     * @param target New value of property target
     */
    public void setTarget(String target);

	/**
	 * Setter for property levelBegin
	 *
	 * @param levelBegin New value of property levelBegin
	 */
	public void setLevelBegin(Integer levelBegin);

	/**
	 * Setter for property levelEnd
	 *
	 * @param levelEnd New value of property levelEnd
	 */
	public void setLevelEnd(Integer levelEnd);

    /**
     * Lifecycle method that should be called when the <code>MenuDisplayer</code>
     * is being prepared for use
     *
     * @param pageContext The JSP pageContext to give the displayer
	 * 					  access to any resources it may need
     * @param mapping The menu displayer mapping used to embody the xml definition
     */
    public void init(PageContext pageContext, MenuDisplayerMapping mapping);

    /**
     * Lifecycle method that should be called when the <code>MenuDisplayer</code>
     * is done being used
     *
     * @param pageContext The JSP pageContext to give the displayer access
	 * 					  to any resources it may need
     */
    public void end(PageContext pageContext);

    /**
	 * Getter for property permissionsAdapter
	 *
     * @return Value of property permissionsAdapter
     */
    public PermissionsAdapter getPermissionsAdapter();

    /**
	 * Setter for property permissionsAdapter
	 *
     * @param permissionsAdapter New value of property permissionsAdapter
     */
    public void setPermissionsAdapter(PermissionsAdapter permissionsAdapter);

}
