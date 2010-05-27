package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;

/**
 * Updates container holds a set of delayed updates
 */
public class DelayedUpdatesContainer implements DelayedUpdate {
	private Set<DelayedUpdate> updatesSet = new LinkedHashSet<DelayedUpdate>();

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {

		for (DelayedUpdate update : updatesSet) {
			update.doUpdate();
		}
	}

	public Set<DelayedUpdate> getUpdates() {
		return updatesSet;
	}

	public void addUpdate(DelayedUpdate update) {
		// dump container elements if needed
		if (update instanceof DelayedUpdatesContainer) {
			DelayedUpdatesContainer container = (DelayedUpdatesContainer) update;
			for (DelayedUpdate element : container.getUpdates()) {
				addUpdate(element);
			}
		}

		// if update already there nothing to do, else save operations order
		if (!updatesSet.contains(update)) {
			updatesSet.add(update);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this && obj instanceof DelayedUpdatesContainer;
	}
}
