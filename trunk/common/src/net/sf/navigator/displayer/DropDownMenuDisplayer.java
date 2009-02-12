/*
 * DropDownMenuDisplayer.java
 *
 * Created on February 6, 2001, 1:00 PM
 */
package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;


/**
 *
 * @author  ssayles
 * @version
 */
public class DropDownMenuDisplayer extends MessageResourcesMenuDisplayer {
    //~ Methods ================================================================

    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        super.init(pageContext, mapping);

        StringBuffer sb = new StringBuffer();

        // add the stylesheet
        sb.append(displayStrings.getMessage("smd.style", "{", "}"));
        
        //display the javascript function
        sb.append(displayStrings.getMessage("dd.js.start"));
        sb.append(displayStrings.getMessage("dd.js.image.src.expand",
                displayStrings.getMessage("dd.image.src.expand")));
        sb.append(displayStrings.getMessage("dd.js.image.src.expanded",
                displayStrings.getMessage("dd.image.src.expanded")));
        sb.append(displayStrings.getMessage("dd.js.toggle.display", "{", "}"));
        sb.append(displayStrings.getMessage("dd.js.end"));

        try {
            out.print(sb.toString());
        } catch (Exception e) {}
    }

    public void display(MenuComponent menu) throws JspException, IOException {
        String title = super.getMessage(menu.getTitle());
        StringBuffer sb = new StringBuffer();
        String img = EMPTY;

        if (menu.getImage() != null) {
            img = displayStrings.getMessage("dd.image", menu.getImage());
        }

        MenuComponent[] components = menu.getMenuComponents();

        sb.append(displayStrings.getMessage("dd.menu.top"));

        if (components.length > 0) {
            if (this.isAllowed(menu)) {
                sb.append(displayStrings.getMessage("dd.menu.expander",
                        menu.getName(), menu.getName() + "_img",
                        displayStrings.getMessage("dd.image.expander",
                            menu.getName() + "_img",
                            displayStrings.getMessage("dd.image.src.expand")) +
                        NBSP + img + title));
                displayComponents(menu, sb);
                sb.append(displayStrings.getMessage("dd.menu.restore",
                        menu.getName(), menu.getName() + "_img"));
            } else {
                // the dd.menu.restricted key is missing!
                sb.append(displayStrings.getMessage("dd.menu.restricted",
                        menu.getName(), menu.getName() + "_img",
                        displayStrings.getMessage("dd.image.expander",
                            menu.getName() + "_img",
                            displayStrings.getMessage("dd.image.src.expand")) +
                        NBSP + img + title));
            }
        } else {
            sb.append(title);
        }

        sb.append(displayStrings.getMessage("dd.menu.bottom"));
        out.println(sb.toString());
    }

    private void displayComponents(MenuComponent menu, StringBuffer sb)
    throws JspException, IOException {
        String title = null;
        String name = menu.getName();
        String href = EMPTY;
        String img = EMPTY;
        MenuComponent[] components = menu.getMenuComponents();

        sb.append(displayStrings.getMessage("dd.menu.item.top", name));

        for (int i = 0; i < components.length; i++) {
            title = super.getMessage(components[i].getTitle());

            if (components[i].getImage() != null) {
                img = displayStrings.getMessage("dd.image",
                        components[i].getImage());
            } else {
                img = EMPTY;
            }

            href = components[i].getUrl();

            sb.append(displayStrings.getMessage("dd.menu.item.row.start"));

            if (components[i].getMenuComponents().length > 0) {
                if (this.isAllowed(components[i])) {
                    sb.append(displayStrings.getMessage("dd.menu.expander",
                            components[i].getName(),
                            components[i].getName() + "_img",
                            displayStrings.getMessage("dd.image.expander",
                                components[i].getName() + "_img",
                                displayStrings.getMessage("dd.image.src.expand")) +
                            NBSP + img + title));
                    displayComponents(components[i], sb);
                    sb.append(displayStrings.getMessage("dd.menu.restore",
                            components[i].getName(),
                            components[i].getName() + "_img"));
                } else {
                    sb.append(displayStrings.getMessage("dd.menu.restricted",
                            components[i].getName(),
                            components[i].getName() + "_img",
                            displayStrings.getMessage("dd.image.expander",
                                components[i].getName() + "_img",
                                displayStrings.getMessage("dd.image.src.expand")) +
                            NBSP + img + title));
                }
            } else {
                if (this.isAllowed(components[i])) {
                    sb.append(displayStrings.getMessage("dd.link.start", href,
                            super.getMenuTarget(components[i]),
                            super.getMenuToolTip(components[i])));
                    sb.append(NBSP);
                    sb.append(NBSP); //a couple of spaces
                    sb.append(img);
                    sb.append(title);
                    sb.append(displayStrings.getMessage("dd.link.end"));
                } else {
                    sb.append(displayStrings.getMessage("dd.link.restricted",
                            href, super.getMenuTarget(components[i]),
                            super.getMenuToolTip(components[i])));
                }
            }

            sb.append(displayStrings.getMessage("dd.menu.item.row.end"));
        }

        sb.append(displayStrings.getMessage("dd.menu.item.bottom"));
    }
}
