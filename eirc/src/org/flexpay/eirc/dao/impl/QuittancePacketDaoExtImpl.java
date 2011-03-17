package org.flexpay.eirc.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.QuittancePacketDaoExt;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class QuittancePacketDaoExtImpl extends HibernateDaoSupport implements QuittancePacketDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@NotNull
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<QuittancePacket> findPackets(ArrayStack filters, final Page<QuittancePacket> pager) {

		final StringBuilder hql = new StringBuilder();
		final StringBuilder hqlCount = new StringBuilder();

		hql.append("from QuittancePacket");
		hqlCount.append("select count(*) from QuittancePacket");

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {

				Long count = (Long) session.createQuery(hqlCount.toString()).uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.list();
			}
		});
	}

	@NotNull
	@Override
	public Long nextPacketNumber() {
		Object[] result = (Object[]) uniqueResult((List<?>) getHibernateTemplate().
                findByNamedQuery("QuittancePacket.nextPacketNumber"));

		log.debug("Next packet result: {}, {}", result);

		Long maxId = result[0] == null ? 0L : (Long) result[0];
		Long maxNumber = result[1] == null ? 0L : (Long) result[1];

		return maxId.compareTo(maxNumber) > 0 ? maxId + 1L : maxNumber + 1L;
	}
}
