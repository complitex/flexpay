package org.flexpay.sz.dbf;

import org.flexpay.sz.persistence.SubsidyRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import com.linuxense.javadbf.DBFException;

public class SubsidyRecordDBFInfo extends DBFInfo<SubsidyRecord> {

        public SubsidyRecordDBFInfo(File originalFile) {
            super(originalFile);
        }

        SubsidyRecord create(Object[] rowData) throws DBFException,
                FileNotFoundException {
            SubsidyRecord subsidyRecord = new SubsidyRecord();
            subsidyRecord.setSurName((String) rowData[getInd("SurName")]);
            subsidyRecord.setFirstName((String) rowData[getInd("FirstName")]);
            subsidyRecord.setMidName((String) rowData[getInd("MidName")]);
            subsidyRecord.setIndx((String) rowData[getInd("INDX")]);
            subsidyRecord.setN_name((String) rowData[getInd("N_Name")]);
            subsidyRecord.setN_code((String) rowData[getInd("N_Code")]);
            subsidyRecord.setStreetName((String) rowData[getInd("StreetName")]);
            subsidyRecord.setStreetType((String) rowData[getInd("StreetType")]);
            subsidyRecord.setHouseNum((String) rowData[getInd("HouseNum")]);
            subsidyRecord.setPartNum((String) rowData[getInd("PartNum")]);
            subsidyRecord.setAppartment((String) rowData[getInd("Appartment")]);
            subsidyRecord.setAccount((String) rowData[getInd("Account")]);
            subsidyRecord.setApp_num((String) rowData[getInd("App_num")]);
            subsidyRecord.setBegin((Date) rowData[getInd("Begin")]);
            subsidyRecord.setDat_end((Date) rowData[getInd("dat_end")]);
            subsidyRecord.setCm_area((Double)rowData[getInd("cm_area")]);
            subsidyRecord.setTotalSq((Double)rowData[getInd("TotalSq")]);
            subsidyRecord.setBlc_area((Double)rowData[getInd("Blc_area")]);
            subsidyRecord.setFrog((Double)rowData[getInd("frog")]);
            subsidyRecord.setDebt((Double)rowData[getInd("debt")]);
            subsidyRecord.setLiving((Double)rowData[getInd("Living")]);
            subsidyRecord.setNach((Double)rowData[getInd("Nach")]);
            subsidyRecord.setTarif((Double)rowData[getInd("Tarif")]);
            subsidyRecord.setN1((Double)rowData[getInd("N1")]);
            subsidyRecord.setP1((Double)rowData[getInd("P1")]);
            subsidyRecord.setN2((Double)rowData[getInd("N2")]);
            subsidyRecord.setP2((Double)rowData[getInd("P2")]);
            subsidyRecord.setN3((Double)rowData[getInd("N2")]);
            subsidyRecord.setP3((Double)rowData[getInd("P3")]);
            subsidyRecord.setN4((Double)rowData[getInd("N4")]);
            subsidyRecord.setP4((Double)rowData[getInd("P4")]);
            subsidyRecord.setN5((Double)rowData[getInd("N5")]);
            subsidyRecord.setP5((Double)rowData[getInd("P5")]);
            subsidyRecord.setN6((Double)rowData[getInd("N6")]);
            subsidyRecord.setP6((Double)rowData[getInd("P6")]);
            subsidyRecord.setN7((Double)rowData[getInd("N7")]);
            subsidyRecord.setP7((Double)rowData[getInd("P7")]);
            subsidyRecord.setN8((Double)rowData[getInd("N8")]);
            subsidyRecord.setP8((Double)rowData[getInd("P8")]);
            subsidyRecord.setOrgsID((Double)rowData[getInd("OrgsID")]);
            subsidyRecord.setFileID((Double)rowData[getInd("FileID")]);
            subsidyRecord.setStatus((Double)rowData[getInd("Status")]);

            return subsidyRecord;
        }

        Object[] getRowData(SubsidyRecord subsidyRecord) throws DBFException,
                FileNotFoundException {
            Object[] rowData = new Object[getDBFFields().length];

            rowData[getInd("SurName")] = subsidyRecord.getSurName();
            rowData[getInd("FirstName")] = subsidyRecord.getFirstName();          
            rowData[getInd("MidName")] = subsidyRecord.getMidName();
            rowData[getInd("INDX")] = subsidyRecord.getIndx();
            rowData[getInd("N_Name")] = subsidyRecord.getN_name();
            rowData[getInd("N_Code")]  = subsidyRecord.getN_code();
            rowData[getInd("StreetName")] = subsidyRecord.getStreetName();
            rowData[getInd("StreetType")] = subsidyRecord.getStreetType();
            rowData[getInd("HouseNum")] = subsidyRecord.getHouseNum();
            rowData[getInd("PartNum")] = subsidyRecord.getPartNum();
            rowData[getInd("Appartment")] = subsidyRecord.getAppartment();
            rowData[getInd("Account")] = subsidyRecord.getAccount();
            rowData[getInd("App_num")] = subsidyRecord.getApp_num();
            rowData[getInd("Begin")] = subsidyRecord.getBegin();
            rowData[getInd("dat_end")] = subsidyRecord.getDat_end();
            rowData[getInd("cm_area")] = subsidyRecord.getCm_area();
            rowData[getInd("TotalSq")] = subsidyRecord.getTotalSq();
            rowData[getInd("Blc_area")] = subsidyRecord.getBlc_area();
            rowData[getInd("frog")] = subsidyRecord.getFrog();
            rowData[getInd("debt")] = subsidyRecord.getDebt();
            rowData[getInd("Living")] = subsidyRecord.getLiving();
            rowData[getInd("Nach")] = subsidyRecord.getNach();
            rowData[getInd("Tarif")] = subsidyRecord.getTarif();
            rowData[getInd("N1")] = subsidyRecord.getN1();
            rowData[getInd("P1")] = subsidyRecord.getP1();
            rowData[getInd("N2")] = subsidyRecord.getN2();
            rowData[getInd("P2")] = subsidyRecord.getP2();
            rowData[getInd("N3")] = subsidyRecord.getN3();
            rowData[getInd("P3")] = subsidyRecord.getP3();
            rowData[getInd("N4")] = subsidyRecord.getN4();
            rowData[getInd("P4")] = subsidyRecord.getP4();
            rowData[getInd("N5")] = subsidyRecord.getN5();
            rowData[getInd("P5")] = subsidyRecord.getP5();
            rowData[getInd("N6")] = subsidyRecord.getN6();
            rowData[getInd("P6")] = subsidyRecord.getP6();
            rowData[getInd("N7")] = subsidyRecord.getN7();
            rowData[getInd("P7")] = subsidyRecord.getP7();
            rowData[getInd("N8")] = subsidyRecord.getN8();
            rowData[getInd("P8")] = subsidyRecord.getP8();
            rowData[getInd("OrgsID")] = subsidyRecord.getIndx();
            rowData[getInd("FileID")] = subsidyRecord.getFileID();
            rowData[getInd("Status")] = subsidyRecord.getStatus();

            return rowData;
        }

}
