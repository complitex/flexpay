/*
 * CoolMenuDisplayer.java
 *
 * Created on March 21, 2002, 5:47 PM
 */
package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.text.MessageFormat;

/**
 *
 * @author  ssayles
 */
public class CoolMenuDisplayer extends MessageResourcesMenuDisplayer {

    /**
       Variables for each menu item: (** means that they have to be spesified!)
       0 name: The name of the item. This must be unique for each item. Do not use spaces or strange charachters in this one! **
       1 parent: The name of the menuitem you want this to "connect" to. This will be a submenu of the item that have the name you place in here. ** for all other then the topitems
       2 text: The text you want in the item. ** (except if you use images)
       3 link: The page you want this item to link to.
       4 target: The target window or frame you want the link to go to (Default is same window if you're not using frames, and the mainframe if you're using frames)
       width: The width of the element. If not spesified it will get the default width spesified above.
       height: The height of the element. If not spesified it will get the default height spesified above.
       5 img1: The "off" image for element if you want to use images.
       6 img2: The image that appears onmouseover if using images.
       7 bgcoloroff: The background color for this item. If not spesified it will get the default background color spesified above.
       8 bgcoloron: The "on" background color for this item. If not spesified it will get the default "on" background color spesified above.
       9 textcolor: The text color for this item. If not spesified it will get the default text color spesified above.
       10 hovercolor: The "on" text color for this item. If not spesified it will get the default "on" text color spesified above. Netscape4 ignores this
       11 onclick: If you want something to happen when the element is clicked (different from going to a link) spesifiy it here.
       onmouseover: This will happen when you mouseover the element. Could be status text, another imageswap or whatever.
       onmouseout: This will happen when you mouseout the element.
       Remember you can have as many levels/sublevels as you want. Just make sure you spesify the correct "parent" for each item.
       To set styles for each level see above.
     */

    //oCMenu.makeMenu('top0','','&nbsp;News','','')
    //    oCMenu.makeMenu('sub10','top1','New scripts','/scripts/index.asp?show=new')

    /**
     * main message format of the menu.  only 10 args max in jdk1.3 :(
     */
    private static MessageFormat menuMessage =
        new MessageFormat(
            "oCMenu.makeMenu(''{0}'',''{1}'',''{2}'',''{3}'',''{4}'','''','''',''{5}'',''{6}'',{7},{8},{9},");
    private static final String SCRIPT_START = "<script type=\"text/javascript\">\n";
    private static final String SCRIPT_END = "</script>\n";
    private static final String END_STATEMENT = "\noCMenu.makeStyle(); oCMenu.construct()\n";
    private static final String TOP_IMAGE = "cmTopMenuImage";
    private static final String SUB_IMAGE = "cmSubMenuImage";
    private static final String BGCOL_ON = "cmBGColorOn";
    private static final String BGCOL_OFF = "cmBGColorOff";
    private static final String TXTCOL = "cmTxtColor";
    private static final String HOVER = "cmHoverColor";
    private static final String DIS_BGCOL_ON = "cmDisBGColorOn";
    private static final String DIS_BGCOL_OFF = "cmDisBGColorOff";
    private static final String DIS_TXTCOL = "cmDisTxtColor";
    private static final String DIS_HOVER = "cmDisHoverColor";

    public void init(PageContext context, MenuDisplayerMapping mapping) {
        super.init(context, mapping);

        try {
            out.print(SCRIPT_START);
        } catch (Exception e) {}
    }

    /**
     * Prints the appropriate javascript for CoolMenu using \
     * <code>menuMessage</code> as the format.
     */
    public void display(MenuComponent menu) throws JspException, IOException {
        StringBuffer sb = new StringBuffer();
        buildMenuString(menu, sb, isAllowed(menu));
        out.print(sb);
    }

    /**
     * This will output the ending javascript statements defined in
     * <code>END_STATEMENT</code> and <code>SCRIPT_END</code>
     */
    public void end(PageContext context) {
        try {
            out.print(END_STATEMENT);
            out.print(SCRIPT_END);
        } catch (Exception e) {
            // do nothing
        }
    }

    protected void buildMenuString(MenuComponent menu, StringBuffer sb, boolean allowed) {
        sb.append(menuMessage.format(getArgs(menu, allowed)));
        sb.append((allowed) ? HOVER : DIS_HOVER);
        sb.append(",'");
        sb.append((menu.getOnclick() == null) ? EMPTY : menu.getOnclick());
        sb.append("')\n");

        MenuComponent[] subMenus = menu.getMenuComponents();

        if (subMenus.length > 0) {
            for (MenuComponent subMenu : subMenus) {
                buildMenuString(subMenu, sb, (allowed) ? isAllowed(subMenu) : allowed);
            }
        }
    }

    protected String[] getArgs(MenuComponent menu, boolean allowed) {
        String[] args = new String[10];
        args[0] = menu.getName();
        args[1] = getParentName(menu);
        args[2] = getTitle(menu);
        args[3] = menu.getUrl() == null ? EMPTY : allowed ? menu.getUrl() : EMPTY;
        args[4] = getTarget(menu);
        args[5] = EMPTY;
        args[6] = EMPTY;
        args[7] = (allowed) ? BGCOL_OFF : DIS_BGCOL_OFF;
        args[8] = (allowed) ? BGCOL_ON : DIS_BGCOL_ON;
        args[9] = (allowed) ? TXTCOL : DIS_TXTCOL;

        return args;
    }

    /**
     * Return a translated title for the menu item that will contain
     * the <code>TOP_IMAGE</code> javscript variable if it is a parent
     * menu with sub menus, or the <code>SUB_IMAGE</code> variable if it
     * is a sub menu with sub menus.
     */
    protected String getTitle(MenuComponent menu) {
        String title = getMessage(menu.getTitle());

        if (menu.getMenuComponents().length > 0) {
            if (menu.getParent() == null) { //then us the top image
                title = title + "'+" + TOP_IMAGE + "+'";
            } else { //use the sub menu image
                title = title + "'+" + SUB_IMAGE + "+'";
            }
        }

        return title;
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
