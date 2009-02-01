package org.flexpay.bti.service.importexport;

import java.util.List;
import java.io.InputStream;

public interface BuildingAttributesImporter {

	/**
	 * Read building attributes from a input stream
	 *
	 * @param is InputStream to read attributes from
	 * @return list of parsed attribute data
	 * @throws Exception if failure occurs
	 */
	List<BuildingAttributeData> readAttributes(InputStream is) throws Exception;
}
