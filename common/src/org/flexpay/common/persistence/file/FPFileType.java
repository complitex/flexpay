package org.flexpay.common.persistence.file;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPModule;

public class FPFileType extends DomainObject {

    private Long code;
    private String fileMask;
	private String name;
	private String description;
    private FPModule module;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getFileMask() {
        return fileMask;
    }

    public void setFileMask(String fileMask) {
        this.fileMask = fileMask;
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
				append("FPFileType {").
				append("id", getId()).
				append("code", code).
				append("fileMask", fileMask).
				append("name", name).
				append("description", description).
				append("}").toString();
	}

}
