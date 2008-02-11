package org.flexpay.common.persistence;

/**
 * Object which name is time dependent and having upper level parent
 */
public class NameTimeDependentChild<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends NameTimeDependent<T, DI> {

	private DomainObject parent;

	/**
	 * Constructs a new NameTimeDependentChild.
	 */
	public NameTimeDependentChild() {
	}

	public NameTimeDependentChild(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'parent'.
	 *
	 * @return Value for property 'parent'.
	 */
	public DomainObject getParent() {
		return parent;
	}

	/**
	 * Setter for property 'parent'.
	 *
	 * @param parent Value to set for property 'parent'.
	 */
	public void setParent(DomainObject parent) {
		this.parent = parent;
	}
}
