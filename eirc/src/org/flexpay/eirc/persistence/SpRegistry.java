package org.flexpay.eirc.persistence;

import java.util.Date;

import org.flexpay.common.persistence.DomainObject;

public class SpRegistry extends DomainObject {
	
	private SpFile spFile;
	
	private Long registryNum;
	private SpRegistryType registryType;
	private Long recordNum;
	private Date creationDate;
	private Date fromDate;
	private Date tillDate;
	private Long sender;
	private Long recipient;
	private Double sum;
	private String containers;
	
	/**
	 * @return the spFile
	 */
	public SpFile getSpFile() {
		return spFile;
	}
	/**
	 * @param spFile the spFile to set
	 */
	public void setSpFile(SpFile spFile) {
		this.spFile = spFile;
	}
	
	/**
	 * @return the registryType
	 */
	public SpRegistryType getRegistryType() {
		return registryType;
	}
	/**
	 * @param registryType the registryType to set
	 */
	public void setRegistryType(SpRegistryType registryType) {
		this.registryType = registryType;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the tillDate
	 */
	public Date getTillDate() {
		return tillDate;
	}
	/**
	 * @param tillDate the tillDate to set
	 */
	public void setTillDate(Date tillDate) {
		this.tillDate = tillDate;
	}
	/**
	 * @return the sender
	 */
	public Long getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(Long sender) {
		this.sender = sender;
	}
	/**
	 * @return the recipient
	 */
	public Long getRecipient() {
		return recipient;
	}
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(Long recipient) {
		this.recipient = recipient;
	}
	/**
	 * @return the sum
	 */
	public Double getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(Double sum) {
		this.sum = sum;
	}
	/**
	 * @return the containers
	 */
	public String getContainers() {
		return containers;
	}
	/**
	 * @param containers the containers to set
	 */
	public void setContainers(String containers) {
		this.containers = containers;
	}
	/**
	 * @return the registryNum
	 */
	public Long getRegistryNum() {
		return registryNum;
	}
	/**
	 * @param registryNum the registryNum to set
	 */
	public void setRegistryNum(Long registryNum) {
		this.registryNum = registryNum;
	}
	/**
	 * @return the recordNum
	 */
	public Long getRecordNum() {
		return recordNum;
	}
	/**
	 * @param recordNum the recordNum to set
	 */
	public void setRecordNum(Long recordNum) {
		this.recordNum = recordNum;
	}
	
	

}
