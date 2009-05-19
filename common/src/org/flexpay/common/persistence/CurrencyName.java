package org.flexpay.common.persistence;

public class CurrencyName extends Translation {

	private String shortName;
	private String fractionName;
	private String shortFractionName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFractionName() {
		return fractionName;
	}

	public void setFractionName(String fractionName) {
		this.fractionName = fractionName;
	}

	public String getShortFractionName() {
		return shortFractionName;
	}

	public void setShortFractionName(String shortFractionName) {
		this.shortFractionName = shortFractionName;
	}
}
