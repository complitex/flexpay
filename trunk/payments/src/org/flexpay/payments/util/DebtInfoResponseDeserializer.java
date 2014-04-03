package org.flexpay.payments.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.flexpay.payments.action.outerrequest.request.response.GetDebtInfoResponse;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pavel Sknar
 */
public class DebtInfoResponseDeserializer extends JsonDeserializer<GetDebtInfoResponse> {
    @Override
    public GetDebtInfoResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode jsonNode = oc.readTree(jsonParser);
        Iterator<JsonNode> debInfo = jsonNode.getElements();
        GetDebtInfoResponse response = new GetDebtInfoResponse();
        if (debInfo != null && debInfo.hasNext()) {
            response.setServiceDetailses(ServiceDetailsUtil.getServiceDetailes(debInfo));
        }
        return response;
    }

}
