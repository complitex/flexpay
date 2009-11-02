package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.DiffDaoExt;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.history.Diff;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collections;
import java.util.List;

public class DiffDaoExtImpl extends HibernateDaoSupport implements DiffDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@NotNull
	@SuppressWarnings ({"unchecked"})
	public List<Diff> findNewHistoryRecords(final FetchRange range) {

		if (!range.wasInitialized()) {
			Object[] stats = (Object[]) DataAccessUtils.uniqueResult(
					getHibernateTemplate().findByNamedQuery("Diff.findNewRecords.stats"));
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
		return getHibernateTemplate().findByNamedQuery("Diff.findNewRecords", params);
	}

	public boolean hasDiffs(Long objectId, int objectType) {

		log.debug("Checking for history existence, type #{}, id: {}", objectType, objectId);

		Object[] params = {objectId, objectType};
		List<?> result = getHibernateTemplate().findByNamedQuery("Diff.hasHistory", params);
		return !result.isEmpty();
	}
}
