package org.flexpay.httptester.mule;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.flexpay.httptester.mule.request.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MuleClient {

    public static void main(String[] args) throws Exception {

        Properties props = loadProperties();

        if ("true".equals(props.getProperty("request.apartment"))) {
            executeRequest(new ApartmentRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.buildingAddress"))) {
            executeRequest(new BuildingAddressRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.building"))) {
            executeRequest(new BuildingRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.street"))) {
            executeRequest(new StreetRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.streetType"))) {
            executeRequest(new StreetTypeRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.district"))) {
            executeRequest(new DistrictRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.town"))) {
            executeRequest(new TownRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.townType"))) {
            executeRequest(new TownTypeRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.region"))) {
            executeRequest(new RegionRequest(props), new DefaultHttpClient());
        }
        if ("true".equals(props.getProperty("request.country"))) {
            executeRequest(new CountryRequest(props), new DefaultHttpClient());
        }
    }

    private static void executeRequest(MuleRequest request, HttpClient httpClient) throws Exception {

        System.out.println("Executing request...");
        Date startTime = new Date();
        HttpResponse httpResponse = httpClient.execute(request.generateRequest());
        Date finishTime = new Date();
        System.out.println("Execute request elapsed time: " + (finishTime.getTime() - startTime.getTime()) + "ms");
        System.out.println("Response: " + httpResponse.toString());

//        request.parseResponse(httpResponse);
//        request.checkResponseSignature();

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
        FileReader fr = new FileReader("mule_config.properties");
        try {
            props.load(fr);
        } finally {
            fr.close();
        }
		return props;
	}

}
