package org.flexpay.common.persistence.file;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPModule;

public class FPFileStatus extends DomainObject {

    private Long code;
	private String name;
	private String description;
    private FPModule module;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FPModule getModule() {
        return module;
    }

    public void setModule(FPModule module) {
        this.module = module;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("FPFileStatus {").
				append("id", getId()).
				append("code", code).
				append("name", name).
				append("description", description).
				append("}").toString();
	}

}
