package org.flexpay.sz.actions;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.service.OsznService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ImportFileAction extends FPActionSupport {

	private static final Map<Integer, String> months = treeMap();
	private static final Integer[] years;

	static {
		months.put(0, "01");
		months.put(1, "02");
		months.put(2, "03");
		months.put(3, "04");
		months.put(4, "05");
		months.put(5, "06");
		months.put(6, "07");
		months.put(7, "08");
		months.put(8, "09");
		months.put(9, "10");
		months.put(10, "11");
		months.put(11, "12");

		Calendar cal = Calendar.getInstance();
		cal.setTime(ApplicationConfig.getPastInfinite());
		int yearFrom = cal.get(Calendar.YEAR);
		cal.setTime(ApplicationConfig.getFutureInfinite());
		int yearTill = cal.get(Calendar.YEAR);
		years = new Integer[yearTill - yearFrom + 1];
		for (int i = 0; i <= yearTill - yearFrom; i++) {
			years[i] = yearFrom + i;
		}
	}

	private List<Oszn> osznList;

	private OsznService osznService;

	@NotNull
	public String doExecute() {

		osznList = osznService.getEntities();
		if (osznList.isEmpty()) {
			return "oszn_absent";
		}

		return INPUT;
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
		return osznList.isEmpty() ? "oszn_absent" : INPUT;
	}

	public List<Oszn> getOsznList() {
		return osznList;
	}


	public static Integer[] getYears() {
		return years;
	}

    public static Map<Integer, String> getMonths() {
        return months;
    }

	@Required
    public void setOsznService(OsznService osznService) {
        this.osznService = osznService;
    }

}
