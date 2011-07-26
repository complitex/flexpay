package org.flexpay.httptester.outerrequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.flexpay.httptester.outerrequest.request.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class OuterRequestClient {

    public static void main(String[] args) throws Exception {

        Properties props = loadProperties();
        HttpClient httpClient = new DefaultHttpClient();

        if ("true".equals(props.getProperty("request.getQuittanceDebtInfo"))) {
            executeRequest(new GetQuittanceDebtInfoRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.getDebtInfo"))) {
            executeRequest(new GetDebtInfoRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.payDebt"))) {
            executeRequest(new PayDebtRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.reversalPay"))) {
            executeRequest(new ReversalPayRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.registryComment"))) {
            executeRequest(new RegistryCommentRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.getRegistryList"))) {
            executeRequest(new GetRegistryListRequest(props), httpClient);
        }
        if ("true".equals(props.getProperty("request.getServiceList"))) {
            executeRequest(new GetServiceListRequest(props), httpClient);
        }
    }

    private static void executeRequest(Request<?> request, HttpClient httpClient) throws Exception {

        System.out.println("Executing request...");
        Date startTime = new Date();
        HttpResponse httpResponse = httpClient.execute(request.generateRequest());
        Date finishTime = new Date();
        System.out.println("Execute request elapsed time: " + (finishTime.getTime() - startTime.getTime()) + "ms");

        request.parseResponse(httpResponse);
        request.checkResponseSignature();

/*
            BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.execute(request.generateRequest()).getEntity().getContent()));
            String line;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
*/

    }

	private static Properties loadProperties() throws IOException {
		Properties props = new Properties();
        FileReader fr = new FileReader("outerrequest_config.properties");
        try {
            props.load(fr);
        } finally {
            fr.close();
        }
		return props;
	}

}
