package org.flexpay.common.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.ImportErrorDaoExt;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.util.Collection;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ImportErrorDaoExtImpl extends SimpleJdbcDaoSupport implements ImportErrorDaoExt {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void disableErrors(Collection<Long> errorIds) {

        final List<Object> params = list();
        params.add(DomainObjectWithStatus.STATUS_DISABLED);
        params.add(errorIds);

        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }

        String idsList = StringUtils.join(errorIds, ", ");

        int updated = getSimpleJdbcTemplate().update(
                "update common_import_errors_tbl ie set ie.status=1 where ie.id in (" + idsList + ")");

        log.debug("Disabled {} errors", updated);
        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Time spent for disable errors query: {}", watch);
        }

    }

}
