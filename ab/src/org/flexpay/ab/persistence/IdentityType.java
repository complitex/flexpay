package org.flexpay.ab.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class IdentityType  implements java.io.Serializable {

    private Long id;
    private Set<IdentityTypeName> identityTypeNames = Collections.emptySet();
    private List<PersonIdentity> personIdentities = Collections.emptyList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<IdentityTypeName> getIdentityTypeNames() {
        return identityTypeNames;
    }

    public void setIdentityTypeNames(Set<IdentityTypeName> identityTypeNames) {
        this.identityTypeNames = identityTypeNames;
    }

    public List<PersonIdentity> getPersonIdentities() {
        return personIdentities;
    }

    public void setPersonIdentities(List<PersonIdentity> personIdentities) {
        this.personIdentities = personIdentities;
    }
}


