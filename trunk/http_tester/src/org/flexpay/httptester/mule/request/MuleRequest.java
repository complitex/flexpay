package org.flexpay.httptester.mule.request;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.digester3.Digester;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.security.Signature;
import java.util.*;

public abstract class MuleRequest {

    public static final String CONTENT_ENCODING = "UTF-8";

    protected Map<String, String> params = new TreeMap<String, String>();
    protected static final Set<String> paramNames = new TreeSet<String>();

    protected Signature requestSignature;
    protected Digester parser;
    protected Properties props;

    public MuleRequest(Properties props) {
        this.props = props;

        for (String paramName : paramNames) {
            params.put(paramName, props.getProperty(paramName));
        }

    }

    public HttpPost generateRequest() throws Exception {

        System.out.println("----------------------------------");
        System.out.println("\n\nGenerating " + getClass().getName() + " request...");

        Scanner scanner = new Scanner(getClass().getResourceAsStream("template/" + props.getProperty(getTemlateFilenamePropertyName()))).useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("Replacing " + entry.getKey());
            content = content.replaceFirst("@" + entry.getKey() + "@", entry.getValue());
        }

        System.out.println("Generated request:\n" + content);

        StringEntity entity = new StringEntity(content, CONTENT_ENCODING);
        HttpPost post = new HttpPost(props.getProperty(getUrlPropertyName()));
        post.setEntity(entity);
        post.setHeader("Accept-Language", props.getProperty(getLocalePropertyName()));

        return post;

    }

    private String getStringFromBytes(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    protected void updateSignature(Signature signature, String value) throws Exception {
        if (value != null) {
            signature.update(value.getBytes(CONTENT_ENCODING));
        }
    }

    protected void updateRequestSignature(String value) throws Exception {
        updateSignature(requestSignature, value);
    }

    protected abstract String getTemlateFilenamePropertyName();

    protected String getUrlPropertyName() {
        return "url";
    }

    protected String getLocalePropertyName() {
        return "locale";
    }

    public Properties getProps() {
        return props;
    }

/*
    public Digester getParser() {
        if (parser == null) {
            initResponseParser();
        }
        return parser;
    }
*/
}
