package org.flexpay.ab.actions.street_type;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;

import com.opensymphony.xwork2.Preparable;

public class StreetTypeEditAction extends CommonAction implements Preparable
{
	private StreetTypeService streetTypeService;
	private Long id;
	private StreetType streetType;
	
	
	public void prepare() throws FlexPayException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		streetType = streetTypeService.read(Long.valueOf(id));
	}
	
	
	public String execute() throws Exception {
		if (isSubmitted()) {
			try {
				streetTypeService.update(streetType, streetType.getTranslations());
			} catch (RuntimeException e) {
				// TODO
			}
			
			return "afterSubmit";
		}
		
        return "form";
	}
	
	/**
	 * Setter for property 'streetTypeService'.
	 * 
	 * @param streetTypeService
	 *            Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StreetType getStreetType() {
		return streetType;
	}

}
