package org.flexpay.payments.actions.request.data.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.actions.request.data.response.data.QuittanceInfo;

import java.io.Serializable;

/**
 * Response for quittance details request from external system
 */
public class QuittanceDetailsResponse extends SimpleResponse implements Serializable {

	private QuittanceInfo[] infos = {};

	public QuittanceInfo[] getInfos() {
		return infos;
	}

	public void setInfos(QuittanceInfo[] infos) {
		this.infos = infos;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("status", status).
                append("infos", infos).
                toString();
    }
}
