package org.flexpay.common.actions.breadcrumbs;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class Crumb implements Serializable {

	private String action;
	private String nameSpace;
	// this is the "*" portion of the mapping myAction_*
	private String wildPortionOfName;
	private Map requestParams;

	public Crumb() {
	}

	public Crumb(String action, String nameSpace, String wildPortionOfName) {
		this.action = action;
		this.nameSpace = nameSpace;
		this.wildPortionOfName = wildPortionOfName;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getWildPortionOfName() {
		return wildPortionOfName;
	}

	public void setWildPortionOfName(String wildPortionOfName) {
		this.wildPortionOfName = wildPortionOfName;
	}

	public String getQualifiedActionName() {
		return nameSpace + "/" + action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map requestParams) {
		this.requestParams = new HashMap(requestParams);
	}

	public String getUrl() {
		StringBuilder url = new StringBuilder(getQualifiedActionName()).append(".action");
		if (requestParams != null) {
			url.append("?");
			for (Object key : requestParams.keySet()) {
				url.append(key).append("=").append(((String[]) requestParams.get(key))[0]).append("&");
			}
		}
		return url.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Crumb) {
			Crumb otherCrumb = (Crumb) obj;
			return getQualifiedActionName().equals(otherCrumb.getQualifiedActionName());
		}

		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("action", action).
				append("nameSpace", nameSpace).
				append("wildPortionOfName", wildPortionOfName).
				append("requestParams", requestParams).
				toString();
	}

}
