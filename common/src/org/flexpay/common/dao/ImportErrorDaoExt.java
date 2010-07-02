package org.flexpay.common.dao;

import java.util.Collection;

public interface ImportErrorDaoExt {

    void disableErrors(Collection<Long> errorIds);

}
