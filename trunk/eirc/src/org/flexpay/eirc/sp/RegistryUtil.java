package org.flexpay.eirc.sp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.StringUtil;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegistryUtil {

	/**
	 * Parse registry FIO group
	 *
	 * @param fioStr Group string
	 * @return Last-First-Middle names list
	 * @throws RegistryFormatException if fioStr is invalid
	 */
	@NotNull
	public static List<String> parseFIO(String fioStr) throws RegistryFormatException {

		if (StringUtils.isBlank(fioStr)) {
			return CollectionUtils.list("", "", "");
		}

		List<String> fields = StringUtil.splitEscapable(
				fioStr, Operation.FIO_DELIMITER, Operation.ESCAPE_SYMBOL);
		if (fields.size() != 3) {
			throw new RegistryFormatException(
					String.format("FIO group '%s' has invalid number of fields %d",
							fioStr, fields.size()));
		}

		return fields;
	}
}
