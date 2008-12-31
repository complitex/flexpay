package org.flexpay.eirc.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface QuittancePacketDaoExt {

	@NotNull
	List<QuittancePacket> findPackets(ArrayStack filters, Page<QuittancePacket> pager);
}