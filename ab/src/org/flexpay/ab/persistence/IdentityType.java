package org.flexpay.ab.persistence;

import java.util.Collections;
import java.util.List;

public class IdentityType  implements java.io.Serializable {

    private Long id;
    private List<IdentityTypeName> identityTypeNames = Collections.emptyList();
    private List<PersonIdentity> personIdentities = Collections.emptyList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IdentityTypeName> getIdentityTypeNames() {
        return identityTypeNames;
    }

    public void setIdentityTypeNames(List<IdentityTypeName> identityTypeNames) {
        this.identityTypeNames = identityTypeNames;
    }

    public List<PersonIdentity> getPersonIdentities() {
        return personIdentities;
    }

    public void setPersonIdentities(List<PersonIdentity> personIdentities) {
        this.personIdentities = personIdentities;
    }
}


