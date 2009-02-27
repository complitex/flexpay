package org.flexpay.common.persistence.history;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

/**
 * History consumer is some abstract destination for shared history records, used to track which records was delivered
 */
public class HistoryConsumer extends DomainObjectWithStatus {

	// unique consumer name
	private String name;
	// optional description
	private String description;
	// last diff packed
	private Diff lastPackedDiff;

	/**
	 * Constructs a new DomainObject.
	 */
	public HistoryConsumer() {
	}

	public HistoryConsumer(@NotNull Long id) {
		super(id);
	}

	public HistoryConsumer(@NotNull Stub<HistoryConsumer> stub) {
		super(stub.getId());
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

	public Diff getLastPackedDiff() {
		return lastPackedDiff;
	}

	public void setLastPackedDiff(Diff lastPackedDiff) {
		this.lastPackedDiff = lastPackedDiff;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("id", getId()).
				append("name", name).
				append("description", description).
				toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof HistoryConsumer && super.equals(obj);
	}
}
