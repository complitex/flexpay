package org.flexpay.bti.service.importexport.impl;

import au.com.bytecode.opencsv.CSVReader;
import org.flexpay.bti.service.importexport.AttributeDataValidator;
import org.flexpay.bti.service.importexport.AttributeNameMapper;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CSVBuildingAttributesImporter {

	private AttributeNameMapper attributeNameMapper;
	private CorrectionsService correctionsService;

	private Set<AttributeDataValidator> validators = Collections.emptySet();

	public List<BuildingAttributeData> readAttributes(InputStream is) throws Exception {

		// process log
		Logger plog = ProcessLogger.getLogger(getClass());

		plog.debug("Starting importing building attributes");

		CSVReader csvReader = new CSVReader(new InputStreamReader(is));
		List<BuildingAttributeData> datum = CollectionUtils.list();
		String[] values;

		// fetch properties and dump 'em to datum list
		while ((values = csvReader.readNext()) != null) {

			BuildingAttributeData data = new BuildingAttributeData();
			int indx = 0;
			data.setRowNum(values[indx++]);
			data.setStreetType(values[indx++]);
			data.setStreetName(values[indx++]);
			data.setBuildingNumber(values[indx++]);
			data.setExternalId(values[indx++]);

			for (int n = indx; n < values.length; ++n) {
				String attrName = attributeNameMapper.getName(n);
				if (attrName != null) {
					data.addValue(attrName, values[n]);
				}
			}

			if (validate(data, plog)) {
				datum.add(data);
			}
		}

		plog.debug("Ended importing building attributes");
		return datum;
	}

	private boolean validate(BuildingAttributeData data, Logger plog) {

		boolean result = true;
		for (AttributeDataValidator validator : validators) {
			try {
				validator.validate(data);
			} catch (FlexPayException e) {
				plog.warn("Row #{}, error", data.getRowNum(), e.getMessage());
				result = false;
			}
		}

		return result;
	}

}
