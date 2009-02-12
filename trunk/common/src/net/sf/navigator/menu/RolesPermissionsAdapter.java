package net.sf.navigator.menu;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * This class used container-managed security to check access
 * to menus.  The roles are set in menu-config.xml
 */
public class RolesPermissionsAdapter implements PermissionsAdapter {

    private Pattern delimiters = Pattern.compile("(?<!\\\\),");
    private HttpServletRequest request;

    public RolesPermissionsAdapter(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * If the menu is allowed, this should return true.
     *
     * @return whether or not the menu is allowed.
     */
    public boolean isAllowed(MenuComponent menu) {
        if (menu.getRoles() == null) {
            return true; // no roles define, allow everyone
        }
		// Get the list of roles this menu allows
		String[] allowedRoles = delimiters.split(menu.getRoles());
		for (String allowedRole : allowedRoles) {
			if (request.isUserInRole(allowedRole)) {
				return true;
			}
		}
        return false;
    }

}
