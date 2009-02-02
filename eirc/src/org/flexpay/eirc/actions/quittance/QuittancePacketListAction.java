package org.flexpay.eirc.actions.quittance;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.eirc.service.QuittancePacketService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class QuittancePacketListAction extends FPActionWithPagerSupport<QuittancePacket> {

	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.currentMonth());

	private List<QuittancePacket> packets = Collections.emptyList();

	private QuittancePacketService quittancePacketService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		ArrayStack filters = CollectionUtils.arrayStack(beginDateFilter, getPager());
		packets = quittancePacketService.listPackets(filters, getPager());

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public List<QuittancePacket> getPackets() {
		return packets;
	}

	@Required
	public void setQuittancePacketService(QuittancePacketService quittancePacketService) {
		this.quittancePacketService = quittancePacketService;
	}

}
