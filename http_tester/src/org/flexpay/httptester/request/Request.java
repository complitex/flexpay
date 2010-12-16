package org.flexpay.httptester.request;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.digester.Digester;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.flexpay.httptester.request.response.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.util.*;

public abstract class Request<R extends Response> {

    public static final String CONTENT_ENCODING = "UTF-8";
    public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

    protected Map<String, String> params = new TreeMap<String, String>();
    protected static final Set<String> paramNames = new TreeSet<String>();

    protected R response;
    protected Signature requestSignature;
    protected Digester parser;
    protected Properties props;

    public Request(Properties props) {
        this.props = props;

        for (String paramName : paramNames) {
            params.put(paramName, props.getProperty(paramName));
        }

    }

    @SuppressWarnings({"unchecked"})
    public R parseResponse(HttpResponse httpResponse) throws Exception {

        System.out.println("\n\nParsing response...");

        InputStream responseContent = httpResponse.getEntity().getContent();
        response = (R) getParser().parse(responseContent);

        System.out.println("Parsed " + response.getClass().getName() + " response:\n" + response);

        return response;
    }

    public HttpPost generateRequest() throws Exception {

        System.out.println("----------------------------------");
        System.out.println("\n\nGenerating " + getClass().getName() + " request...");

        Scanner scanner = new Scanner(getClass().getResourceAsStream("template/" + props.getProperty(getTemlateFilenamePropertyName()))).useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            content = content.replaceFirst("@" + entry.getKey() + "@", entry.getValue());
        }

        content = content.replaceFirst("@" + getAliasPropertyName() + "@", props.getProperty(getAliasPropertyName())).
                          replaceFirst("@" + getSignaturePropertyName() + "@", buildSignature());

        System.out.println("Generated request:\n" + content);

        StringEntity entity = new StringEntity(content, CONTENT_ENCODING);
        HttpPost post = new HttpPost(props.getProperty(getUrlPropertyName()));
        post.setEntity(entity);
        post.setHeader("Accept-Language", props.getProperty(getLocalePropertyName()));

        return post;

    }

    public boolean checkResponseSignature() throws Exception {

        System.out.println("\n\nChecking signature...");

        Certificate certificate = loadCertificate();

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(certificate);

        addParamsToResponseSignature(signature);

        boolean isOk = signature.verify(Hex.decodeHex(response.getSignature().toCharArray()));

        System.out.println("Response signature is " + (isOk ? "OK" : "BAD"));
        System.out.println("----------------------------------");

        return isOk;
    }

    protected abstract void initResponseParser();

    protected abstract void addParamsToSignature() throws Exception;

    protected abstract void addParamsToResponseSignature(Signature signature) throws Exception;

    private String buildSignature() throws Exception {

        requestSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
        requestSignature.initSign(loadPrivateKey());

        addParamsToSignature();

        return getStringFromBytes(requestSignature.sign());
    }

    private PrivateKey loadPrivateKey() throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("key/" + props.getProperty(getPrivateKeyPropertyName())));

        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = (KeyPair) new PEMReader(br).readObject();

        return keyPair.getPrivate();
    }

    private Certificate loadCertificate() throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(getClass().getResourceAsStream("keystore/" + props.getProperty(getKeystorePropertyName())), props.getProperty(getPasswordPropertyName()).toCharArray());

        String certAlias = props.getProperty(getCertificateAliasPropertyName());

        if (!keyStore.isCertificateEntry(certAlias)) {
            System.out.println("Entry is not a certificate");
        }

        return keyStore.getCertificate(certAlias);
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

    protected String getAliasPropertyName() {
        return "login";
    }

    protected String getPasswordPropertyName() {
        return "password";
    }

    protected String getKeystorePropertyName() {
        return "keystore.file";
    }

    protected String getPrivateKeyPropertyName() {
        return "private_key.file";
    }

    protected String getSignaturePropertyName() {
        return "signature";
    }

    protected String getCertificateAliasPropertyName() {
        return "cert_alias";
    }

    public Properties getProps() {
        return props;
    }

    public R getResponse() {
        return response;
    }

    public Digester getParser() {
        if (parser == null) {
            initResponseParser();
        }
        return parser;
    }
}
