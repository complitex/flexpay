/*
 * ListMenuDisplayer.java
 *
 * Created on December 7, 2002, 12:35 AM
 */
package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;


/**
 *
 * @author  <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version 1.0
 */
public class ListMenuDisplayer extends MessageResourcesMenuDisplayer {
    //~ Instance fields ========================================================

    //~ Methods ================================================================

    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        super.init(pageContext, mapping);
        String id = (String) pageContext.getAttribute("menuId");

        try {
            out.println(displayStrings.getMessage("lmd.begin",
                (id != null) ?  "id=\"" + id + "\" " : ""));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void display(MenuComponent menu) throws JspException, IOException {
        if (isAllowed(menu)) {
            out.println(displayStrings.getMessage("lmd.menu.top"));
            displayComponents(menu, 0);
            out.println(displayStrings.getMessage("lmd.menu.bottom"));
        }
    }

    protected void displayComponents(MenuComponent menu, int level)
    throws JspException, IOException {
        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            // eliminate spaces in string used for Id
            String domId = StringUtils.deleteWhitespace(getMessage(menu.getName()));
            // added to create a unique id everytime
            domId += ((int) (1000*Math.random()));

            String menuClass = "menu";

            if (level >= 1) {
                menuClass = "submenu";
            }

            // if there is a location/page/action tag on base item use it
            if (menu.getUrl() != null ){
                out.println(displayStrings.getMessage("lmd.menu.actuator.link",
                            domId, getMessage(menu.getTitle()), menuClass,
                            getMessage(menu.getUrl())));
            } else {
                out.println(displayStrings.getMessage("lmd.menu.actuator.top",
                        domId,
                        getMessage(menu.getTitle()),
                        menuClass));
            }
            
            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    if (components[i].getMenuComponents().length > 0) {
                        out.println("<li>");

                        displayComponents(components[i], level + 1);

                        out.println(displayStrings.getMessage("lmd.menu.actuator.bottom"));
                    } else {
                        out.println(displayStrings.getMessage("lmd.menu.item",
                                                              components[i].getUrl(),
                                                              super.getMenuToolTip(components[i]),
                                                              getExtra(components[i]),
                                                              this.getMessage(components[i].getTitle())));
                    }
                }
            }

            // close the </ul> for the top menu
            if (menuClass.equals("menu")) {
                out.println("</ul>");
            }
        } else {
            if (menu.getParent() == null) {
                out.println(displayStrings.getMessage("lmd.menu.standalone",
                                                      menu.getUrl(),
                                                      super.getMenuToolTip(menu),
                                                      getExtra(menu),
                                                      getMessage(menu.getTitle())));
            } else {
                out.println(displayStrings.getMessage("lmd.menu.item",
                                                      menu.getUrl(),
                                                      super.getMenuToolTip(menu),
                                                      getExtra(menu),
                                                      getMessage(menu.getTitle())));
            }
        }
    }

    /**
     * This will output the ending HTML code to close tags from the beginning
     * @param context the current pageContext
     */
    public void end(PageContext context) {
        try {
            out.print(displayStrings.getMessage("lmd.end"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String getExtra(MenuComponent menu) {
        StringBuffer extra = new StringBuffer();
        if (menu.getTarget() != null) {
            extra.append(" target=\"").append(menu.getTarget()).append("\"");
        }
        if (menu.getOnclick() != null) {
            extra.append(" onclick=\"").append(menu.getOnclick()).append("\"");
        }
        if (menu.getOnmouseover() != null) {
            extra.append(" onmouseover=\"").append(menu.getOnmouseover()).append("\"");
        }
        if (menu.getOnmouseout() != null) {
            extra.append(" onmouseout=\"").append(menu.getOnmouseout()).append("\"");
        }
        if (menu.getWidth() != null) {
            extra.append(" style=\"width: " + menu.getWidth() + "px\"");
        }
        return (extra.length() > 0) ? extra.toString() : "";
    }
}
