package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFException;
import org.flexpay.sz.persistence.SubsidyRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class SubsidyRecordDBFInfo extends DBFInfo<SubsidyRecord> {

        public SubsidyRecordDBFInfo(File originalFile) {
            super(originalFile);
        }

        SubsidyRecord create(Object[] rowData) throws DBFException,
                FileNotFoundException {
            SubsidyRecord subsidyRecord = new SubsidyRecord();
            subsidyRecord.setSurName(((String) rowData[getInd("sur_nam")]).trim());
            subsidyRecord.setFirstName(((String) rowData[getInd("f_nam")]).trim());
            subsidyRecord.setMidName(((String) rowData[getInd("m_nam")]).trim());
            subsidyRecord.setIndx(((String) rowData[getInd("indx")]).trim());
            subsidyRecord.setN_name(((String) rowData[getInd("n_name")]).trim());
            subsidyRecord.setN_code(((String) rowData[getInd("n_code")]).trim());
            subsidyRecord.setStreetType(((String) rowData[getInd("vul_cat")]).trim());
            subsidyRecord.setStreetName(((String) rowData[getInd("vul_name")]).trim());
            subsidyRecord.setExtStreetID(new Double((String)rowData[getInd("vul_code")]));
            subsidyRecord.setHouseNum(((String) rowData[getInd("bld_num")]).trim());
            subsidyRecord.setPartNum(((String) rowData[getInd("corp_num")]).trim());
            subsidyRecord.setApartment(((String) rowData[getInd("flat")]).trim());
            subsidyRecord.setAccount(((String) rowData[getInd("own_num")]).trim());
            subsidyRecord.setApp_num(((String) rowData[getInd("app_num")]).trim());
            subsidyRecord.setBegin((Date) rowData[getInd("dat_beg")]);
            subsidyRecord.setDat_end((Date) rowData[getInd("dat_end")]);
            subsidyRecord.setCm_area((Double)rowData[getInd("cm_area")]);
            subsidyRecord.setTotalSq((Double)rowData[getInd("nm_area")]);
            subsidyRecord.setBlc_area((Double)rowData[getInd("blc_area")]);
            subsidyRecord.setFrog((Double)rowData[getInd("frog")]);
            subsidyRecord.setDebt((Double)rowData[getInd("debt")]);
            subsidyRecord.setLiving((Double)rowData[getInd("numb")]);
            subsidyRecord.setNach((Double)rowData[getInd("p1")]);
            subsidyRecord.setTariff((Double)rowData[getInd("n1")]);
            subsidyRecord.setP2((Double)rowData[getInd("p2")]);
            subsidyRecord.setN2((Double)rowData[getInd("n2")]);
            subsidyRecord.setP3((Double)rowData[getInd("p3")]);
            subsidyRecord.setN3((Double)rowData[getInd("n3")]);
            subsidyRecord.setP4((Double)rowData[getInd("p4")]);
            subsidyRecord.setN4((Double)rowData[getInd("n4")]);
            subsidyRecord.setP5((Double)rowData[getInd("p5")]);
            subsidyRecord.setN5((Double)rowData[getInd("n5")]);
            subsidyRecord.setP6((Double)rowData[getInd("p6")]);
            subsidyRecord.setN6((Double)rowData[getInd("n6")]);
            subsidyRecord.setP7((Double)rowData[getInd("p7")]);
            subsidyRecord.setN7((Double)rowData[getInd("n7")]);
            subsidyRecord.setP8((Double)rowData[getInd("p8")]);
            subsidyRecord.setN8((Double)rowData[getInd("n8")]);
            //subsidyRecord.setOrgsID((Double)rowData[getInd("OrgsID")]);
            //subsidyRecord.setFileID((Double)rowData[getInd("FileID")]);
            //subsidyRecord.setStatus((Double)rowData[getInd("Status")]);

            return subsidyRecord;
        }

        Object[] getRowData(SubsidyRecord subsidyRecord) throws DBFException,
                FileNotFoundException {
            Object[] rowData = new Object[getDBFFields().length];

            rowData[getInd("sur_nam")] = subsidyRecord.getSurName();
            rowData[getInd("f_nam")] = subsidyRecord.getFirstName();          
            rowData[getInd("m_nam")] = subsidyRecord.getMidName();
            rowData[getInd("indx")] = subsidyRecord.getIndx();
            rowData[getInd("n_name")] = subsidyRecord.getN_name();
            rowData[getInd("n_code")]  = subsidyRecord.getN_code();
            rowData[getInd("vul_cat")] = subsidyRecord.getStreetType();
            rowData[getInd("vul_name")] = subsidyRecord.getStreetName();
            rowData[getInd("vul_code")] = "" + subsidyRecord.getExtStreetID().intValue();
            rowData[getInd("bld_num")] = subsidyRecord.getHouseNum();
            rowData[getInd("corp_num")] = subsidyRecord.getPartNum();
            rowData[getInd("flat")] = subsidyRecord.getApartment();
            rowData[getInd("own_num")] = subsidyRecord.getAccount();
            rowData[getInd("app_num")] = subsidyRecord.getApp_num();
            rowData[getInd("dat_beg")] = subsidyRecord.getBegin();
            rowData[getInd("dat_end")] = subsidyRecord.getDat_end();
            rowData[getInd("cm_area")] = subsidyRecord.getCm_area();
            rowData[getInd("nm_area")] = subsidyRecord.getTotalSq();
            rowData[getInd("blc_area")] = subsidyRecord.getBlc_area();
            rowData[getInd("frog")] = subsidyRecord.getFrog();
            rowData[getInd("debt")] = subsidyRecord.getDebt();
            rowData[getInd("numb")] = subsidyRecord.getLiving();
            rowData[getInd("p1")] = subsidyRecord.getNach();
            rowData[getInd("n1")] = subsidyRecord.getTariff();
            rowData[getInd("n2")] = subsidyRecord.getN2();
            rowData[getInd("p2")] = subsidyRecord.getP2();
            rowData[getInd("n3")] = subsidyRecord.getN3();
            rowData[getInd("p3")] = subsidyRecord.getP3();
            rowData[getInd("n4")] = subsidyRecord.getN4();
            rowData[getInd("p4")] = subsidyRecord.getP4();
            rowData[getInd("n5")] = subsidyRecord.getN5();
            rowData[getInd("p5")] = subsidyRecord.getP5();
            rowData[getInd("n6")] = subsidyRecord.getN6();
            rowData[getInd("p6")] = subsidyRecord.getP6();
            rowData[getInd("n7")] = subsidyRecord.getN7();
            rowData[getInd("p7")] = subsidyRecord.getP7();
            rowData[getInd("n8")] = subsidyRecord.getN8();
            rowData[getInd("p8")] = subsidyRecord.getP8();
            //rowData[getInd("OrgsID")] = subsidyRecord.getIndx();
            //rowData[getInd("FileID")] = subsidyRecord.getFileID();
            //rowData[getInd("Status")] = subsidyRecord.getStatus();

            return rowData;
        }

}
