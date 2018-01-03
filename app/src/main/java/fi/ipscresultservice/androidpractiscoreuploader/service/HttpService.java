package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.StrictMode;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


// TODO: HttpClient deprecated, does not work in Samsung.
public class HttpService {

	private static String serverUrl = "http://192.168.43.105:8080/IPSCResultServer/api/matches";
	// private static String serverUrl = "";

	public static void sendMatchScore(String json) {
		// ObjectMapper objectMapper = new ObjectMapper();
		CloseableHttpClient httpClient = null;
		try {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			Log.d("HTTPService", "Starting httpclient");
			httpClient = HttpClientBuilder.create().build();
			// String JSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(matchScore);
			HttpPost request = new HttpPost(serverUrl);
			Log.d("HTTPService", "Getting params");
			StringEntity params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			Log.d("HttpService", "Sending JSON");
			HttpResponse response = httpClient.execute(request);
			Log.d("HttpService", "Executed");
			String responseString = new BasicResponseHandler().handleResponse(response);
			Log.d("HttpService", "JSON sent. Response : " + responseString);
			httpClient.close();

		} catch (Exception ex) {
			Log.d("HttpService", "Error " + ex.getCause());
			ex.printStackTrace();

		}
	}
	public static void setServerAddress(String address) {
		serverUrl = address;
	}
}