package org.flexpay.payments.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.flexpay.payments.action.outerrequest.request.response.GetDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.Status;

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

        GetDebtInfoResponse response = new GetDebtInfoResponse();
        JsonNode statusCode = jsonNode.findValue("statusCode");
        response.setStatus(Status.getStaus(statusCode.asInt()));
        List<JsonNode> debInfo = jsonNode.findValues("serviceDetails");
        if (debInfo != null && debInfo.size() > 0 && debInfo.get(0).getElements().hasNext()) {
            response.setServiceDetailses(ServiceDetailsUtil.getServiceDetailes(debInfo.get(0).getElements()));
        }
        return response;
    }

}
