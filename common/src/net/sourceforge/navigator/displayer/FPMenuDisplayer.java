package net.sourceforge.navigator.displayer;

import net.sourceforge.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

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
			List<MenuComponent> components = menu.getComponents();
			for (int i = 1; i <= components.size(); i++) {
				MenuComponent m = components.get(components.size() - i);
				if (hasMenuAccess(m)) {
					activeMenu = m;
					break;
				}
			}
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
		if (level > 2) {
			return true;
		}
		MenuComponent component = null;
		List<MenuComponent> components = menu.getComponents();
		for (MenuComponent m : components) {
			if (hasMenuAccess(m)) {
				component = m;
				break;
			}
		}
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
			if (hasMenuAccess(component)) {
				if (level == 3) {
					if (component.isFirst()) {
						out.println(displayStrings.getMessage("left.menu.top", getTitle(menu)));
					}
					if (component.isActive(activeMenuBranch)) {
						out.println(displayStrings.getMessage("left.menu.top_item.top_active",
								component.getUrl() != null ? component.getUrl() : component.getAction(), getMenuToolTip(component), getTitle(component)));
					} else {
						out.println(displayStrings.getMessage("left.menu.top_item.top",
								component.getUrl() != null ? component.getUrl() : component.getAction(), getMenuToolTip(component), getTitle(component)));
					}
				}
				if (component.getMenuComponents().length > 0) {
					displayComponents(component, level + 1);
				} else {
					if (component.getRequiredAuthority() == null || rolesGranted.contains(component.getRequiredAuthority())) {
						if (component.isActive(activeMenuBranch)) {
							out.println(displayStrings.getMessage("left.menu.item_active", getTitle(component)));
						} else {
							out.println(displayStrings.getMessage("left.menu.item", component.getUrl() != null ? component.getUrl() : component.getAction(),
									getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
						}
					}
				}
				if (level == 3) {
					out.println(displayStrings.getMessage("left.menu.top_item.bottom"));
				}
			}
		}

		if (level == 3) {
			out.println(displayStrings.getMessage("left.menu.bottom"));
		}

    }

	private boolean hasMenuAccess(MenuComponent component) {
		return component.getParent() == null
				|| (component.getRequiredAuthority() == null || rolesGranted.contains(component.getRequiredAuthority()))
				&& hasMenuAccess(component.getParent());
	}

	public void displayTabLevel(MenuComponent menu, int level) throws IOException {

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".top"));

        int len = menu.getMenuComponents().length;
        int i = 1;

		for (MenuComponent component : menu.getMenuComponents()) {
			if (hasMenuAccess(component)) {
				if (level == 1) {
					if ((activeMenuBranch == null && i == len) || component.isActive(activeMenuBranch)) {
						out.println(displayStrings.getMessage("tab.menu.level1_active.item", getTitle(component)));
					} else {
						out.println(displayStrings.getMessage("tab.menu.level1.item", component.getUrl() != null ? component.getUrl() : component.getAction(),
								getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
					}
				} else {
					out.println(displayStrings.getMessage("tab.menu.level2" + (i == 1 ? ".first_item" : ".item"), 
							component.getUrl() != null ? component.getUrl() : component.getAction(),
							getMenuTarget(component), getMenuToolTip(component), getTitle(component)));
					if (i < len) {
						out.println(displayStrings.getMessage("tab.menu.level2.separator"));
					} else {
						out.println(displayStrings.getMessage("tab.menu.level2.last_item"));
					}
				}
			}
            i++;
		}

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".bottom"));

	}

}
