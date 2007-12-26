package org.flexpay.ab.actions.region;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.actions.FPActionSupport;
import org.apache.log4j.Logger;

import java.util.Map;

public class RegionView extends FPActionSupport implements Preparable {

	public static final String ATTRIBUTE_REGION = RegionView.class.getName() + ".REGION";

	public static Logger log = Logger.getLogger(RegionView.class);

	private Region region = new Region();
	private RegionService regionService;

	/**
	 * This method is called to allow the action to prepare itself.
	 *
	 * @throws Exception thrown if a system level exception occurs.
	 */
	public void prepare() throws Exception {
		log.info("Region: " + region);
		if (region.getId() == null) {
			Map session = ActionContext.getContext().getSession();
			Region reg = (Region) session.remove(ATTRIBUTE_REGION);
			if (reg != null) {
				region = reg;
			}
		}
	}

	public String execute() {
		log.info("Region: " + region);
		if (region.getId() != null) {
			region = regionService.read(region.getId());
			return SUCCESS;
		} else {
			addActionError(getText("error.region_no_id"));
			return ERROR;
		}
	}

	/**
	 * Setter for property 'service'.
	 *
	 * @param regionService Value to set for property 'service'.
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Setter for property 'region'.
	 *
	 * @param region Value to set for property 'region'.
	 */
	public void setRegion(Region region) {
		this.region = region;
	}
}
