package org.flexpay.eirc.dao.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.QuittancePacketDaoExt;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class QuittancePacketDaoExtImpl extends HibernateDaoSupport implements QuittancePacketDaoExt {

	@NotNull
	@SuppressWarnings ({"unchecked"})
	public List<QuittancePacket> findPackets(ArrayStack filters, final Page<QuittancePacket> pager) {

		final StringBuilder hql = new StringBuilder();
		final StringBuilder hqlCount = new StringBuilder();

		hql.append("from QuittancePacket");
		hqlCount.append("select count(*) from QuittancePacket");

		return getHibernateTemplate().executeFind(new HibernateCallback() {
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
}
