package net.sourceforge.navigator.menu;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.list;

public class MenuComponent extends MenuBase implements Serializable, InitializingBean {

	public final static String ACTIVE_MENU = "activeMenuComponentName";

    protected static MenuComponent[] _menuComponent = new MenuComponent[0];

    protected List<MenuComponent> menuComponents = list();
    protected MenuComponent parent;
	protected String name;
	protected String userRole;
	private int level;
	private int index;
	protected MenuInform menuInform;
	protected GrantedAuthorityImpl requiredAuthority;

	public GrantedAuthorityImpl getRequiredAuthority() {
		return requiredAuthority;
	}

	public void setRequiredAuthority(GrantedAuthorityImpl requiredAuthority) {
		this.requiredAuthority = requiredAuthority;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
		if (StringUtils.isNotEmpty(userRole)) {
			requiredAuthority = new GrantedAuthorityImpl(userRole);
		}
	}

	public MenuComponent[] getMenuComponents() {
        return menuComponents.toArray(_menuComponent);
    }

    public void setMenuComponents(MenuComponent[] menuComponents) {
        this.menuComponents.addAll(Arrays.asList(menuComponents));
    }

    public void setParent(MenuComponent parent) {
        if (parent != null) {
            if (!parent.getComponents().contains(this)) {
                level = parent.getLevel() + 1;
                parent.getComponents().add(this);
            }
        }
        this.parent = parent;
    }

    public MenuComponent getParent() {
        return parent;
    }

    public List<MenuComponent> getComponents() {
        return menuComponents;
    }

	public boolean isFirst() {
		return index == 0;
	}

 	public boolean isLast() {
		return parent != null && index == parent.getComponents().size() - 1;
	}

    public MenuComponent getByIndex(int index) {
        for (MenuComponent menuComponent : menuComponents) {
            if (menuComponent.getIndex() == index) {
                return menuComponent;
            }
        }
        return null;
    }

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isActive(String menuName) {
		return name.equals(menuName);
	}

    public boolean isActive(Set<String> menuNames) {
        return menuNames != null && menuNames.contains(name);
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	@Required
    public void setName(String name) {
        this.name = name;
    }

	public MenuInform getMenuInform() {
		return menuInform;
	}

	@Required
	public void setMenuInform(MenuInform menuInform) {
		this.menuInform = menuInform;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		menuInform.addNameOfMenuComponent(name);
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("name", name).
				append("title", title).
				append("action", action).
				append("index", index).
				append("level", level).
				append("childs.size", menuComponents.size());
		if (parent != null) {
			sb.append("parent.name", parent.getName());
		}
		return sb.toString();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuComponent)) {
            return false;
        }

        MenuComponent that = (MenuComponent) o;

        return name.equals(that.getName());

    }

}
