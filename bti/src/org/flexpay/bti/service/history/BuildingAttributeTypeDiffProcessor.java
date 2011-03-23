package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.DiffProcessor;

public class BuildingAttributeTypeDiffProcessor implements DiffProcessor<BuildingAttributeType> {

	/**
	 * Process creation diff
	 *
	 * @param obj  Created object
	 * @param diff Diff
	 */
	@Override
	public void onCreate(BuildingAttributeType obj, Diff diff) {
		diff.setObjectTypeName(obj.getClass().getName());
	}

	/**
	 * Process update diff
	 *
	 * @param obj1 Old object version
	 * @param obj2 new object version
	 * @param diff Diff of two versions
	 */
	@Override
	public void onUpdate(BuildingAttributeType obj1, BuildingAttributeType obj2, Diff diff) {
	}

	/**
	 * Process deletion diff
	 *
	 * @param obj  Deleted object
	 * @param diff Diff
	 */
	@Override
	public void onDelete(BuildingAttributeType obj, Diff diff) {
	}
}
