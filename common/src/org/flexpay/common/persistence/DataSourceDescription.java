package org.flexpay.common.persistence;

/**
 * General description of external organizations, data bases, whatever our system needs to
 * be integrated with
 */
public class DataSourceDescription extends DomainObject {

	private String description;

	/**
	 * Constructs a new DataSourceDescription.
	 */
	public DataSourceDescription() {
	}

	public DataSourceDescription(String description) {
		this.description = description;
	}

	public DataSourceDescription(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'description'.
	 *
	 * @return Value for property 'description'.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for property 'description'.
	 *
	 * @param description Value to set for property 'description'.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
