package org.flexpay.httptester;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.digester.Digester;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.flexpay.httptester.data.*;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public class Client {

    public static final int REQUEST_TYPE_SEARCH = 1;
    public static final int REQUEST_TYPE_PAY = 2;
    public static final int REQUEST_TYPE_REFUND = 3;

    private static String generateSearchRequest(Properties props) throws Exception {

        Map<String, String> params = new TreeMap<String, String>();

        params.put("alias", props.getProperty("alias"));
        params.put("requestId", props.getProperty("requestId"));
        params.put("searchType", props.getProperty("searchType"));
        params.put("searchCriteria", props.getProperty("searchCriteria"));

        String content = loadRequestTemplate("request_search.file", props).
                replaceFirst("@requestId@", params.get("requestId")).
                replaceFirst("@searchType@", params.get("searchType")).
                replaceFirst("@searchCriteria@", params.get("searchCriteria")).
                replaceFirst("@alias@", params.get("alias"));

        String signatureString = buildSearchSignature(loadPrivateKey(props, params.get("alias")), params);
        content = content.replaceFirst("@signature@", signatureString);

        return content;

    }

    private static String generatePayRequest(Properties props) throws Exception {

        Map<String, String> params = new TreeMap<String, String>();

        params.put("alias", props.getProperty("alias"));
        params.put("requestId", props.getProperty("requestId"));
        params.put("totalPaySum", props.getProperty("paySum1"));
        params.put("serviceId1", props.getProperty("serviceId1"));
        params.put("serviceProviderAccount1", props.getProperty("serviceProviderAccount1"));
        params.put("paySum1", props.getProperty("paySum1"));

        String content = loadRequestTemplate("request_pay.file", props).
                replaceFirst("@requestId@", params.get("requestId")).
                replaceFirst("@serviceId1@", params.get("serviceId1")).
                replaceFirst("@serviceProviderAccount1@", params.get("serviceProviderAccount1")).
                replaceFirst("@paySum1@", params.get("paySum1")).
                replaceFirst("@totalPaySum@", params.get("totalPaySum")).
                replaceFirst("@alias@", params.get("alias"));

        String signatureString = buildPaySignature(loadPrivateKey(props, params.get("alias")), params);
        content = content.replaceFirst("@signature@", signatureString);

        return content;

    }

    private static String generateRefundRequest(Properties props) throws Exception {

        Map<String, String> params = new TreeMap<String, String>();

        params.put("alias", props.getProperty("alias"));
        params.put("requestId", props.getProperty("requestId"));
        params.put("operationId", props.getProperty("operationId"));
        params.put("totalPaySum", props.getProperty("totalPaySum"));

        String content = loadRequestTemplate("request_refund.file", props).
                replaceFirst("@requestId@", params.get("requestId")).
                replaceFirst("@operationId@", params.get("operationId")).
                replaceFirst("@totalPaySum@", params.get("totalPaySum")).
                replaceFirst("@alias@", params.get("alias"));

        String signatureString = buildRefundSignature(loadPrivateKey(props, params.get("alias")), params);
        content = content.replaceFirst("@signature@", signatureString);

        return content;

    }

    private static void executeRequest(int requestType, Properties props, HttpClient httpClient) throws Exception {

        String content = "";
        if (requestType == REQUEST_TYPE_SEARCH) {
            content = generateSearchRequest(props);
            System.out.println("Search request:\n" + content);
        } else if (requestType == REQUEST_TYPE_PAY) {
            content = generatePayRequest(props);
            System.out.println("Pay request:\n" + content);
        } else if (requestType == REQUEST_TYPE_REFUND) {
            content = generateRefundRequest(props);
            System.out.println("Refund request:\n" + content);
        } else {
            System.out.println("Incorrect request type");
            return;
        }
        HttpPost post = new HttpPost(props.getProperty("url"));
        post.setEntity(new StringEntity(content));
        post.setHeader("Accept-Language", props.getProperty("locale"));

        HttpResponse response = httpClient.execute(post);

        if (requestType == REQUEST_TYPE_SEARCH) {
            Digester digester = initSearchParser();
            SearchResponse parsedResponse = (SearchResponse) digester.parse(response.getEntity().getContent());
            System.out.println("Search response:\n" + parsedResponse);
            System.out.println("Search response signature is " + (checkSignatureSearch(parsedResponse, props) ? "OK" : "BAD"));
        } else if (requestType == REQUEST_TYPE_PAY) {
            Digester digester = initPayParser();
            PayDebtResponse parsedResponse = (PayDebtResponse) digester.parse(response.getEntity().getContent());
            System.out.println("Pay response:\n" + parsedResponse);
            System.out.println("Pay response signature is " + (checkSignaturePay(parsedResponse, props) ? "OK" : "BAD"));
        } else {
            Digester digester = initRefundParser();
            RefundResponse parsedResponse = (RefundResponse) digester.parse(response.getEntity().getContent());
            System.out.println("Refund response:\n" + parsedResponse);
            System.out.println("Refund response signature is " + (checkSignatureRefund(parsedResponse, props) ? "OK" : "BAD"));
        }

/*
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
*/

    }

	public static void main(String[] args) throws Exception {

        Properties props = loadProperties();
        HttpClient httpClient = new DefaultHttpClient();

//        executeRequest(REQUEST_TYPE_SEARCH, props, httpClient);
        executeRequest(REQUEST_TYPE_PAY, props, httpClient);
//        executeRequest(REQUEST_TYPE_REFUND, props, httpClient);

	}

	private static String loadRequestTemplate(String fileNameProperty, Properties props) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(props.getProperty(fileNameProperty))).useDelimiter("\\Z");
		String content = scanner.next();
		scanner.close();
		return content;
	}

    private static String buildRefundSignature(PrivateKey key, Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(key);

        signature.update(params.get("requestId").getBytes());
        signature.update(params.get("operationId").getBytes());
        signature.update(params.get("totalPaySum").getBytes());

        return getStringFromBytes(signature.sign());
    }

	private static String buildPaySignature(PrivateKey key, Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		Signature signature = Signature.getInstance("SHA1withDSA");
		signature.initSign(key);

        signature.update(params.get("requestId").getBytes());
        signature.update(params.get("totalPaySum").getBytes());
        signature.update(params.get("serviceId1").getBytes());
        signature.update(params.get("serviceProviderAccount1").getBytes());
        signature.update(params.get("paySum1").getBytes());

		return getStringFromBytes(signature.sign());
	}

    private static String buildSearchSignature(PrivateKey key, Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(key);

        signature.update(params.get("requestId").getBytes());
        signature.update(params.get("searchType").getBytes());
        signature.update(params.get("searchCriteria").getBytes());

        return getStringFromBytes(signature.sign());
    }

	private static PrivateKey loadPrivateKey(Properties props, String alias) throws Exception {

		KeyStore keyStore = loadKeyStore(props);

		if (!keyStore.isKeyEntry(alias)) {
			System.out.println("Entry is not a private key");
		}

		return (PrivateKey) keyStore.getKey(alias, props.getProperty("password").toCharArray());
	}

	private static KeyStore loadKeyStore(Properties props) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(props.getProperty("keystore")), props.getProperty("password").toCharArray());
		return keyStore;
	}

	private static Certificate loadCertificate(Properties props) throws Exception{

		KeyStore keyStore = loadKeyStore(props);

        String certAlias = props.getProperty("cert_alias");

		if (!keyStore.isCertificateEntry(certAlias)) {
			System.out.println("Entry is not a certificate");
		}

		return keyStore.getCertificate(certAlias);
	}


	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
        FileReader fr = new FileReader("org/flexpay/httptester/config.properties");
        try {
            props.load(fr);
        } finally {
            fr.close();
        }
		return props;
	}

	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    private static void dumpResponseContent(HttpResponse response, Properties props) throws IOException {
		// writing response to console
        InputStream is = response.getEntity().getContent();
        Reader reader = new InputStreamReader(is);
        FileWriter fileWriter = new FileWriter(props.getProperty("response.file"));

        try {
            char[] buf = new char[2048];

            int n = reader.read(buf);
            while (n > 0) {
                fileWriter.write(buf, 0, n);
                n = reader.read(buf);
            }
        } finally {
            reader.close();
            fileWriter.close();
        }
	}

	private static String getStringFromBytes(byte[] bytes) {
		return new String(Hex.encodeHex(bytes));
	}

    private static Digester initPayParser() {

        Digester digester = new Digester();

        digester.addObjectCreate("response", PayDebtResponse.class);
        digester.addBeanPropertySetter("response/signature", "signature");
        digester.addBeanPropertySetter("response/payInfo/requestId", "requestId");
        digester.addBeanPropertySetter("response/payInfo/operationId", "operationId");
        digester.addBeanPropertySetter("response/payInfo/statusCode", "statusCode");
        digester.addBeanPropertySetter("response/payInfo/statusMessage", "statusMessage");

        digester.addObjectCreate("response/payInfo/servicePayInfo", PayDebtResponse.ServicePayInfo.class);

        digester.addSetNext("response/payInfo/servicePayInfo", "addServicePayInfo");
        digester.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceId", "serviceId");
        digester.addBeanPropertySetter("response/payInfo/servicePayInfo/documentId", "documentId");
        digester.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceStatusCode", "statusCode");
        digester.addBeanPropertySetter("response/payInfo/servicePayInfo/serviceStatusMessage", "statusMessage");

        return digester;
    }

    private static Digester initRefundParser() {

        Digester digester = new Digester();

        digester.addObjectCreate("response", RefundResponse.class);
        digester.addBeanPropertySetter("response/signature", "signature");
        digester.addBeanPropertySetter("response/reversalInfo/requestId", "requestId");
        digester.addBeanPropertySetter("response/reversalInfo/statusCode", "statusCode");
        digester.addBeanPropertySetter("response/reversalInfo/statusMessage", "statusMessage");

        return digester;
    }

	private static Digester initSearchParser() {

		Digester digester = new Digester();

		digester.addObjectCreate("response", SearchResponse.class);
        digester.addBeanPropertySetter("response/signature", "signature");

		digester.addObjectCreate("response/quittanceDebtInfo/quittanceInfo", QuittanceInfo.class);
		digester.addObjectCreate("response/quittanceDebtInfo/quittanceInfo/serviceDetails", ServiceDetails.class);
		digester.addObjectCreate("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute", ServiceDetails.ServiceAttribute.class);

		digester.addSetNext("response/quittanceDebtInfo/quittanceInfo", "addQuittanceInfo");
		digester.addSetNext("response/quittanceDebtInfo/quittanceInfo/serviceDetails", "addServiceDetails");
		digester.addSetNext("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute", "addAttribute");

		digester.addBeanPropertySetter("response/quittanceDebtInfo/requestId", "requestId");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/statusCode", "statusCode");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/statusMessage", "statusMessage");

		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/quittanceNumber", "quittanceNumber");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/accountNumber", "accountNumber");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/creationDate", "creationDate");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personFirstName", "personFirstName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personMiddleName", "personMiddleName");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/personLastName", "personLastName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/country", "country");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/region", "region");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/townName", "townName");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/townType", "townType");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/streetName", "streetName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/streetType", "streetType");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/buildingNumber", "buildingNumber");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/buildingBulk", "buildingBulk");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/apartmentNumber", "apartmentNumber");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/totalToPay", "totalToPay");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/totalPayed", "totalPayed");

        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceId", "serviceId");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceName", "serviceName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/incomingBalance", "incomingBalance");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/outgoingBalance", "outgoingBalance");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/amount", "amount");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/expence", "expence");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/rate", "rate");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/recalculation", "recalculation");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/benifit", "benifit");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/subsidy", "subsidy");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/payment", "payment");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/payed", "payed");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceProviderAccount", "serviceProviderAccount");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personFirstName", "firstName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personMiddleName", "middleName");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/personLastName", "lastName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/country", "country");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/region", "region");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/townName", "townName");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/townType", "townType");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/streetName", "streetName");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/streetType", "streetType");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/buildingNumber", "buildingNumber");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/buildingBulk", "buildingBulk");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/apartmentNumber", "apartmentNumber");
        digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/roomNumber", "roomNumber");

		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute/name", "name");
		digester.addBeanPropertySetter("response/quittanceDebtInfo/quittanceInfo/serviceDetails/serviceAttribute/value", "value");

        digester.addObjectCreate("response/debtInfo/serviceDetails", ServiceDetails.class);
        digester.addSetNext("response/debtInfo/serviceDetails", "addServiceDetails");
        digester.addBeanPropertySetter("response/debtInfo/requestId", "requestId");
        digester.addBeanPropertySetter("response/debtInfo/statusCode", "statusCode");
        digester.addBeanPropertySetter("response/debtInfo/statusMessage", "statusMessage");

        digester.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceId", "serviceId");
        digester.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceName", "serviceName");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/incomingBalance", "incomingBalance");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/outgoingBalance", "outgoingBalance");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/amount", "amount");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/expence", "expence");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/rate", "rate");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/recalculation", "recalculation");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/benifit", "benifit");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/subsidy", "subsidy");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/payment", "payment");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/payed", "payed");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/serviceProviderAccount", "serviceProviderAccount");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/personFirstName", "firstName");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/personMiddleName", "middleName");
        digester.addBeanPropertySetter("response/debtInfo/serviceDetails/personLastName", "lastName");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/country", "country");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/region", "region");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/townName", "townName");
        digester.addBeanPropertySetter("response/debtInfo/serviceDetails/townType", "townType");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/streetName", "streetName");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/streetType", "streetType");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/buildingNumber", "buildingNumber");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/buildingBulk", "buildingBulk");
		digester.addBeanPropertySetter("response/debtInfo/serviceDetails/apartmentNumber", "apartmentNumber");
        digester.addBeanPropertySetter("response/debtInfo/serviceDetails/roomNumber", "roomNumber");

		return digester;
	}

    private static boolean checkSignatureRefund(RefundResponse response, Properties properties) throws Exception {

        Certificate certificate = loadCertificate(properties);

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initVerify(certificate);

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        return signature.verify(Hex.decodeHex(response.getSignature().toCharArray()));
    }

    private static boolean checkSignaturePay(PayDebtResponse response, Properties properties) throws Exception {

        Certificate certificate = loadCertificate(properties);

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initVerify(certificate);

        updateSignature(signature, response.getRequestId());
        updateSignature(signature, response.getOperationId());
        updateSignature(signature, response.getStatusCode());
        updateSignature(signature, response.getStatusMessage());

        for (PayDebtResponse.ServicePayInfo servicePayInfo : response.getServicePayInfos()) {

            updateSignature(signature, servicePayInfo.getServiceId());
            updateSignature(signature, servicePayInfo.getDocumentId());
            updateSignature(signature, servicePayInfo.getStatusCode());
            updateSignature(signature, servicePayInfo.getStatusMessage());

        }

        return signature.verify(Hex.decodeHex(response.getSignature().toCharArray()));
    }

	private static boolean checkSignatureSearch(SearchResponse response, Properties properties) throws Exception {

		Certificate certificate = loadCertificate(properties);
		
		Signature signature = Signature.getInstance("SHA1withDSA");
		signature.initVerify(certificate);

		updateSignature(signature, response.getRequestId());
		updateSignature(signature, response.getStatusCode());
		updateSignature(signature, response.getStatusMessage());

		for (QuittanceInfo quittanceInfo : response.getQuittanceInfos()) {

            if (response.getResponseType() == SearchResponse.QUITTANCE_DEBT_RESPONSE) {
                updateSignature(signature, quittanceInfo.getQuittanceNumber());
                updateSignature(signature, quittanceInfo.getAccountNumber());
                updateSignature(signature, quittanceInfo.getCreationDate());
                updateSignature(signature, quittanceInfo.getPersonFirstName());
                updateSignature(signature, quittanceInfo.getPersonMiddleName());
                updateSignature(signature, quittanceInfo.getPersonLastName());
                updateSignature(signature, quittanceInfo.getCountry());
                updateSignature(signature, quittanceInfo.getRegion());
                updateSignature(signature, quittanceInfo.getTownName());
                updateSignature(signature, quittanceInfo.getTownType());
                updateSignature(signature, quittanceInfo.getStreetName());
                updateSignature(signature, quittanceInfo.getStreetType());
                updateSignature(signature, quittanceInfo.getBuildingNumber());
                updateSignature(signature, quittanceInfo.getBuildingBulk());
                updateSignature(signature, quittanceInfo.getApartmentNumber());
                updateSignature(signature, quittanceInfo.getTotalToPay());
                updateSignature(signature, quittanceInfo.getTotalPayed());
            }

			for (ServiceDetails serviceDetails : quittanceInfo.getServiceDetails()) {

                updateSignature(signature, serviceDetails.getServiceId());
                updateSignature(signature, serviceDetails.getServiceName());
				updateSignature(signature, serviceDetails.getIncomingBalance());
				updateSignature(signature, serviceDetails.getOutgoingBalance());
				updateSignature(signature, serviceDetails.getAmount());
                if (response.getResponseType() == SearchResponse.QUITTANCE_DEBT_RESPONSE) {
                    updateSignature(signature, serviceDetails.getExpence());
                    updateSignature(signature, serviceDetails.getRate());
                    updateSignature(signature, serviceDetails.getRecalculation());
                    updateSignature(signature, serviceDetails.getBenifit());
                    updateSignature(signature, serviceDetails.getSubsidy());
                    updateSignature(signature, serviceDetails.getPayment());
                    updateSignature(signature, serviceDetails.getPayed());
                }
				updateSignature(signature, serviceDetails.getServiceProviderAccount());
				updateSignature(signature, serviceDetails.getFirstName());
				updateSignature(signature, serviceDetails.getMiddleName());
                updateSignature(signature, serviceDetails.getLastName());
                if (response.getResponseType() == SearchResponse.QUITTANCE_DEBT_RESPONSE) {
                    updateSignature(signature, serviceDetails.getCountry());
                    updateSignature(signature, serviceDetails.getRegion());
                }
                updateSignature(signature, serviceDetails.getTownName());
                updateSignature(signature, serviceDetails.getTownType());
				updateSignature(signature, serviceDetails.getStreetName());
				updateSignature(signature, serviceDetails.getStreetType());
				updateSignature(signature, serviceDetails.getBuildingNumber());
				updateSignature(signature, serviceDetails.getBuildingBulk());
				updateSignature(signature, serviceDetails.getApartmentNumber());
                updateSignature(signature, serviceDetails.getRoomNumber());

                if (response.getResponseType() == SearchResponse.QUITTANCE_DEBT_RESPONSE) {

                    for (ServiceDetails.ServiceAttribute serviceAttribute : serviceDetails.getAttributes()) {
                        updateSignature(signature, serviceAttribute.getName());
                        updateSignature(signature, serviceAttribute.getValue());
                    }
                }
			}
		}

		return signature.verify(Hex.decodeHex(response.getSignature().toCharArray()));
	}

	private static void updateSignature(Signature signature, String value) throws Exception{
    	if (value != null) {
			signature.update(value.getBytes("utf8"));
		}
	}
}
