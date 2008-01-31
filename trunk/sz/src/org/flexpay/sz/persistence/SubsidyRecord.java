package org.flexpay.sz.persistence;

import java.util.Date;

public class SubsidyRecord extends Record {
	
	public SubsidyRecord()
	{
		super();
	}
	
    private String surName;//	VARCHAR(30)
    private String firstName;//	VARCHAR(15)
    private String midName;//	VARCHAR(20)
    private String indx;//	VARCHAR(6)
    private String n_name;//	VARCHAR(30)
    private String n_code;//	CHAR(5)
    private String streetType;//	VARCHAR(10)
    private String streetName;//	VARCHAR(30)
    private Double extStreetID;//	INT(5)
    private String houseNum;//	VARCHAR(7)
    private String partNum;//	VARCHAR(2)
    private String appartment;//	VARCHAR(9)
    private String account;//	VARCHAR(15)
    private String app_num;//	CHAR(8)
    private Date begin;//	TIMESTAMP
    private Date dat_end;//	DATE
    private Double cm_area;//	NUMBER(7.2)
    private Double totalSq;//	NUMBER(7.2)
    private Double blc_area;//	NUMBER(5.2)
    private Double frog;//	NUMBER(7.1)
    private Double debt;//	NUMBER(10.2)
    private Double living;//	NUMBER(2)
    private Double nach;//	NUMBER(10.4)
    private Double tarif;//	NUMBER(10.4)
    private Double p2;//	NUMBER(10.4)
    private Double n2;//	NUMBER(10.4)
    private Double p3;//	NUMBER(10.4)
    private Double n3;//	NUMBER(10.4)
    private Double p4;//	NUMBER(10.4)
    private Double n4;//	NUMBER(10.4)
    private Double p5;//	NUMBER(10.4)
    private Double n5;//	NUMBER(10.4)
    private Double p6;//	NUMBER(10.4)
    private Double n6;//	NUMBER(10.4)
    private Double p7;//	NUMBER(10.4)
    private Double n7;//	NUMBER(10.4)
    private Double p8;//	NUMBER(10.4)
    private Double n8;//	NUMBER(10.4)
    private Double orgsID;//	INT(10)
    private Double fileID;//	INT(10)
    private Double status;//	INT(10)

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getIndx() {
        return indx;
    }

    public void setIndx(String indx) {
        this.indx = indx;
    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
    }

    public String getN_code() {
        return n_code;
    }

    public void setN_code(String n_code) {
        this.n_code = n_code;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Double getExtStreetID() {
        return extStreetID;
    }

    public void setExtStreetID(Double extStreetID) {
        this.extStreetID = extStreetID;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getPartNum() {
        return partNum;
    }

    public void setPartNum(String partNum) {
        this.partNum = partNum;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getApp_num() {
        return app_num;
    }

    public void setApp_num(String app_num) {
        this.app_num = app_num;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getDat_end() {
        return dat_end;
    }

    public void setDat_end(Date dat_end) {
        this.dat_end = dat_end;
    }

    public Double getCm_area() {
        return cm_area;
    }

    public void setCm_area(Double cm_area) {
        this.cm_area = cm_area;
    }

    public Double getTotalSq() {
        return totalSq;
    }

    public void setTotalSq(Double totalSq) {
        this.totalSq = totalSq;
    }

    public Double getBlc_area() {
        return blc_area;
    }

    public void setBlc_area(Double blc_area) {
        this.blc_area = blc_area;
    }

    public Double getFrog() {
        return frog;
    }

    public void setFrog(Double frog) {
        this.frog = frog;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Double getLiving() {
        return living;
    }

    public void setLiving(Double living) {
        this.living = living;
    }

    public Double getNach() {
        return nach;
    }

    public void setNach(Double nach) {
        this.nach = nach;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public Double getP2() {
        return p2;
    }

    public void setP2(Double p2) {
        this.p2 = p2;
    }

    public Double getN2() {
        return n2;
    }

    public void setN2(Double n2) {
        this.n2 = n2;
    }

    public Double getP3() {
        return p3;
    }

    public void setP3(Double p3) {
        this.p3 = p3;
    }

    public Double getN3() {
        return n3;
    }

    public void setN3(Double n3) {
        this.n3 = n3;
    }

    public Double getP4() {
        return p4;
    }

    public void setP4(Double p4) {
        this.p4 = p4;
    }

    public Double getN4() {
        return n4;
    }

    public void setN4(Double n4) {
        this.n4 = n4;
    }

    public Double getP5() {
        return p5;
    }

    public void setP5(Double p5) {
        this.p5 = p5;
    }

    public Double getN5() {
        return n5;
    }

    public void setN5(Double n5) {
        this.n5 = n5;
    }

    public Double getP6() {
        return p6;
    }

    public void setP6(Double p6) {
        this.p6 = p6;
    }

    public Double getN6() {
        return n6;
    }

    public void setN6(Double n6) {
        this.n6 = n6;
    }

    public Double getP7() {
        return p7;
    }

    public void setP7(Double p7) {
        this.p7 = p7;
    }

    public Double getN7() {
        return n7;
    }

    public void setN7(Double n7) {
        this.n7 = n7;
    }

    public Double getP8() {
        return p8;
    }

    public void setP8(Double p8) {
        this.p8 = p8;
    }

    public Double getN8() {
        return n8;
    }

    public void setN8(Double n8) {
        this.n8 = n8;
    }

    public Double getOrgsID() {
        return orgsID;
    }

    public void setOrgsID(Double orgsID) {
        this.orgsID = orgsID;
    }

    public Double getFileID() {
        return fileID;
    }

    public void setFileID(Double fileID) {
        this.fileID = fileID;
    }

    public Double getStatus() {
        return status;
    }

    public void setStatus(Double status) {
        this.status = status;
    }

}
