/*
 * SimpleMenuDisplayer.java
 *
 * Created on February 15, 2001, 11:14 AM
 */
package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 *
 * @author  ssayles
 * @version 1.0
 */
public class SimpleMenuDisplayer extends MessageResourcesMenuDisplayer {

    protected static final String nbsp = NBSP;

    public void init(PageContext pageContext, MenuDisplayerMapping mapping) {
        super.init(pageContext, mapping);

        try {
            out.println(displayStrings.getMessage("smd.style"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void display(MenuComponent menu) throws JspException, IOException {
        if (isAllowed(menu)) {
            out.println(displayStrings.getMessage("smd.menu.top"));
            displayComponents(menu, 0);
            out.println(displayStrings.getMessage("smd.menu.bottom"));
        }
    }

    protected void displayComponents(MenuComponent menu, int level) throws JspException, IOException {
        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            out.println(displayStrings.getMessage("smd.menu.item.top",
                    getSpace(level) +
                    displayStrings.getMessage("smd.menu.item.image.bullet") +
                    getMessage(menu.getTitle())));

            for (MenuComponent component : components) {
                if (component.getMenuComponents().length > 0) {
                    if (isAllowed(component)) {
                        displayComponents(component, level + 1);
                    }
                } else {
                    if (isAllowed(component)) {
                        out.println(displayStrings.getMessage("smd.menu.item", component.getUrl(), super.getMenuTarget(component), super.getMenuToolTip(component), this.getSpace(level + 1) + getImage(component) + this.getMessage(component.getTitle())));
                    }
                }
            }
        } else {
            out.println(displayStrings.getMessage("smd.menu.item",
                    menu.getUrl(), super.getMenuTarget(menu),
                    super.getMenuToolTip(menu),
                    this.getSpace(level) + getImage(menu) +
                    getMessage(menu.getTitle())));
        }
    }

    protected String getSpace(int length) {
        String space = EMPTY;

        for (int i = 0; i < length; i++) {
            space = space + nbsp + nbsp;
        }

        return space;
    }

    protected String getImage(MenuComponent menu) {
        return menu.getImage() == null || menu.getImage().equals(EMPTY) ? EMPTY : displayStrings.getMessage("smd.menu.item.image", menu.getImage(), super.getMenuToolTip(menu));
    }

}
