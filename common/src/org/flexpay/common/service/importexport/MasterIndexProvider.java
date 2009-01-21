package org.flexpay.common.service.importexport;

import org.flexpay.common.persistence.MasterIndexBounds;

/**
 * Index values provider
 */
public interface MasterIndexProvider {

	MasterIndexBounds getIndexBatch(int type, int number);
}
