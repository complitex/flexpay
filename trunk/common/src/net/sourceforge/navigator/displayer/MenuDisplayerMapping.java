package net.sourceforge.navigator.displayer;

import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;

public class MenuDisplayerMapping implements Serializable {

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

	@Required
    public void setType(String type) {
        this.type = type;
    }

}
