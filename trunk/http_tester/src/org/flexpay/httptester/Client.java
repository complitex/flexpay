package org.flexpay.httptester;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.flexpay.httptester.request.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Client {

    public static void main(String[] args) throws Exception {

        Properties props = loadProperties();
        HttpClient httpClient = new DefaultHttpClient();

//        executeRequest(new GetQuittanceDebtInfoRequest(props), httpClient);
//        executeRequest(new GetDebtInfoRequest(props), httpClient);
        executeRequest(new PayDebtRequest(props), httpClient);
//        executeRequest(new ReversalPayRequest(props), httpClient);
//        executeRequest(new RegistryCommentRequest(props), httpClient);
//        executeRequest(new GetRegistryListRequest(props), httpClient);
//        executeRequest(new GetServiceListRequest(props), httpClient);

    }

    private static void executeRequest(Request<?> request, HttpClient httpClient) throws Exception {

        request.parseResponse(httpClient.execute(request.generateRequest()));
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
        FileReader fr = new FileReader("org/flexpay/httptester/config.properties");
        try {
            props.load(fr);
        } finally {
            fr.close();
        }
		return props;
	}

}
