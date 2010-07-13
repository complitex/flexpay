package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;

public class RegistryContainer extends DomainObject {

    /**
     * Symbol used escape special symbols
     */
    public static final char ESCAPE_SYMBOL = '\\';

    /**
     * Symbol used to split fields in containers
     */
    public static final char CONTAINER_DATA_DELIMITER = ':';

    /**
     * Registry commentary (annotation) type
     */
    public static final String COMMENTARY_CONTAINER_TYPE = "1001";
    public static final long COMMENTARY_PAYMENT_NUMBER_LENGTH = 16;
    public static final long COMMENTARY_DATA_LENGTH = 256;
    public static final String COMMENTARY_PAYMENT_DATE_FORMAT = "yyyy/MM/dd";

    public static final long CONTAINER_DATA_MAX_SIZE = 2048;

	private int order;
	private String data;
	private Registry registry;

	public RegistryContainer() {
	}

	public RegistryContainer(String data) {
		this.data = data;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("order", order)
				.append("data", data)
				.toString();
	}
}
