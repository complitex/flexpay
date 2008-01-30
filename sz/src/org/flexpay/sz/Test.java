package org.flexpay.sz;

import java.io.File;
import java.io.FileNotFoundException;

import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.dbf.SubsidyRecordDBFInfo;
import org.flexpay.sz.persistence.CharacteristicRecord;
import org.flexpay.sz.persistence.SubsidyRecord;

import com.linuxense.javadbf.DBFException;

public class Test {

	private void testCharacteristic() throws FileNotFoundException, DBFException {
		CharacteristicDBFInfo dbfInfo = new CharacteristicDBFInfo(new File(
				"c:\\34467793.A02"));

		SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo> reader = new SzDbfReader<CharacteristicRecord, CharacteristicDBFInfo>(
				dbfInfo, new File("c:\\34467793.A02"));

		SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo> writer = new SzDbfWriter<CharacteristicRecord, CharacteristicDBFInfo>(
				dbfInfo, new File("c:\\result"), "Cp866", true);

		CharacteristicRecord liveProperty = null;
		while ((liveProperty = reader.read()) != null) {
			writer.write(liveProperty);
		}
		writer.close();

		/*
		 * Collection<LiveProperty> liveProperties = reader.readAll();
		 * writer.write(liveProperties); writer.close();
		 */

		System.out.println("done");
	}

    private void testSubsidyRecord() throws FileNotFoundException, DBFException {
        SubsidyRecordDBFInfo dbfInfo = new SubsidyRecordDBFInfo(new File(
                "c:\\Z_060807.DBF"));

        SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo> reader = new SzDbfReader<SubsidyRecord, SubsidyRecordDBFInfo>(
                dbfInfo, new File("c:\\Z_060807.DBF"));

        SzDbfWriter<SubsidyRecord, SubsidyRecordDBFInfo> writer = new SzDbfWriter<SubsidyRecord, SubsidyRecordDBFInfo>(
                dbfInfo, new File("c:\\result"), "Cp866", true);

        SubsidyRecord liveProperty = null;
        while ((liveProperty = reader.read()) != null) {
            writer.write(liveProperty);
        }
        writer.close();

        System.out.println("SubsidyRecord done");
    }

	public static void main(String[] args) throws Exception {
		Test t = new Test();
		t.testCharacteristic();
        t.testSubsidyRecord();
    }

}
