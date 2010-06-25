package org.flexpay.common.persistence.registry;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Map;
import java.util.HashMap;


public class RegistryFPFileType extends DomainObject {
    private static final Map<Integer, String> typeToName = new HashMap<Integer, String>();

    public static final int FP_FORMAT = 0;
    public static final int MB_FORMAT = 1;

    static {
        typeToName.put(FP_FORMAT, "common.registry.file.FP_FORMAT");
        typeToName.put(MB_FORMAT, "common.registry.file.MB_FORMAT");

    }

    private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getI18nName() {
		return typeToName.get(code);
	}

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
				.append(code)
				.toHashCode();
    }
}
