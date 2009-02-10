package org.flexpay.tc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class TariffCalculationResult extends DomainObject {

	private BigDecimal value;
	private Date creationDate = new Date();
	private Date calculationDate;
	private Building building;
	private Tariff tariff;

	public TariffCalculationResult(){
	}

	public TariffCalculationResult(@NotNull BigDecimal value, @NotNull Date creationDate, @NotNull Date calculationDate, @NotNull Building building, @NotNull Tariff tariff) {
		this.value = value;
		this.creationDate = creationDate;
		this.calculationDate = calculationDate;
		this.building = building;
		this.tariff = tariff;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCalculationDate() {
		return calculationDate;
	}

	public void setCalculationDate(Date calculationDate) {
		this.calculationDate = calculationDate;
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
				append("TariffCalculationResult {").
				append("id", getId()).
				append("value", value).
				append("creationDate", creationDate).
				append("calculationDate", calculationDate).
				append("}").toString();
	}

}
