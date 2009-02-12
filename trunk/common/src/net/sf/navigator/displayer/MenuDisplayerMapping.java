/*
 * MenuDisplayerMapping.java
 *
 * Created on February 6, 2001, 5:24 PM
 */
package net.sf.navigator.displayer;

import java.io.Serializable;

/**
 *
 * @author  ssayles
 * @version 1.0
 */
public class MenuDisplayerMapping implements Serializable {

    /** Holds value of property name. */
    private String name;

    /** Holds value of property type. */
    private String type;

    /** Holds value of property config. */
    private String config;

    /**
     * Getter for property name
     *
     * @return Value of property name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property name
     *
     * @param name New value of property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property type
     *
     * @return Value of property type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for property type
     *
     * @param type New value of property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for property config
     *
     * @return Value of property config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Setter for property config
     *
     * @param config New value of property config
     */
    public void setConfig(String config) {
        this.config = config;
    }

}
