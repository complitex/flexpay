package org.flexpay.tc.actions.tariff;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TariffCalcRulesFileImportAction extends FPActionSupport {

	private Map<Long, String> names = CollectionUtils.treeMap();
	private Map<Long, String> descriptions = CollectionUtils.treeMap();

	@NotNull
	public String doExecute() {
		initNames();
		return INPUT;
	}

	private void initNames() {
		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			descriptions.put(lang.getId(), "");
		}
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return INPUT;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public Map<Long, String> getDescriptions() {
		return descriptions;
	}

}
