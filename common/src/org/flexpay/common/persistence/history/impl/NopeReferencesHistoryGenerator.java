package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaTemplate;

public class NopeReferencesHistoryGenerator<T extends DomainObject> implements ReferencesHistoryGenerator<T> {

	@Override
	public void generateReferencesHistory(@NotNull T obj) {
		// do nothing
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
    }
}
