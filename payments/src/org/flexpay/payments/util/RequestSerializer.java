package org.flexpay.payments.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.Response;

import java.io.IOException;

/**
 * @author Pavel Sknar
 */
public class RequestSerializer extends JsonSerializer<SearchRequest<? extends Response>> {

    @Override
    public void serialize(SearchRequest<? extends Response> request, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("searchCriteria");
        generator.writeString(request.getSearchCriteria());
        generator.writeFieldName("searchType");
        generator.writeNumber(request.getSearchType());
        generator.writeEndObject();
    }
}
