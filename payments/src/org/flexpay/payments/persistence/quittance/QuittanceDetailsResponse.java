package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Response for quittance details request from external system
 */
public class QuittanceDetailsResponse extends DetailsResponse implements Serializable {

	private QuittanceInfo[] infos;

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
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("infos", infos).
                toString();
    }
}
