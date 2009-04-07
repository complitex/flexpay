package net.sourceforge.navigator.util;

import net.sourceforge.navigator.menu.MenuComponent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class MenuUtils {

	@SuppressWarnings({"SuspiciousMethodCalls"})
	public static MenuComponent getMenuByName(String menuName, MenuComponent menu) {
		if (menu.getComponents().isEmpty()) {
			return null;
		}
		for (MenuComponent menuComponent : menu.getComponents()) {
			if (menuComponent.getName().equals(menuName)) {
				return menuComponent;
			}
			MenuComponent c = getMenuByName(menuName, menuComponent);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	public static MenuComponent getMenuByAction(String action, ApplicationContext context) {
		if (action == null) {
			return null;
		}
		Map<?, ?> menuComponentBeans = context.getBeansOfType(MenuComponent.class);
		for (Object value : menuComponentBeans.values()) {
			MenuComponent menuComponent = (MenuComponent) value;
			if (action.equals(menuComponent.getAction())) {
				return menuComponent;
			}
		}
		return null;
	}

}
