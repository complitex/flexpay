package org.flexpay.common.persistence;

/**
 * Object which name is time dependent and having upper level parent
 */
public abstract class NameTimeDependentChild<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends NameTimeDependent<T, DI> {

	private DomainObject parent;

	public NameTimeDependentChild() {
	}

	public NameTimeDependentChild(Long id) {
		super(id);
	}

	public DomainObject getParent() {
		return parent;
	}

	public Stub<DomainObject> getParentStub() {
		return new Stub<DomainObject>(parent);
	}

	public void setParent(DomainObject parent) {
		this.parent = parent;
	}

}
