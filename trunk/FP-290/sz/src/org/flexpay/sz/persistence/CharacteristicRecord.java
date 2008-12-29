package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Dwelling description
 *
 * TODO: refactor field names
 */
public class CharacteristicRecord extends Record {

	private Double cod;
	private Double cdpr;
	private Double ncard;
	private String idcode;
	private String pasp;
	private String fio;
	private String idpil;
	private String pasppil;
	private String fiopil;
	private Double idx;
	private Double cdul;
	private String house;
	private String build;
	private String apt;
	private Double vl;
	private Double plzag;
	private Double plopal;

	public Double getCod() {
		return cod;
	}

	public void setCod(Double cod) {
		this.cod = cod;
	}

	public Double getCdpr() {
		return cdpr;
	}

	public void setCdpr(Double cdpr) {
		this.cdpr = cdpr;
	}

	public Double getNcard() {
		return ncard;
	}

	public void setNcard(Double ncard) {
		this.ncard = ncard;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	public String getPasp() {
		return pasp;
	}

	public void setPasp(String pasp) {
		this.pasp = pasp;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getIdpil() {
		return idpil;
	}

	public void setIdpil(String idpil) {
		this.idpil = idpil;
	}

	public String getPasppil() {
		return pasppil;
	}

	public void setPasppil(String pasppil) {
		this.pasppil = pasppil;
	}

	public String getFiopil() {
		return fiopil;
	}

	public void setFiopil(String fiopil) {
		this.fiopil = fiopil;
	}

	public Double getCdul() {
		return cdul;
	}

	public void setCdul(Double cdul) {
		this.cdul = cdul;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getApt() {
		return apt;
	}

	public void setApt(String apt) {
		this.apt = apt;
	}

	public Double getVl() {
		return vl;
	}

	public void setVl(Double vl) {
		this.vl = vl;
	}

	public Double getPlzag() {
		return plzag;
	}

	public void setPlzag(Double plzag) {
		this.plzag = plzag;
	}

	public Double getPlopal() {
		return plopal;
	}

	public void setPlopal(Double plopal) {
		this.plopal = plopal;
	}

	public Double getIdx() {
		return idx;
	}

	public void setIdx(Double idx) {
		this.idx = idx;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("CharacteristicRecord {").
				append("id", getId()).
				append("cod", cod).
				append("cdpr", cdpr).
				append("ncard", ncard).
				append("idcode", idcode).
				append("pasp", pasp).
				append("fio", fio).
				append("idpil", idpil).
				append("pasppil", pasppil).
				append("fiopil", fiopil).
				append("idx", idx).
				append("cdul", cdul).
				append("house", house).
				append("build", build).
				append("apt", apt).
				append("vl", vl).
				append("plzag", plzag).
				append("plopal", plopal).
				append("}").toString();
	}

}
