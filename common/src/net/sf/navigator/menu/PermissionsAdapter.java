package net.sf.navigator.menu;

/**
 * Defines a pluggable adapter into the menu framework that is used for 
 * checking permissions on menus. 
 */
public interface PermissionsAdapter {
    
    /**
     * If the menu is allowed, this should return true.
     *
     * @param menu menu
	 * @return whether or not the menu is allowed.
     */
    public boolean isAllowed(MenuComponent menu);
    
}
