package org.flexpay.sz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.flexpay.sz.dbf.CharacteristicDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.persistence.CharacteristicRecord;

import com.linuxense.javadbf.DBFException;

public class Test {

	private void test() throws FileNotFoundException, DBFException {
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

	public static void main(String[] args) throws Exception {
		Test t = new Test();
		t.test();
	}

}
