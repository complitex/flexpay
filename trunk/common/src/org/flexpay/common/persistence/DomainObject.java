package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Parent class for all domain objects
 */
public class DomainObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings ({"UnusedDeclaration"})
	protected Integer version;
	protected Long id;

	public DomainObject() {
	}

	public DomainObject(@NotNull Long id) {
		this.id = id;
	}

	@Nullable
	public Long getId() {
		return id;
	}

	public void setId(@Nullable Long id) {
		this.id = id;
	}

	public boolean isNew() {
		return id == null || id <= 0;
	}

	public boolean isNotNew() {
		return !isNew();
	}

	public static Collection<Long> collectionIds(@NotNull Collection<? extends DomainObject> objects) {
		List<Long> result = list();
		for (DomainObject o : objects) {
			result.add(o.getId());
		}

		return result;
	}

	@SuppressWarnings ({"RawUseOfParameterizedType"})
	private static final CollectionUtils.KeyExtractor DOMAIN_OBJECT_ID_EXTRACTOR =
			new CollectionUtils.KeyExtractor() {
				@Override
				public Long key(Object o) {
					return ((DomainObject) o).getId();
				}
			};

	@SuppressWarnings ({"unchecked"})
	public static <T extends DomainObject> CollectionUtils.KeyExtractor<Long, T> idExtractor() {
		return DOMAIN_OBJECT_ID_EXTRACTOR;
	}

	public static <T extends DomainObject> Comparator<T> comparator() {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.getId().compareTo(o2.getId());
			}
		};
	}

	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DomainObject)) {
			return false;
		}

		DomainObject that = (DomainObject) obj;
		Long thisId = getId();
		// do not check this.id and that.id because of Hibernate proxies that return null
		return thisId != null && that.getId() != null && thisId.equals(that.getId());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("version", version)
				.toString();
	}

}
