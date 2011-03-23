package org.flexpay.bti.service.importexport.impl;

import au.com.bytecode.opencsv.CSVReader;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.service.importexport.AttributeDataValidator;
import org.flexpay.bti.service.importexport.AttributeNameMapper;
import org.flexpay.bti.service.importexport.BuildingAttributeData;
import org.flexpay.bti.service.importexport.BuildingAttributesImporter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.IntegerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

public class CSVBuildingAttributesImporter implements BuildingAttributesImporter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private AttributeNameMapper attributeNameMapper;
	private CorrectionsService correctionsService;
	private Stub<DataSourceDescription> sourceDescription;

	private Set<AttributeDataValidator> validators = Collections.emptySet();

	public List<BuildingAttributeData> readAttributes(InputStream is) throws Exception {

		// process log
		Logger plog = ProcessLogger.getLogger(getClass());

		plog.info("Starting importing building attributes");

		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		CSVReader csvReader = new CSVReader(new InputStreamReader(is, "UTF-8"), ';');
		List<BuildingAttributeData> datum = CollectionUtils.list();
		String[] values;

		// fetch properties and dump 'em to datum list
		while ((values = csvReader.readNext()) != null) {

			log.debug("Next row: {}", Arrays.asList(values));

			BuildingAttributeData data = new BuildingAttributeData();
			int indx = 0;
			data.setRowNum(values[indx++]);
			data.setStreetType(values[indx++]);
			data.setStreetName(values[indx++]);
			data.setBuildingNumber(values[indx++]);
			data.setExternalId(values[indx++]);

			findBuilding(data, plog);
			if (!data.isBuildingFound()) {
				continue;
			}

			for (int n = indx; n < values.length; ++n) {
				String attrName = attributeNameMapper.getName(n + 1);
				if (attrName != null) {
					if (log.isDebugEnabled()) {
						log.debug("Found attribute: #{} ({}). {} - {}",
								new Object[] {n + 1, IntegerUtil.toXLSColumnNumber(n+1), attrName, values[n]});
					}
					data.addValue(attrName, values[n]);
				}
			}

			if (!validate(data, plog)) {
				continue;
			}

			datum.add(data);
		}

		plog.info("Ended importing building attributes");
		return datum;
	}

	private void findBuilding(BuildingAttributeData data, Logger plog) {

		Stub<BuildingAddress> addressStub = correctionsService.findCorrection(
				data.getExternalId(), BuildingAddress.class, sourceDescription);
		if (addressStub != null) {
			data.setBuildingAddress(addressStub);
			return;
		}

		plog.warn("Building not found by external id {} in row #{}", data.getExternalId(), data.getRowNum());
		// todo find building by address
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

	@Required
	public void setAttributeNameMapper(AttributeNameMapper attributeNameMapper) {
		this.attributeNameMapper = attributeNameMapper;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setSourceDescription(DataSourceDescription sourceDescription) {
		this.sourceDescription = stub(sourceDescription);
	}

}
