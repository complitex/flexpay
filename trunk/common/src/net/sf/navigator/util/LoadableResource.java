package net.sf.navigator.util;

import javax.servlet.ServletContext;

/**
 * Defines the interface for a loadable web application resource
 */
public interface LoadableResource {

    public void setLoadParam(String loadParam);

    public String getLoadParam();

    public void setName(String name);

    public String getName();

    public void load() throws LoadableResourceException;

    public void reload() throws LoadableResourceException;

    public ServletContext getServletContext();

    public void setServletContext(ServletContext servletContext);

}
