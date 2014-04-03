package org.flexpay.payments.action.outerrequest.request.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.flexpay.payments.action.outerrequest.request.response.data.QuittanceInfo;
import org.flexpay.payments.util.DebtInfoResponseDeserializer;
import org.flexpay.payments.util.QuittanceDebtInfoResponseDeserializer;

import java.io.Serializable;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

@JsonDeserialize(using = QuittanceDebtInfoResponseDeserializer.class)
public class GetQuittanceDebtInfoResponse extends SearchResponse implements Serializable {

    public final static String TAG_NAME = "quittanceDebtInfo";

    private List<QuittanceInfo> infos = list();

    @Override
    public String getTagName() {
        return TAG_NAME;
    }

    public List<QuittanceInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<QuittanceInfo> infos) {
        this.infos = infos;
    }

    public void addQuiitanceInfo(QuittanceInfo info) {
        if (infos == null) {
            infos = list();
        }
        infos.add(info);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("status", status).
                append("jmsRequestId", jmsRequestId).
                append("infos", infos).
                toString();
    }

}
