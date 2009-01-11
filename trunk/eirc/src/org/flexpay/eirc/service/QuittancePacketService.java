package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.QuittancePacket;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.commons.collections.ArrayStack;

import java.util.List;

public interface QuittancePacketService {

	/**
	 * Read full quittance packet details
	 *
	 * @param stub Quittance packet stub
	 * @return packet if found, or <code>null</code> otherwise
	 */
	@Nullable
	QuittancePacket read(@NotNull Stub<QuittancePacket> stub);

	/**
	 * Create a new Quittance packet
	 * @param packet QuittancePacket to create
	 * @return persisted packet
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	QuittancePacket create(@NotNull QuittancePacket packet) throws FlexPayExceptionContainer;

	/**
	 * Update a Quittance packet
	 * @param packet QuittancePacket to update
	 * @return packet back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	QuittancePacket update(@NotNull QuittancePacket packet) throws FlexPayExceptionContainer;

	/**
	 * List quittance packets
	 *
	 * @param filters stack of filters
	 * @param pager Pager
	 * @return packets
	 */
	@NotNull
	List<QuittancePacket> listPackets(ArrayStack filters, Page<QuittancePacket> pager);

	/**
	 * Suggest new quittance packet number
	 *
	 * @return new packet number
	 */
	@NotNull
	Long suggestPacketNumber();
}
