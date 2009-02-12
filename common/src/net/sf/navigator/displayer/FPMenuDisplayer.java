package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FPMenuDisplayer extends MessageResourcesMenuDisplayer {

	private List<MenuComponent> levelComponents = new ArrayList<MenuComponent>();

    public void display(MenuComponent menu) throws JspException, IOException {
		if (levelBegin == null) {
			levelBegin = 1;
		}

		menu = menu.getMenuComponents()[0];

		log.debug("---------------------------------------------");
		log.debug("levelBegin = {}, levelEnd = {}", levelBegin, levelEnd);

		if (levelBegin > 1) {
			getLevelComponents(menu, levelBegin);
			for (MenuComponent component : levelComponents) {
				displayComponents(component, levelBegin);
			}
		} else {
			displayComponents(menu, levelBegin);
		}

    }

	private void getLevelComponents(MenuComponent menu, int level) {
		log.debug("menu.getLevel() = {}, level = {}", menu.getLevel(), level);
		if (menu.getLevel() < level) {
			MenuComponent[] components = menu.getMenuComponents();
			for (MenuComponent component : components) {
				getLevelComponents(component, level + 1);
			}
		} else if (menu.getLevel() == level) {
			levelComponents.add(menu);
		}
	}

    protected void displayComponents(MenuComponent menu, int level) throws JspException, IOException {
        MenuComponent[] components = menu.getMenuComponents();

		log.debug("level = {}", level);
		log.debug("components.length = {}", components.length);

		if (components.length == 0 || (levelEnd != null && level > levelEnd)) {
			log.debug("level = {}, levelEnd = {}. Exiting...", level, levelEnd);
			return;
		}

		log.debug("components[0].getTitle() = {}", components[0].getTitle());

		if (level == 1) {
			displayTabLevel(menu, level);
			log.debug("menu.getMenuComponents()[1] = {}", menu.getMenuComponents()[0], menu.getMenuComponents()[1]);
			displayComponents(menu.getMenuComponents()[1], level + 1);
			return;
		} else if (level == 2) {
			displayTabLevel(menu, level);
			log.debug("menu.getMenuComponents()[0] = {}", menu.getMenuComponents()[0], menu.getMenuComponents()[1]);
			displayComponents(menu.getMenuComponents()[0], level + 1);
			return;
		}

		if (menu.isFirst() && level == 3) {
			out.println(displayStrings.getMessage("left.menu.top", getMessage(menu.getTitle())));
		}

		for (MenuComponent component : components) {
			if (level == 3) {
				out.println(displayStrings.getMessage("left.menu.top_item.top", getMessage(component.getTitle())));
			}
			if (component.getMenuComponents().length > 0) {
				displayComponents(component, level + 1);
			} else {
				out.println(displayStrings.getMessage("left.menu.item", component.getUrl() != null ? component.getUrl() : component.getAction(),
						getMenuTarget(component), getMenuToolTip(component), getMessage(component.getTitle())));
			}
		}
		if (level == 3) {
			out.println(displayStrings.getMessage("left.menu.top_item.bottom"));
			if (menu.isLast()) {
				out.println(displayStrings.getMessage("left.menu.bottom"));
			}
		}

    }

	public void displayTabLevel(MenuComponent menu, int level) throws IOException {

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".top"));

		for (MenuComponent component : menu.getMenuComponents()) {
			out.println(displayStrings.getMessage("tab.menu.level" + level + ".item", component.getUrl() != null ? component.getUrl() : component.getAction(),
					getMenuTarget(component), getMenuToolTip(component), getMessage(component.getTitle())));
		}

		out.println(displayStrings.getMessage("tab.menu.level" + level + ".bottom"));

	}

}
