package org.flexpay.ab.util;

import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Map;

public class TestNTDUtils {

	public static Map<Long, String> initNames(String value) {
		Map<Long, String> names = treeMap();
		for (Language lang : ApplicationConfig.getLanguages()) {
			names.put(lang.getId(), value);
		}
		return names;
	}

}
