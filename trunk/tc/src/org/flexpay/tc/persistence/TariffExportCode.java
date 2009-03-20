package org.flexpay.tc.persistence;

import org.flexpay.common.persistence.DomainObject;
import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;

import java.util.Map;

public class TariffExportCode extends DomainObject {

	public static final int EXPORTED = 1;
	public static final int TARIFF_NOT_FOUND_FOR_BUILDING = 0;
	public static final int BUILDING_NOT_FOUND = -1;
	public static final int CANNOT_CREATE_HISTORY_RECORD = -2;
	public static final int NULL_NOT_NULL_TARIFF_VALUE = -3;
	public static final int NEGATIVE_VALUE = -4;
	public static final int BEGIN_DATE_IS_NULL = -5;
	public static final int TARIFF_NOT_FOUND_FOR_BUILDING_WHILE_ADDING_NOT_NULL_TARIFF_VALUE = -6;
	public static final int UNKNOWN_RESULT_CODE = -100;

	private static final Map<Integer, String> typeToName = map(
			ar(     EXPORTED,
                    TARIFF_NOT_FOUND_FOR_BUILDING,
                    BUILDING_NOT_FOUND,
                    CANNOT_CREATE_HISTORY_RECORD,
                    NULL_NOT_NULL_TARIFF_VALUE,
                    NEGATIVE_VALUE,
                    BEGIN_DATE_IS_NULL,
                    UNKNOWN_RESULT_CODE,
                    TARIFF_NOT_FOUND_FOR_BUILDING_WHILE_ADDING_NOT_NULL_TARIFF_VALUE),
			ar(
					"tc.tariff.export_code.exported",
					"tc.tariff.export_code.tariff_not_found_for_building",
                    "tc.tariff.export_code.building_not_found",
					"tc.tariff.export_code.cannot_create_history_record",
					"tc.tariff.export_code.null_not_null_tariff_value",
					"tc.tariff.export_code.negative_value",
					"tc.tariff.export_code.begin_date_is_null",
					"tc.tariff.export_code.unknown_result_code",
                    "tc.tariff.export_code.tariff_not_found_for_building_while_adding_not_null_tariff_value"));

	private int code;

    /**
	 * Constructs a new TariffExportCode.
	 */
	public TariffExportCode() {
	}

	/**
	 * Constructs a new TariffExportCode and set up code.
	 *
	 * @param code code
	 */
	public TariffExportCode(int code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	public String getI18nName() {
		return typeToName.get(code);
	}

	@Override
	public String toString() {
		return "TariffExportCode{" +
			   "code=" + code +
			   '}';
	}
}
