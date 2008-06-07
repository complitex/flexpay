package org.flexpay.eirc.persistence.exchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractChangePersonalAccountOperation extends ContainerOperation {

	protected String oldValue;
	protected String newValue;
	protected Date changeApplyingDate;

	protected AbstractChangePersonalAccountOperation(List<String> datum)
			throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid change personal account operation data");
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
		try {
			changeApplyingDate = simpleDateFormat.parse(datum.get(1));
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
