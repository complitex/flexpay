package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractChangePersonalAccountOperation extends ContainerOperation {

	protected String oldValue;
	protected String newValue;
	protected Date changeApplyingDate;

	protected AbstractChangePersonalAccountOperation(List<String> datum) throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid change personal account operation data");
		}

		try {
			String dateStr = datum.get(1);
			if (StringUtils.isBlank(dateStr)) {
				changeApplyingDate = DateUtil.now();
			} else if (dateStr.length() == "ddMMyyyy".length()) {
				changeApplyingDate = new SimpleDateFormat("ddMMyyyy").parse(dateStr);
			} else if (dateStr.length() == "ddMMyyyyHHmmss".length()) {
				changeApplyingDate = new SimpleDateFormat("ddMMyyyyHHmmss").parse(dateStr);
			} else {
				changeApplyingDate = DateUtil.now();
			}
			if (DateUtil.now().before(changeApplyingDate)) {
				throw new InvalidContainerException("Someone invented time machine? Specified date is in a future: " + datum.get(1));
			}
		} catch (ParseException e) {
			throw new InvalidContainerException("Cannot parse date: " + datum.get(1));
		}

		if (datum.size() >= 3) {
			oldValue = datum.get(2);
		}
		if (datum.size() >= 4) {
			newValue = datum.get(3);
		}
	}
}
