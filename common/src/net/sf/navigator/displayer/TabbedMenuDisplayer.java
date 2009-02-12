package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import java.io.IOException;


/**
 *
 * @author  <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version 1.0
 */
public class TabbedMenuDisplayer extends ListMenuDisplayer {

    public void display(MenuComponent menu) throws JspException, IOException {
        if (isAllowed(menu)) {
            displayComponents(menu, 0);
        }
    }

    protected void displayComponents(MenuComponent menu, int level) throws JspException, IOException {
        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            out.print("\t<li>");

            String menuClass = "submenu";

            if (level >= 1) {
                menuClass = "deepmenu";
            }

            if (menu.getUrl() == null) {
                log.info("The Menu '" + getMessage(menu.getTitle()) +
                    "' does not have a location defined, using first submenu's location");
                menu.setUrl(components[0].getUrl());
            }

            out.print(displayStrings.getMessage("tmd.menu.tab",
                    menu.getUrl(), super.getMenuToolTip(menu),
                    getExtra(menu), getMessage(menu.getTitle())));

            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    if (components[i].getMenuComponents().length > 0) {
                        // and an <li> for submenus (but not deepmenus)
                        if (menuClass.equals("submenu")) {
                            out.print("<li>");
                        }

                        displayComponents(components[i], level + 1);

                        out.println("</ul></li>");

                        if (i == (components[i].getMenuComponents().length - 1)) { // last one
                            out.println("</li>");
                        }
                    } else {
                        out.println(displayStrings.getMessage("tmd.menu.item",
                                components[i].getUrl(),
                                super.getMenuToolTip(components[i]),
                                super.getExtra(components[i]),
                                this.getMessage(components[i].getTitle())));
                    }
                }
            }

            // close the </ul> for the top menu
            if (menuClass.equals("submenu")) {
                out.println("\t</ul>");
            }

            out.print("\t</li>");
        } else {
            out.println(displayStrings.getMessage("tmd.menu.item",
                    menu.getUrl(), super.getMenuToolTip(menu),
                    super.getExtra(menu), getMessage(menu.getTitle())));
        }
    }

}
