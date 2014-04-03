package org.flexpay.payments.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.flexpay.payments.action.outerrequest.request.response.GetQuittanceDebtInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Pavel Sknar
 */
public class QuittanceDebtInfoResponseDeserializer extends JsonDeserializer<GetQuittanceDebtInfoResponse> {
    private static Logger logger = LoggerFactory.getLogger(QuittanceDebtInfoResponseDeserializer.class);

    public QuittanceDebtInfoResponseDeserializer() {

    }

    @Override
    public GetQuittanceDebtInfoResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        logger.info("start deserialize");
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode jsonNode = oc.readTree(jsonParser);
        logger.info("tree: {}", jsonNode.asText());
        Iterator<JsonNode> quittanceInfo = jsonNode.getElements();
        GetQuittanceDebtInfoResponse response = new GetQuittanceDebtInfoResponse();
        if (quittanceInfo != null && quittanceInfo.hasNext()) {
            logger.info("has quittance info");
            response.setInfos(QuittanceInfoUtil.getQuttanceInfos(quittanceInfo));
        }
        return response;
    }

}
