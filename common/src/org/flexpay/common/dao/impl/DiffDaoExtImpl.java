package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.DiffDaoExt;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

public class DiffDaoExtImpl extends JpaDaoSupport implements DiffDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@NotNull
	@SuppressWarnings ({"unchecked"})
    @Override
	public List<Diff> findNewHistoryRecords(final FetchRange range) {

		if (!range.wasInitialized()) {
			Object[] stats = (Object[]) DataAccessUtils.uniqueResult(
					getJpaTemplate().findByNamedQuery("Diff.findNewRecords.stats"));
			range.setMinId((Long) stats[0]);
			range.setMaxId((Long) stats[1]);
			range.setCount(((Long) stats[2]).intValue());
			range.setLowerBound(range.getMinId());
			range.setUpperBound(range.getLowerBound() != null ? range.getLowerBound() + range.getPageSize() : null);

			log.debug("initialized range: {}", range);
		}

		if (!range.wasInitialized()) {
			log.debug("No records in range");
			return Collections.emptyList();
		}

		Object[] params = {range.getLowerBound(), range.getUpperBound()};
		return getJpaTemplate().findByNamedQuery("Diff.findNewRecords", params);
	}

    @Override
	public boolean hasDiffs(Long objectId, int objectType) {

		if (log.isDebugEnabled()) {
			log.debug("Checking for history existence, type 0x{}, id: {}",
					Integer.toHexString(objectType), objectId);
		}

		Object[] params = {objectId, objectType};
		List<?> result = getJpaTemplate().findByNamedQuery("Diff.hasHistory", params);
		return !result.isEmpty();
	}

	@Override
	public void removeDiffs(final int processingStatus) {
		getJpaTemplate().execute(new JpaCallback<Object>() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.createNamedQuery("Diff.deleteRecordsByDiffStatus")
						.setParameter(1, processingStatus)
						.executeUpdate();
				entityManager.createNamedQuery("Diff.deleteByDiffStatus")
						.setParameter(1, processingStatus)
						.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public void updateDiffsProcessingStatus(final int statusOld, final int statusNew) {
		getJpaTemplate().execute(new JpaCallback<Object>() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.createNamedQuery("Diff.updateStatus")
						.setParameter(1, statusNew) // set clause
						.setParameter(2, statusOld) // where clause
						.executeUpdate();
				return null;
			}
		});
	}
}
