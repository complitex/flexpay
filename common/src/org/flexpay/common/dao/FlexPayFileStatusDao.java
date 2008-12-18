package org.flexpay.common.dao;

import org.flexpay.common.persistence.FlexPayFileStatus;

import java.util.List;

public interface FlexPayFileStatusDao extends GenericDao<FlexPayFileStatus, Long> {

    List<FlexPayFileStatus> listStatusesByName(String name);

}
