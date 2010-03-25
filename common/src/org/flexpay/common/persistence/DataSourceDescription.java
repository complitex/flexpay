package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Set;

/**
 * General description of external organizations, data bases, whatever our system needs to
 * be integrated with
 */
public class DataSourceDescription extends DomainObject {

	private String description;
    private Set<DataCorrection> dataCorrections;

	public DataSourceDescription() {
	}

	public DataSourceDescription(String description) {
		this.description = description;
	}

	public DataSourceDescription(Long id) {
		super(id);
	}

	public DataSourceDescription(Stub<DataSourceDescription> stub) {
		super(stub.getId());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public Set<DataCorrection> getDataCorrections() {
        return dataCorrections;
    }

    public void setDataCorrections(Set<DataCorrection> dataCorrections) {
        this.dataCorrections = dataCorrections;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("description", description).
				toString();
	}

}
