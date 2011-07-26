package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class BuildingHistoryBuilderProxy implements HistoryBuilder<Building> {

	private BuildingHistoryBuilderHolder builderHolder;

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
	@Override
	public Diff diff(@Nullable Building t1, @NotNull Building t2) {
		return builderHolder.getInstance().diff(t1, t2);
	}

	/**
	 * Create diff for deleted object
	 *
	 * @param obj object to build diff for
	 * @return Diff object
	 */
	@NotNull
	@Override
	public Diff deleteDiff(@NotNull Building obj) {
		return builderHolder.getInstance().deleteDiff(obj);
	}

	/**
	 * Apply diff to an object
	 *
	 * @param building	Object to apply diff to
	 * @param diff Diff to apply
	 */
	@Override
	public void patch(@NotNull Building building, @NotNull Diff diff) {
		builderHolder.getInstance().patch(building, diff);
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        builderHolder.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setBuilderHolder(BuildingHistoryBuilderHolder builderHolder) {
		this.builderHolder = builderHolder;
	}

}
