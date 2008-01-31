package org.flexpay.sz.persistence;

public abstract class Record {
	private Long id;
	private SzFile szFile;
	
	public Record(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SzFile getSzFile() {
		return szFile;
	}

	public void setSzFile(SzFile szFile) {
		this.szFile = szFile;
	}

}
