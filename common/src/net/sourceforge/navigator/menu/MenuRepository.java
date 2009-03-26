package net.sourceforge.navigator.menu;

import net.sourceforge.navigator.displayer.MenuDisplayerMapping;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRepository implements Serializable {

    public static final String MENU_REPOSITORY_KEY = "menuRepository";

    protected String name;
    protected ApplicationContext applicationContext;
    protected MenuComponent menu = new MenuComponent();
    protected Map<String, MenuDisplayerMapping> displayers = new HashMap<String, MenuDisplayerMapping>();

	public MenuComponent getMenu() {
        return menu;
    }

    @Required
    public void setMenu(MenuComponent menu) {
        this.menu = menu;
    }

    public void setIndex(MenuComponent menu) {
		List<MenuComponent> components = menu.getComponents();
		if (components.isEmpty()) {
			return;
		}
		for (int i = 0; i < components.size(); i++) {
			components.get(i).setIndex(i);
			setIndex(components.get(i));
		}
	}


    public MenuDisplayerMapping getMenuDisplayer(String displayerName) {
        return displayers.get(displayerName);
    }

    public Map<String, MenuDisplayerMapping> getDisplayers() {
        return displayers;
    }

    @Required
    public void setDisplayers(Map<String, MenuDisplayerMapping> displayers) {
        this.displayers = displayers;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("MenuRepository {").
                append("name", name).
                append("menu", menu).
                append("}").toString();
    }

}
