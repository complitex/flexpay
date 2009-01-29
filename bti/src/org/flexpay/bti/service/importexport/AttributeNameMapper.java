package org.flexpay.bti.service.importexport;

import org.jetbrains.annotations.Nullable;

public interface AttributeNameMapper {

	/**
	 * Get name of the n-th attribute, returned <code>null</code> is for unknown position of attribute
	 *
	 * @param n Order of attribute to get name for
	 * @return Attribute name
	 */
	@Nullable
	String getName(int n);
}
