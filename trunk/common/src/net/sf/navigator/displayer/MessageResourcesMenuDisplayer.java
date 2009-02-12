package net.sf.navigator.displayer;

import net.sf.navigator.menu.MenuComponent;
import org.apache.struts.util.MessageResources;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class MessageResourcesMenuDisplayer extends AbstractMenuDisplayer {

    //~ Instance fields ========================================================

    protected Object messages = null;
    protected Locale locale = null;

    //~ Methods ================================================================

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Object getMessageResources() {
        return messages;
    }

    public void setMessageResources(Object messages) {
        this.messages = messages;
    }

    /**
     * Get the title key from the bundle (if it exists).  This method
     * is public to expose it to Velocity.
     * 
     * @param key the key
     * @return message
     */
    @SuppressWarnings({"CastConflictsWithInstanceof"})
	public String getMessage(String key) {
        String message = null;

        if (messages != null && messages instanceof ResourceBundle) {
			log.debug("Looking up string '" + key + "' in ResourceBundle");
            ResourceBundle bundle = (ResourceBundle) messages;
            try {
                message = bundle.getString(key);
            } catch (MissingResourceException mre) {
                message = null;
            }
        } else if (messages != null) {
			log.debug("Looking up message '" + key + "' in Struts' MessageResources");
            // this is here to prevent a non-struts webapp from throwing a NoClassDefFoundError
            if ("org.apache.struts.util.PropertyMessageResources".equals(messages.getClass().getName())) {
                MessageResources resources = (MessageResources) messages;
                try {
                    message = locale != null ? resources.getMessage(locale, key) : resources.getMessage(key);
                } catch (Throwable t) {
                    message = null;
                }
            }
        } else {
            message = key;
        }

        if (message == null) {
            message = key;
        }

        return message;
    }

    /**
     * Get the menu's target (if it exists).  This method
     * is public to expose it to Velocity.
     *
     * @param menu menu
     * @return menuTarget
     */
    public String getMenuTarget(MenuComponent menu) {
        return this.target != null ? target : menu.getTarget() != null ? menu.getTarget() : MenuDisplayer._SELF;
    }

    /**
     * Get the menu's tooltip (if it exists).  This method
     * is public to expose it to Velocity
     *
     * @param menu menu
     * @return menuToolTip
     */
    public String getMenuToolTip(MenuComponent menu) {
        return menu.getToolTip() != null ? this.getMessage(menu.getToolTip()) : this.getMessage(menu.getTitle());
    }

    public String getMenuOnClick(MenuComponent menu) {
        if (menu.getOnclick() != null) {
            return " onclick=\"" + menu.getOnclick() + "\"";
        }
        return "";
    }

    public abstract void display(MenuComponent menu) throws JspException, IOException;

}
