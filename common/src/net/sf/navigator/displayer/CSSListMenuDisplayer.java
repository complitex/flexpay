package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 *
 * @author  <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version 1.0
 */
public class CSSListMenuDisplayer extends ListMenuDisplayer {

    //~ Methods ================================================================
    public void display(MenuComponent menu) throws JspException, IOException {
        if (isAllowed(menu)) {
            out.println(displayStrings.getMessage("ccslm.menu.top",
                    (hasViewableChildren(menu)) ? " class=\"menubar\"" : ""));
            displayComponents(menu, 0);
            out.println(displayStrings.getMessage("lmd.menu.bottom"));
        }
    }

    protected void displayComponents(MenuComponent menu, int level)
    throws JspException, IOException {
        if (menu.getUrl() == null) {
            // http://issues.appfuse.org/browse/SM-48 - javascript:void(0) works better than #
            menu.setUrl("javascript:void(0)");
        }
        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            // if there is a location/page/action tag on base item use it
            if (components.length == 0){
                out.println(displayStrings.getMessage("lmd.menu.item",
                            menu.getUrl(),
                            super.getMenuToolTip(menu),
                            getExtra(menu),
                            this.getMessage(menu.getTitle())));
            } else {
                out.println(displayStrings.getMessage("ccslm.menubar.top",
                            menu.getUrl(), getExtra(menu), this.getMessage(menu.getTitle())));
                if (hasViewableChildren(menu)) {
                    out.println("\t\t<ul>");
                }
            }

            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    if (components[i].getMenuComponents().length > 0) {
                        if (!hasViewableChildren(components[i])) {
                            out.println("<li>");
                        } else {
                            out.println("<li class=\"menubar\">");
                        }

                        displayComponents(components[i], level + 1);

                        out.println((hasViewableChildren(components[i]) ? "\t\t</ul>\t</li>\n" : "\t</li>\n"));
                        //out.println(displayStrings.getMessage("lmd.menu.actuator.bottom"));
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
            if (level == 0 && hasViewableChildren(menu)) {
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

    private boolean hasViewableChildren(MenuComponent menu) {
        for (int i = 0; i < menu.getMenuComponents().length; i++) {
            if (isAllowed(menu.getMenuComponents()[i])) {
                return true;
            }
        }
        return false;
    }
}
