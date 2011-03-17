package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.*;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.action.outerrequest.request.response.Response;
import org.flexpay.payments.action.outerrequest.request.response.Status;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.flexpay.common.util.CollectionUtils.list;

public abstract class Request<R extends Response> implements Serializable {

    protected final static Logger log = LoggerFactory.getLogger(Request.class);

    public static final String CONTENT_ENCODING = "UTF-8";
    public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

    protected String requestId;
    protected String login;
    protected String requestSignatureString;

    protected PaymentsUserPreferences paymentsUserPreferences;
    protected R response;
    protected StringBuilder sResponse;
    protected Locale locale = Locale.ENGLISH;

    protected Request(R response) {
        this.response = response;
    }

    public String buildResponse() throws FlexPayException {
        return buildResponse(null);
    }

    public String buildResponse(Status status) throws FlexPayException {

        Signature responseSignature = createResponseSignature();

        log.debug("Building response: requestId = {}, status = {}, responseStatus = {}", new Object[] {requestId, status, response.getStatus()});

        sResponse = new StringBuilder();
        sResponse.append("<response>").
                append("<").append(response.getTagName()).append(">");

        addFieldToResponse(responseSignature, "requestId", requestId);

        if (status != null) {
            addFieldToResponse(responseSignature, "statusCode", status.getCode());
            addFieldToResponse(responseSignature, "statusMessage", getStatusText(status));
        } else {
            addFieldToResponse(responseSignature, "statusCode", response.getStatus().getCode());
            addFieldToResponse(responseSignature, "statusMessage", getStatusText(response.getStatus()));
            if (Status.SUCCESS.equals(response.getStatus())) {
                addResponseBody(responseSignature);
            }
        }

        sResponse.append("</").append(response.getTagName()).append(">");

        addSignature(responseSignature);
        sResponse.append("</response>");

        log.debug("Response = {}", sResponse.toString());

        return sResponse.toString();
    }

    protected abstract void addResponseBody(Signature signature) throws FlexPayException;

    public boolean authenticate(CertificateService certificateService) throws FlexPayException, UsernameNotFoundException, CertificateNotFoundException, CertificateBlockedException, CertificateExpiredException, InvalidVerifySignatureException {

        UserPreferences userPreference;

        try {
            List<byte[]> fields = list(getBytes(requestId));
            fields.addAll(getFieldsToSign());
            userPreference = certificateService.authenticateUserByCertificate(login, Hex.decodeHex(requestSignatureString.toCharArray()), fields);
            if (userPreference instanceof PaymentsUserPreferences) {
                paymentsUserPreferences = (PaymentsUserPreferences) userPreference;
            } else {
                log.debug("This user preferences not instance of PaymentsUserPreferences. Login = {}", login);
                throw new FlexPayException("This user preferences not instance of PaymentsUserPreferences. Login = " + login);
            }

            log.debug("User preferences = {}", paymentsUserPreferences);

        } catch (UsernameNotFoundException e) {
            log.error("Error getting user preperences for login {}", login);
            throw e;
        } catch (CertificateNotFoundException e) {
            log.error("Error loading certificate from keystore for login {}", login);
            throw e;
        } catch (CertificateBlockedException e) {
            log.error("Certificate for user with login {} is blocked", login);
            throw e;
        } catch (CertificateExpiredException e) {
            log.error("Certificate for user with login {} is expired", login);
            throw e;
        } catch (InvalidVerifySignatureException e) {
            log.error("Error checking request digital signature", e);
            throw e;
        } catch (Throwable t) {
            log.error("Error getting user preperences for login {}", login, t);
            throw new FlexPayException("Error getting user preperences for login " + login, t);
        }

        return true;

    }

    private Signature createRequestSignature() throws NoSuchAlgorithmException {
        return Signature.getInstance(SIGNATURE_ALGORITHM);
    }

    private Signature createResponseSignature() throws FlexPayException {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(getKey());
            return signature;
        } catch (Exception e) {
            log.error("Error initializing signature", e);
            throw new FlexPayException("Error initializing signature");
        }
    }

    public abstract List<byte[]> getFieldsToSign() throws UnsupportedEncodingException;

    public abstract boolean validate();

    protected void updateSignature(Signature signature, String value) throws Exception {
        if (value != null) {
            signature.update(value.getBytes(CONTENT_ENCODING));
        }
    }

    protected byte[] getBytes(@NotNull String value) throws UnsupportedEncodingException {
        return value.getBytes(CONTENT_ENCODING);
    }

    protected void addFieldToResponse(Signature signature, String tagName, Object field) throws FlexPayException {

        if (field == null) {
            return;
        }

        String fieldValue = field.toString();
        sResponse.append("<").append(tagName).append(">").
                append(fieldValue).
                append("</").append(tagName).append(">");

        try {
            updateSignature(signature, fieldValue);
        } catch (Exception e) {
            log.error("Unsupported encoding for value {}", fieldValue);
            throw new FlexPayException("Unsupported encoding for value {}", fieldValue);
        }
    }

    protected void addSignature(Signature signature) throws FlexPayException {

        try {
            sResponse.append("<signature>").append(Hex.encodeHex(signature.sign())).append("</signature>");
        } catch (SignatureException e) {
            log.error("Error signing response", e);
            throw new FlexPayException("Error signing response", e);
        }
    }

    private PrivateKey getKey() throws FlexPayException {
        try {
            KeyStore keyStore = KeyStoreUtil.loadKeyStore();
            if (!keyStore.isKeyEntry(ApplicationConfig.getSelfKeyAlias())) {
                log.error("Unable to load security key for signing response");
                throw new FlexPayException("Unable to load security key for signing response");
            }

            PrivateKey key = (PrivateKey) keyStore.getKey(ApplicationConfig.getSelfKeyAlias(), ApplicationConfig.getSelfKeyPassword().toCharArray());
            if (key == null) {
                log.error("Unable to load security key for signing response (password is bad)");
                throw new FlexPayException("Unable to load security key for signing response (password is bad)");
            }

            return key;

        } catch (Exception e) {
            log.error("Error loading certificate from keystore", e);
            throw new FlexPayException(e.getMessage());
        }
    }

    public abstract void copyResponse(R response);

    protected String getStatusText(Status status) {
        return ResourceBundle.getBundle("org/flexpay/payments/i18n/responseStatuses", locale).getString(status.getTextKey());
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRequestSignatureString() {
        return requestSignatureString;
    }

    public void setRequestSignatureString(String requestSignatureString) {
        this.requestSignatureString = requestSignatureString;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public R getResponse() {
        return response;
    }

    public PaymentsUserPreferences getPaymentsUserPreferences() {
        return paymentsUserPreferences;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("response", response).
                toString();
    }
}
