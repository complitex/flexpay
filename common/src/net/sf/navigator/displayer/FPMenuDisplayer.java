package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FPMenuDisplayer extends AbstractMenuDisplayer {

	private Map<Integer, MenuComponent> levelComponents = new HashMap<Integer, MenuComponent>();

    public void display(MenuComponent menu) throws JspException, IOException {
		if (levelBegin == null) {
			levelBegin = 1;
		}

		if (menu.getMenuComponents().length == 0) {
			log.error("Incorrect settings for struts-menu");
			return;
		}

		if (activeMenu == null) {
			activeMenu = menu.getComponents().get(menu.getComponents().size() - 1);
		}

		if (levelBegin > 1) {
			getDefaultMenu();
			getLevelComponents(menu, 1);
			for (int i = 0; i < levelComponents.size(); i++) {
				MenuComponent component = levelComponents.get(i);
				if (component.isActive(activeMenuBranch)) {
					displayComponents(component, levelBegin);
				}
			}
		} else {
			displayComponents(menu, levelBegin);
		}

    }

	private void getDefaultMenu() {
		if (activeMenu.getLevel() == 1) {
			MenuComponent[] comp = activeMenu.getMenuComponents();
			int l = comp.length;
			if (l > 0) {
				addLevelComponent(activeMenu, 2);
			}
		}
	}

    private boolean addLevelComponent(MenuComponent menu, int level) {
		MenuComponent component = menu.getByIndex(0);
		if (component != null) {
			addToActiveMenuBranch(component);
			if (levelEnd != null && level == levelEnd) {
				return true;
			} else {
				if (addLevelComponent(component, level + 1)) {
					return true;
				}
			}
		}
		return false;
    }

	private boolean getLevelComponents(MenuComponent menu, int level) {
		if (menu.getLevel() < levelBegin - 1) {
			MenuComponent[] components = menu.getMenuComponents();
			for (MenuComponent component : components) {
				if (getLevelComponents(component, level + 1)) {
					return true;
				}
			}
		} else if (menu.getLevel() == levelBegin - 1) {
			levelComponents.put(menu.getIndex(), menu);
			if (menu.isActive(activeMenuBranch)) {
				return true;
			}
		}
		return false;
	}

    protected void displayComponents(MenuComponent menu, int level) throws JspException, IOException {

        MenuComponent[] components = menu.getMenuComponents();

		if (components.length == 0 || (levelEnd != null && level > levelEnd)) {
			return;
		}

		if (level == 1 || level == 2) {
			displayTabLevel(menu, level);
			for (MenuComponent component : components) {
				if (component.isActive(activeMenuBranch)) {
					displayComponents(component, level + 1);
					return;
				}
			}
//			displayComponents(components[level == 1 ? components.length - 1 : 0], level + 1);
			return;
		}

		for (MenuComponent component : components) {
			if (level == 3) {
				if (component.isFirst()) {
					out.println(displayStrings.getMessage("left.menu.top", getTitle(menu)));
				}
				out.println(displayStrings.getMessage("left.menu.top_item.top", getTitle(component)));
			}
			if (component.getMenuComponents().length > 0) {
				displayComponents(component, level + 1);
			} else {
				if (component.getRequiredAuthority() == null || rolesGranted.contains(component.getRequiredAuthority())) {
					out.println(displayStrings.getMessage("left.menu.item", component.getUrl() != null ? component.getUrl() : component.getAction(),
							getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
				}
			}
			if (level == 3) {
				out.println(displayStrings.getMessage("left.menu.top_item.bottom"));
			}
		}

		if (level == 3) {
			out.println(displayStrings.getMessage("left.menu.bottom"));
		}

    }

	public void displayTabLevel(MenuComponent menu, int level) throws IOException {

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".top"));

        int len = menu.getMenuComponents().length;
        int i = 1;

		for (MenuComponent component : menu.getMenuComponents()) {
			if (component.getRequiredAuthority() == null || rolesGranted.contains(component.getRequiredAuthority())) {
				if (level == 1) {
					if ((activeMenuBranch == null && i == len) || (activeMenuBranch != null && component.isActive(activeMenuBranch))) {
						out.println(displayStrings.getMessage("tab.menu.level1_active.item", getTitle(component)));
					} else {
						out.println(displayStrings.getMessage("tab.menu.level1.item", component.getUrl() != null ? component.getUrl() : component.getAction(),
								getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
					}
				} else {
					out.println(displayStrings.getMessage("tab.menu.level" + level + ".item", component.getUrl() != null ? component.getUrl() : component.getAction(),
							getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
				}
			}
            i++;
		}

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".bottom"));

	}

}
