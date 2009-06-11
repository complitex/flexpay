package org.flexpay.common.persistence.history.builder;

import org.flexpay.common.persistence.DomainObject;

/**
 * Callback interface for {@link HistoryBuilderHelper#buildReferenceDiff(org.flexpay.common.persistence.DomainObject,
 * org.flexpay.common.persistence.DomainObject, org.flexpay.common.persistence.history.Diff, ReferenceExtractor)}
 *
 * @param <Ref> Reference type
 * @param <DO> Object type
 */
public interface ReferenceExtractor<Ref extends DomainObject, DO extends DomainObject> {

	/**
	 * Extract needed reference from an object
	 *
	 * @param obj Object to extract reference from
	 * @return Reference
	 */
	Ref getReference(DO obj);

	/**
	 * Get reference field code
	 *
	 * @return field code
	 */
	int getReferenceField();
}

