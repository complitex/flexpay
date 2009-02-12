package net.sf.navigator.menu;

import net.sf.navigator.displayer.MenuDisplayerMapping;
import net.sf.navigator.util.LoadableResource;
import net.sf.navigator.util.LoadableResourceException;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Holder of Menus and their items. Can be populated programmatically
 */
public class MenuRepository implements LoadableResource, Serializable {

	private Logger log = LoggerFactory.getLogger(getClass());

	//~ Static fields/initializers =============================================

    public static final String MENU_REPOSITORY_KEY = "net.sf.navigator.menu.MENU_REPOSITORY";

    //~ Instance fields ========================================================

    protected String config = null;
    protected String name = null;
    protected ServletContext servletContext = null;
    protected LinkedMap menus = new LinkedMap();
    protected LinkedMap displayers = new LinkedMap();
    protected LinkedMap templates = new LinkedMap();
    private String breadCrumbDelimiter;

    //~ Methods ================================================================
    @SuppressWarnings({"unchecked"})
	public Set<String> getMenuNames() {
        return menus.keySet();
    }

    /**
     * Convenience method for dynamic menus - returns the top-level menus
     * only
	 *
	 * @return List of Menu components
	 */
    public List<MenuComponent> getTopMenus() {
        List<MenuComponent> topMenus = new ArrayList<MenuComponent>();
        if (menus == null) {
            log.warn("No menus found in repository!");
            return topMenus;
        }

        for (Object o : menus.keySet()) {
            String componentName = (String) o;
            MenuComponent menu = getMenu(componentName);
            if (menu.getParent() == null) {
                topMenus.add(menu);
            }
        }
        return topMenus;
    }

    public MenuComponent getMenu(String menuName) {
        return (MenuComponent) menus.get(menuName);
    }

    public MenuDisplayerMapping getMenuDisplayerMapping(String displayerName) {
        return (MenuDisplayerMapping) displayers.get(displayerName);
    }

    protected Digester initDigester() {
        Digester digester = new Digester();
        digester.setClassLoader(Thread.currentThread().getContextClassLoader());
        digester.push(this);

        //digester.setDebug(getDebug());
        // 1
        digester.addObjectCreate("MenuConfig/Menus/Menu", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu");
        digester.addSetNext("MenuConfig/Menus/Menu", "addMenu");

        // 2
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        // 3        
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        // 4
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        // 5
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        // 6
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        // 7
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "net.sf.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "addMenuComponent", "net.sf.navigator.menu.MenuComponent");

        digester.addObjectCreate("MenuConfig/Displayers/Displayer", "net.sf.navigator.displayer.MenuDisplayerMapping", "mapping");
        digester.addSetProperties("MenuConfig/Displayers/Displayer");
        digester.addSetNext("MenuConfig/Displayers/Displayer", "addMenuDisplayerMapping", "net.sf.navigator.displayer.MenuDisplayerMapping");
        digester.addSetProperty("MenuConfig/Displayers/Displayer/SetProperty", "property", "value");

        return digester;
    }

    /**
     * Adds a new menu.  This is called when parsing the menu xml definition
	 *
     * @param menu The menu component to add
     */
    public void addMenu(MenuComponent menu) {
        if (menus.containsKey(menu.getName())) {            
			log.warn("Menu '{}' already exists in repository", menu.getName());
            List<MenuComponent> children = getMenu(menu.getName()).getComponents();
            if (children != null && menu.getComponents() != null) {
                for (MenuComponent child : children) {
                    menu.addMenuComponent(child);
                }
            }
        }
		setFirstLast(menu, 1);
        menus.put(menu.getName(), menu);
    }

	public void setFirstLast(MenuComponent menu, int level) {
		MenuComponent[] components = menu.getMenuComponents();
		if (components.length == 0) {
			return;
		}
		components[0].setFirst(true);
		components[components.length - 1].setLast(true);
		for (MenuComponent component : components) {
			component.setLevel(level);
			setFirstLast(component, level + 1);
		}
	}

    /**
     * Allows easy removal of a menu by its name
     *
     * @param name name
     */
    public void removeMenu(String name) {
        menus.remove(name);
    }

    /**
     * Allows easy removal of all menus, suggested use for users wanting to reload menus without having to perform
     * a complete reload of the MenuRepository
     */
    public void removeAllMenus() {
        menus.clear();
    }

    public void addMenuDisplayerMapping(MenuDisplayerMapping displayerMapping) {
        displayers.put(displayerMapping.getName(), displayerMapping);
    }

    /**
     * This method is so menu repositories can retrieve displayers from the
     * default repository specified in menu-config.xml
	 *
     * @return the displayers specified in this repository
     */
    public LinkedMap getDisplayers() {
        return displayers;
    }

    /**
     * Allow the displayers to be set as a whole.  This should only be used
     * when copying the displayers from the default repository to a newly
     * created repository
     *
     * @param displayers displayers
     */
    public void setDisplayers(LinkedMap displayers) {
        this.displayers = displayers;
    }

    public void load() throws LoadableResourceException {
        if (getServletContext() == null) {
            throw new LoadableResourceException("no reference to servlet context found");
        }

        InputStream input = null;
        Digester digester = initDigester();

        try {
            input = getServletContext().getResourceAsStream(config);
            digester.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoadableResourceException("Error parsing resource file: " + config + " nested exception is: " + e.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void reload() throws LoadableResourceException {
        menus.clear();
        displayers.clear();
        load();
    }

    public void setLoadParam(String loadParam) {
        config = loadParam;
    }

    public String getLoadParam() {
        return config;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    /**
     * Get a subMenu beneath a root or parent menu.  Will drill-down as deep as requested
	 *
     * @param menuName - e.g grandParent.parent.menu
     * @param delimiter - e.g. '.'
     * @return MenuComponent
     */
    public MenuComponent getMenu(String menuName, String delimiter) {
        MenuComponent parent = null;
        StringTokenizer st = new StringTokenizer(menuName, delimiter);
        boolean firstMenu = true;

        while (st.hasMoreTokens()) {
            if (firstMenu) {
                parent = this.getMenu(st.nextToken());
                firstMenu = false;
				continue;
            }
			MenuComponent child = null;
			String nameComponent = st.nextToken();
			for (int a = 0; a < parent.getComponents().size(); a++) {
				if (nameComponent.equals((parent.getComponents().get(a)).getName())) {
					child = parent.getComponents().get(a);
					a = parent.getComponents().size();
				}
			}
			if (child != null) {
				parent = child;
			} else {
				parent = null;
				break;
			}
        }

        return parent;
    }

    /**
     * Get the depth of the deepest sub-menu within the requested top menu
	 *
     * @param menuName - name of the top menu to check the menu depth 
     * @return int.  If no menuName found return -1
     */
    public int getMenuDepth(String menuName) {
        MenuComponent menu = this.getMenu(menuName);
        if (menu == null) {
            return -1;
		}
        if (menu.getMenuComponents() == null) {
            return 1;
		}
        return menu.getMenuDepth();
    }

    /**
     * Get the depth of the deepest sub-menu throughout all menus held in the repository
	 *
     * @return int.  If no menus return -1.
     */
    public int getMenuDepth() {
        int currentDepth = 0;

        List<MenuComponent> topMenus = this.getTopMenus();

        if (topMenus == null) {
            return -1;
		}

        for (MenuComponent topMenu : topMenus) {
            int depth = topMenu.getMenuDepth();
            if (currentDepth < depth) {
                currentDepth = depth;
            }
        }
        return currentDepth;
    }

    /**
     * Get menus as array rather than a List
	 *
     * @return MenuComponent[]
     */
    public MenuComponent[] getTopMenusAsArray() {
        List<MenuComponent> menuList = this.getTopMenus();
        MenuComponent[] menuArray = new MenuComponent[menuList.size()];
        for (int a = 0; a < menuList.size(); a++) {
            menuArray[a] = menuList.get(a);
        }

        return menuArray;
    }

    /**
     * Get a List of all the top menus' names
	 *
     * @return List
     */
    public List<String> getTopMenusNames() {
        List<MenuComponent> menuList = this.getTopMenus();
        List<String> names = new ArrayList<String>();
        for (MenuComponent menu : menuList) {
            names.add(menu.getName());
        }
        return names;
    }

    public void setBreadCrumbDelimiter(String string) {
        breadCrumbDelimiter = string;
    }

    public void buildBreadCrumbs() {
        if (breadCrumbDelimiter == null) {
            throw new NullPointerException("No breadCrumbDelimiter present");
        }
        List<MenuComponent> menuList = this.getTopMenus();
        for (MenuComponent menu : menuList) {
            menu.setBreadCrumb(breadCrumbDelimiter);
        }
    }

    public void buildBreadCrumbs(String delimiter) {
        this.breadCrumbDelimiter = delimiter;
        buildBreadCrumbs();
    }

}
