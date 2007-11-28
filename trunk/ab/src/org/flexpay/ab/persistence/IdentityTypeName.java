package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Language;

import java.io.Serializable;

public class IdentityTypeName implements Serializable {
    private Long id;
    private String name;
    private Language lang;
    private IdentityTypeName identityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public IdentityTypeName getIdentityName() {
        return identityName;
    }

    public void setIdentityName(IdentityTypeName identityName) {
        this.identityName = identityName;
    }
}
