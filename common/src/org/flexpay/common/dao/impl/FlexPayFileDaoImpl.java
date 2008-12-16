package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.FlexPayFileDao;
import org.flexpay.common.persistence.FlexPayFile;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

public class FlexPayFileDaoImpl extends SimpleJdbcDaoSupport implements FlexPayFileDao {

    private HibernateTemplate hibernateTemplate;

    /**
     * Create or update flexpay file
     *
     * @param file flexpay file
     *
     * @return created or updated flexPayFile
     */
    public FlexPayFile save(@NotNull FlexPayFile file) {
        return (FlexPayFile) hibernateTemplate.merge(file);
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public FlexPayFile read(@NotNull Long id) {
        List list = hibernateTemplate.find("from FlexPayFile where id = ?", id);
        return list.isEmpty() ? null : (FlexPayFile) list.iterator().next();
    }

    public void delete(@NotNull FlexPayFile file) {
        hibernateTemplate.delete(file);
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

}
