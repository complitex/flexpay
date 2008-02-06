package org.flexpay.sz.persistence;

import org.flexpay.common.persistence.DomainObject;

public abstract class Record extends DomainObject {

	private SzFile szFile;
	
	public Record(){
	}

	public SzFile getSzFile() {
		return szFile;
	}

	public void setSzFile(SzFile szFile) {
		this.szFile = szFile;
	}
}
