package org.flexpay.sz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.flexpay.sz.dbf.LivePropertyDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.dbf.SzDbfWriter;
import org.flexpay.sz.persistence.Characteristic;

import com.linuxense.javadbf.DBFException;

public class Test {

	private void test() throws FileNotFoundException, DBFException {
		LivePropertyDBFInfo dbfInfo = new LivePropertyDBFInfo(new File(
				"c:\\34467793.A02"), "Cp866");

		SzDbfReader<Characteristic, LivePropertyDBFInfo> reader = new SzDbfReader<Characteristic, LivePropertyDBFInfo>(
				dbfInfo, new FileInputStream("c:\\34467793.A02"), "Cp866");

		SzDbfWriter<Characteristic, LivePropertyDBFInfo> writer = new SzDbfWriter<Characteristic, LivePropertyDBFInfo>(
				dbfInfo, new File("c:\\result"), "Cp866", true);
		
		
		Characteristic liveProperty = null;
		while((liveProperty = reader.read()) != null)
		{
			writer.write(liveProperty);
		}
		writer.close();
		
		
		/*Collection<LiveProperty> liveProperties = reader.readAll();
		writer.write(liveProperties);
		writer.close();*/

		System.out.println("done");
	}

	public static void main(String[] args) throws Exception {
		Test t = new Test();
		t.test();
	}

}
