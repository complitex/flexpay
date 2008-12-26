package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

public abstract class Record extends DomainObject {

	private SzFile szFile;
	
	public SzFile getSzFile() {
		return szFile;
	}

	public void setSzFile(SzFile szFile) {
		this.szFile = szFile;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("Record {").
				append("id", getId()).
				append("}").toString();
	}

}
