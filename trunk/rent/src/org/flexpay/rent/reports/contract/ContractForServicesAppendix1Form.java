package org.flexpay.rent.reports.contract;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

public class ContractForServicesAppendix1Form implements Cloneable, Serializable {
	
	private String contractNumber;
	private Date contractDate;
	private Date beginDate;
	private String renter;
	private String address;

	private String centralHeatingSP;
	private String centralHeatingSPAccount;
	private BigDecimal centralHeatingHeatedSquare = BigDecimal.ZERO;
	private Integer centralHeatingDesignLoad;

	private String hotWaterSP;
	private String hotWaterSPAccount;
	private Integer hotWaterPercent;
	private Integer hotWaterDesignLoad;

	private String coldWaterSP;
	private String coldWaterSPAccount;
	private Integer coldWaterPercent;
	private Integer coldWaterSize;

	private String waterHeaterSP;
	private String waterHeaterSPAccount;
	private Integer waterHeaterPercent;
	private Integer waterHeaterSize;

	public Map<String, ?> getParams() {
		return map(ar("contractNumber",
					"contractDate",
					"beginDate",
					"renter",
					"address",
					"centralHeatingSP",
					"centralHeatingSPAccount",
					"centralHeatingHeatedSquare",
					"centralHeatingDesignLoad",
					"hotWaterSP",
					"hotWaterSPAccount",
					"hotWaterPercent",
					"hotWaterDesignLoad",
					"coldWaterSP",
					"coldWaterSPAccount",
					"coldWaterPercent",
					"coldWaterSize",
					"waterHeaterSP",
					"waterHeaterSPAccount",
					"waterHeaterPercent",
					"waterHeaterSize"),
				ar(contractNumber,
					contractDate,
					beginDate,
					renter,
					address,
					centralHeatingSP,
					centralHeatingSPAccount,
					centralHeatingHeatedSquare,
					centralHeatingDesignLoad,
					hotWaterSP,
					hotWaterSPAccount,
					hotWaterPercent,
					hotWaterDesignLoad,
					coldWaterSP,
					coldWaterSPAccount,
					coldWaterPercent,
					coldWaterSize,
					waterHeaterSP,
					waterHeaterSPAccount,
					waterHeaterPercent,
					waterHeaterSize
				));
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCentralHeatingSP() {
		return centralHeatingSP;
	}

	public void setCentralHeatingSP(String centralHeatingSP) {
		this.centralHeatingSP = centralHeatingSP;
	}

	public String getCentralHeatingSPAccount() {
		return centralHeatingSPAccount;
	}

	public void setCentralHeatingSPAccount(String centralHeatingSPAccount) {
		this.centralHeatingSPAccount = centralHeatingSPAccount;
	}

	public BigDecimal getCentralHeatingHeatedSquare() {
		return centralHeatingHeatedSquare;
	}

	public void setCentralHeatingHeatedSquare(BigDecimal centralHeatingHeatedSquare) {
		this.centralHeatingHeatedSquare = centralHeatingHeatedSquare;
	}

	public Integer getCentralHeatingDesignLoad() {
		return centralHeatingDesignLoad;
	}

	public void setCentralHeatingDesignLoad(Integer centralHeatingDesignLoad) {
		this.centralHeatingDesignLoad = centralHeatingDesignLoad;
	}

	public String getHotWaterSP() {
		return hotWaterSP;
	}

	public void setHotWaterSP(String hotWaterSP) {
		this.hotWaterSP = hotWaterSP;
	}

	public String getHotWaterSPAccount() {
		return hotWaterSPAccount;
	}

	public void setHotWaterSPAccount(String hotWaterSPAccount) {
		this.hotWaterSPAccount = hotWaterSPAccount;
	}

	public Integer getHotWaterPercent() {
		return hotWaterPercent;
	}

	public void setHotWaterPercent(Integer hotWaterPercent) {
		this.hotWaterPercent = hotWaterPercent;
	}

	public Integer getHotWaterDesignLoad() {
		return hotWaterDesignLoad;
	}

	public void setHotWaterDesignLoad(Integer hotWaterDesignLoad) {
		this.hotWaterDesignLoad = hotWaterDesignLoad;
	}

	public String getColdWaterSP() {
		return coldWaterSP;
	}

	public void setColdWaterSP(String coldWaterSP) {
		this.coldWaterSP = coldWaterSP;
	}

	public String getColdWaterSPAccount() {
		return coldWaterSPAccount;
	}

	public void setColdWaterSPAccount(String coldWaterSPAccount) {
		this.coldWaterSPAccount = coldWaterSPAccount;
	}

	public Integer getColdWaterPercent() {
		return coldWaterPercent;
	}

	public void setColdWaterPercent(Integer coldWaterPercent) {
		this.coldWaterPercent = coldWaterPercent;
	}

	public Integer getColdWaterSize() {
		return coldWaterSize;
	}

	public void setColdWaterSize(Integer coldWaterSize) {
		this.coldWaterSize = coldWaterSize;
	}

	public String getWaterHeaterSP() {
		return waterHeaterSP;
	}

	public void setWaterHeaterSP(String waterHeaterSP) {
		this.waterHeaterSP = waterHeaterSP;
	}

	public String getWaterHeaterSPAccount() {
		return waterHeaterSPAccount;
	}

	public void setWaterHeaterSPAccount(String waterHeaterSPAccount) {
		this.waterHeaterSPAccount = waterHeaterSPAccount;
	}

	public Integer getWaterHeaterPercent() {
		return waterHeaterPercent;
	}

	public void setWaterHeaterPercent(Integer waterHeaterPercent) {
		this.waterHeaterPercent = waterHeaterPercent;
	}

	public Integer getWaterHeaterSize() {
		return waterHeaterSize;
	}

	public void setWaterHeaterSize(Integer waterHeaterSize) {
		this.waterHeaterSize = waterHeaterSize;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("contractNumber", contractNumber).
				append("contractDate", contractDate).
				append("beginDate", beginDate).
				append("renter", renter).
				append("address", address).
				append("centralHeatingSP", centralHeatingSP).
				append("centralHeatingSPAccount", centralHeatingSPAccount).
				append("centralHeatingHeatedSquare", centralHeatingHeatedSquare).
				append("centralHeatingDesignLoad", centralHeatingDesignLoad).
				append("hotWaterSP", hotWaterSP).
				append("hotWaterSPAccount", hotWaterSPAccount).
				append("hotWaterPercent", hotWaterPercent).
				append("hotWaterDesignLoad", hotWaterDesignLoad).
				append("coldWaterSP", coldWaterSP).
				append("coldWaterSPAccount", coldWaterSPAccount).
				append("coldWaterPercent", coldWaterPercent).
				append("coldWaterSize", coldWaterSize).
				append("waterHeaterSP", waterHeaterSP).
				append("waterHeaterSPAccount", waterHeaterSPAccount).
				append("waterHeaterPercent", waterHeaterPercent).
				append("waterHeaterSize", waterHeaterSize).
				toString();
	}

}
