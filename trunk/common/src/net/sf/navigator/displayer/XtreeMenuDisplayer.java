package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Create the javascript required for the Xtree menu functionality
 * 
 */
public class XtreeMenuDisplayer extends MessageResourcesMenuDisplayer {
    //~ Static fields/initializers =============================================

    /*Variables for each menu item: (** means that they have to be specified!)
       0 name: The name of the item. This must be unique for each item. Do not use spaces or strange characters in this one! **
       1 parent_name: The name of the menuitem you want this to "connect" to. This will be a submenu of the item that have the name you place in here. ** for all other then the topitems
       2 text: The text you want in the item. 
       3 link: The page you want this item to link to.
       Remember you can have as many levels/sublevels as you want. Just make sure you spesify the correct "parent" for each item.
       To set styles for each level see above.
     */

    private static MessageFormat  newWebFXTreeMessage = new MessageFormat("var {0} = new WebFXTree(''{2}'');");
    private static MessageFormat  newWebFXTreeItemMessage = new MessageFormat("var {0} = new WebFXTreeItem(''{2}'');");
    private static MessageFormat  newWebFXTreeItemMessage1 = new MessageFormat("var {0} = new WebFXTreeItem(''{2}'',''{3}'');");
    private static MessageFormat  addMessage = new MessageFormat("{1}.add({0});");
    private static MessageFormat  writeMessage = new MessageFormat("document.write({0});");
    private static String parent = "";
    
    private static final String END_STATEMENT_START = "document.write("; 
    private static final String END_STATEMENT_END = ");"; 
    private static final String TAB = "    "; // four spaces
    private static final String SCRIPT_START = "\n<script type=\"text/javascript\">\n<!--";
    private static final String SCRIPT_END = "//-->\n</script>\n";

    //~ Methods ================================================================

    public void init(PageContext context, MenuDisplayerMapping mapping) {
        super.init(context, mapping);

        try {
            out.print(SCRIPT_START);
        } catch (Exception e) {
            // do nothing
        }
    }

    public void display(MenuComponent menu) throws JspException, IOException {
        StringBuffer sb = new StringBuffer();
        buildMenuString(menu, sb, isAllowed(menu));
        out.print("\n" + TAB + sb);
        out.print(TAB + END_STATEMENT_START + parent + END_STATEMENT_END);
    }

    public void end(PageContext context) {
        try {
            out.print(SCRIPT_END);
        } catch (Exception e) {
            // do nothing
        }
    }

    protected void buildMenuString(MenuComponent menu, StringBuffer sb, boolean allowed) {
        if (allowed) {
            String[] args = getArgs(menu);
            if (args[1] == null || args[1].equals("")) {
                sb.append(newWebFXTreeMessage.format(args)).append("\n").append(TAB).append(TAB);
                parent = args[0];
            } else if (args[3] == null || args[3].equals("")) {
                sb.append(newWebFXTreeItemMessage.format(args)).append("\n").append(TAB).append(TAB);
                sb.append(addMessage.format(args)).append("\n").append(TAB).append(TAB);
            } else {
                sb.append(newWebFXTreeItemMessage1.format(args)).append("\n").append(TAB).append(TAB);
                sb.append(addMessage.format(args)).append("\n").append(TAB).append(TAB);
            }
            
            MenuComponent[] subMenus = menu.getMenuComponents();

            if (subMenus.length > 0) {
                for (MenuComponent subMenu : subMenus) {
                    buildMenuString(subMenu, sb, isAllowed(subMenu));
                }
            }
        }
    }

    protected String[] getArgs(MenuComponent menu) {
        String[] args = new String[4];
        args[0] = menu.getName();
        args[1] = getParentName(menu);
        args[2] = getMessage(menu.getTitle());
        args[3] = (menu.getUrl() == null) ? EMPTY : menu.getUrl();

        return args;
    }

    protected String getParentName(MenuComponent menu) {
        return menu.getParent() == null ? "" : menu.getParent().getName();
    }

    protected String getTarget(MenuComponent menu) {
        String theTarget = super.getTarget(menu);

        if (theTarget == null) {
            theTarget = EMPTY;
        }

        return theTarget;
    }
}
