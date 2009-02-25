package org.flexpay.bti.service.importexport;

import java.io.InputStream;
import java.util.List;

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
