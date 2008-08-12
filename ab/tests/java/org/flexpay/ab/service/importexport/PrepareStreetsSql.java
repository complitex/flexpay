package org.flexpay.ab.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;

import java.io.*;

public class PrepareStreetsSql extends SpringBeanAwareTestCase {

	public static final String charsetName = "UTF-8";

	@Test
	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	public void dumpTypesCorrection() throws IOException {
		InputStream is = null;
		BufferedReader r = null;
		OutputStream os = null;

		try {
			is = getFileStream("org/flexpay/ab/service/importexport/strtypecorr.csv");
			r = new BufferedReader(new InputStreamReader(is, charsetName));
			os = new FileOutputStream("strtypecorr.sql");

			String sqlTbl = "drop table if exists tmp_street_type_corr;\n" +
							"create table tmp_street_type_corr (\n" +
							"	id bigint,\n" +
							"	name varchar(255),\n" +
							"	type_id bigint,\n" +
							"	cn_type_id bigint\n" +
							");\n";
			os.write(sqlTbl.getBytes(charsetName));

			do {
				String line = r.readLine();
				if (line == null) {
					break;
				}

				String[] parts = line.split(",");
				// remove quote signs
				String id = parts[0].substring(1, parts[0].length() - 1);
				String name = parts[1].substring(1, parts[1].length() - 1);
				String streetTypeId = parts[2].substring(1, parts[2].length() - 1);
				String cnTypeId = parts[3].substring(1, parts[3].length() - 1);

				String sql = String.format("insert into tmp_street_type_corr (id, name, type_id, cn_type_id) " +
										   "values (%s, '%s', %s, %s);\n", id, name, streetTypeId, cnTypeId);
				os.write(sql.getBytes(charsetName));
			} while (true);

		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(r);
			IOUtils.closeQuietly(is);
		}
	}

	@Test
	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	public void dumpStreetsCorrection() throws IOException {

		InputStream is = null;
		BufferedReader r = null;
		OutputStream os = null;
		try {
			is = getFileStream("org/flexpay/ab/service/importexport/strcorr.csv");
			r = new BufferedReader(new InputStreamReader(is, charsetName));
			os = new FileOutputStream("strcorr.sql");

			String sqlTbl = "drop table if exists tmp_street_corr;\n" +
							"create table tmp_street_corr (\n" +
							"	name varchar(255),\n" +
							"	type_corr_id bigint,\n" +
							"	cn_obj_id bigint,\n" +
							"	street_id bigint\n" +
							");\n";
			os.write(sqlTbl.getBytes(charsetName));

			do {
				String line = r.readLine();
				if (line == null) {
					break;
				}

				String[] parts = line.split(",");
				// remove quote signs
				String name = parts[0].substring(1, parts[0].length() - 1);
				String typeCorrId = parts[1].substring(1, parts[1].length() - 1);
				String cnObjId = parts[2].substring(1, parts[2].length() - 1);
				String streetId = parts[3].substring(1, parts[3].length() - 1);

				String sql = String.format("insert into tmp_street_corr (name, type_corr_id, cn_obj_id, street_id) " +
										   "values ('%s', %s, %s, %s);\n", name, typeCorrId, cnObjId, streetId);
				os.write(sql.getBytes(charsetName));
			} while (true);

		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(r);
			IOUtils.closeQuietly(is);
		}
	}
}
