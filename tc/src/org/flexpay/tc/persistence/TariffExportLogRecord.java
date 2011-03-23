package org.flexpay.tc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.DomainObject;

import java.util.Date;

public class TariffExportLogRecord extends DomainObject{
	
	private TariffExportCode tariffExportCode;
	private Date exportdate;
	private Date tariffBeginDate;
	private Building building;
	private Tariff tariff;


	public TariffExportCode getTariffExportCode() {
		return tariffExportCode;
	}

	public void setTariffExportCode(TariffExportCode tariffExportCode) {
		this.tariffExportCode = tariffExportCode;
	}

	public Date getExportdate() {
		return exportdate;
	}

	public void setExportdate(Date exportdate) {
		this.exportdate = exportdate;
	}

	public Date getTariffBeginDate() {
		return tariffBeginDate;
	}

	public void setTariffBeginDate(Date tariffBeginDate) {
		this.tariffBeginDate = tariffBeginDate;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("tariffExportCode", tariffExportCode == null?"-":tariffExportCode.toString()).
				append("exportdate", exportdate).
				append("tariffBeginDate", tariffBeginDate).
				toString();
	}
}
