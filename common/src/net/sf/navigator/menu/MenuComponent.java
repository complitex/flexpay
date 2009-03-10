package net.sf.navigator.menu;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.GrantedAuthorityImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MenuComponent extends MenuBase implements Serializable {

    protected static MenuComponent[] _menuComponent = new MenuComponent[0];

    protected List<MenuComponent> menuComponents = new ArrayList<MenuComponent>();
    protected MenuComponent parent;
	protected String userRole;
	private int level;
	private int index;
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
        return menuNames.contains(name);
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
				append("MenuComponent {").
                append("name", getName()).
				append("title", title).
				append("action", action).
				append("index", index).
				append("level", level).
				append("childs.size", menuComponents.size());
		if (parent != null) {
			sb.append("parent.name", parent.getName());
		}
		return sb.append("}").toString();
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
