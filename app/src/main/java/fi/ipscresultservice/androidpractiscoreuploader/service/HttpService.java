package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import fi.ipscresultservice.androidpractiscoreuploader.SendPostRequest;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderApp;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileType;


// TODO: HttpClient deprecated, does not work in Samsung.
public class HttpService {
	private static final String TAG = HttpService.class.getSimpleName();
	private static String serverUrl = "http://86.115.27.2:8080/IPSCResultServer/api/matches";

	public static void sendRequest() {
		Log.d(TAG, "Calling SendPostRequest");
		File file = FileService.readPractiScoreExportFile();
		String json = PractiScoreFileParser.readMatchScoreData(file, PractiScoreFileType.MATCH_DEF);

		new SendPostRequest(serverUrl, json).execute();
	}


	public static void setServerUrl(String url) {
		serverUrl = url;
	}

	public static String getServerUrl() { return serverUrl; }

}


//	private class CallAPI extends AsyncTask<String, String, String> {
//
//		public CallAPI(){
//			//set context variables if required
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}

//		@Override
//		protected String doInBackground(String... params) {
//			String urlString = params[0]; // URL to call
//			String data = params[1]; //data to post
//			OutputStream out = null;
//			try {
//				URL url = new URL(urlString);
//				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//				urlConnection.setRequestMethod("POST");
//				urlConnection.setReadTimeout(15000);
//				urlConnection.setConnectTimeout(15000);
//				urlConnection.setDoInput(true);
//				urlConnection.setDoOutput(true);
//
//				out = new BufferedOutputStream(urlConnection.getOutputStream());
//				BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));
//				writer.write(data);
//				writer.flush();
//				writer.close();
//				out.close();
//				urlConnection.connect();
//
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}
//			return null;
//		}
//	}

//	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
//		StringBuilder result = new StringBuilder();
//		boolean first = true;
//		for(Map.Entry<String, String> entry : params.entrySet()){
//			if (first)
//				first = false;
//			else
//				result.append("&");
//
//			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//			result.append("=");
//			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//		}
//
//		return result.toString();
//	}







//	public static void sendMatchScore(String json) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		CloseableHttpClient httpClient = null;
//		try {
//			Log.d(TAG, "Sending match score to server");
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//			Log.d(TAG, "Build client");
////			httpClient = HttpClientBuilder.create().build();
//
//			Log.d(TAG, "Send message");
//			HttpPost request = new HttpPost(serverUrl);

//			StringEntity params = new StringEntity(json);
//			request.addHeader("content-type", "application/json");
//			request.setEntity(params);
//
//			HttpResponse response = httpClient.execute(request);

//			String responseString = new BasicResponseHandler().handleResponse(response);
//			Log.d(TAG, "JSON sent. Response : " + responseString);
//			httpClient.close();
//
//		} catch (Exception ex) {
//			Log.d("HttpService", "Error " + ex.getCause());
//			ex.printStackTrace();
//		}
//	}