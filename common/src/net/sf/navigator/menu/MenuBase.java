package net.sf.navigator.menu;

import org.springframework.beans.factory.annotation.Required;

public abstract class MenuBase {

    protected String action;
    protected String location;

    /** Align menu 'left','right','top','bottom' ...and other alignment of particular menu system */
    protected String align;
    
    /** Holds value of property altImage. */
    protected String altImage;

    /** Holds value of property description. */
    protected String description;

    /** Holds value of property height. */
    protected String height;

    /** Holds value of property image. */
    protected String image;

    /** Holds value of property name. */
    protected String name;

    /** Holds value of property onclick. */
    protected String onclick;

    /** Holds value of property ondblclick. */
    protected String ondblclick;

    /** Holds value of property onmouseout. */
    protected String onmouseout;

    /** Holds value of property onmouseover. */
    protected String onmouseover;

    /** Holds value of property roles. */
    protected String roles;

    /** Holds value of property target. */
    protected String target;

    /** Holds value of property title. */
    protected String title;

    /** Holds value of property toolTip. */
    protected String toolTip;

    /** Holds value of property width. */
    protected String width;

    /** Holds parsed (with variables) url that is used to render a link */
    private String url;

    /** Holds value of property onContextMenu */
    protected String onContextMenu;

    /**
     * Holds value of property module; a Struts module prefix that overrides the current module.
     *
     * <p>The default module is specified by <code>""</code>.  Any non-default module should begin with <code>"/"</code>.
     */
    protected String module;

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public String getAlign() {
        return align;
    }
    
    public void setAlign(String align) {
        this.align = align;
    }
    
    public void setAltImage(String altImage) {
        this.altImage = altImage;
    }

    public String getAltImage() {
        return altImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

	@Required
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnmouseout() {
        return onmouseout;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmouseover() {
        return onmouseover;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public String getOnContextMenu() {
        return onContextMenu;
    }

    public void setOnContextMenu(String string) {
        onContextMenu = string;
    }

    public String getOndblclick() {
        return ondblclick;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
