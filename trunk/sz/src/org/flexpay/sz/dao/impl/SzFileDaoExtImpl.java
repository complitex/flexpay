package org.flexpay.sz.dao.impl;

import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.sz.dao.SzFileDaoExt;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;

public class SzFileDaoExtImpl extends HibernateDaoSupport implements SzFileDaoExt {

	@Override
	public void updateStatus(@NotNull final Collection<Long> fileIds, @NotNull final FPFileStatus status) {

		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				return session.createQuery("update SzFile f set f.status.id = :statusId " +
										   "where f.id in (:ids)")
						.setLong("statusId", status.getId())
						.setParameterList("ids", fileIds)
						.executeUpdate();
			}
		});
	}

}
