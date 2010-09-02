package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;

import java.util.Collection;
import java.util.List;

public interface ConsumerAttributeTypeDao extends GenericDao<ConsumerAttributeTypeBase, Long> {

    List<ConsumerAttributeTypeBase> findAtributeTypeByCodes(Collection<String> codes);

	List<ConsumerAttributeTypeBase> listAttributeTypes(Page<ConsumerAttributeTypeBase> pager);

	List<ConsumerAttributeTypeBase> listAttributeTypes();
}
