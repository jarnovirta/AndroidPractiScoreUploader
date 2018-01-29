package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;
import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;


public class HttpService {
	private static final String TAG = HttpService.class.getSimpleName();

	private static String serverUrl;
	private static final String matchDefPath =  "/api/matches";
	private static final String matchResultsPath =  "/api/matches/matchId/scores";
	private static final String testConnectionPath = "/api/testConnection";

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static int sendMatchDefinitionData(Match match) {
		try {
			Log.d(TAG, "Sending match def");
			String url = serverUrl + matchDefPath;
			String json = objectMapper.writeValueAsString(match);
			return sendPost(json, url, 20000);

		}
		catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
			return -1;
		}
	}

	public static int sendMatchResultData(MatchScore matchScore) {
		try {
			Log.d(TAG, "Reading match scores");
			String url = serverUrl + matchResultsPath;
			String json = objectMapper.writeValueAsString(matchScore);
			return sendPost(json, url, 40000);
		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return -1;
		}
	}


	public static int sendPost(String json, String serverUrlString, int timeout) {
		Log.d(TAG, "Sending POST request to url: " + serverUrlString);
		int responseCode = -1;
		try {
			URL url = new URL(serverUrlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(timeout);
			conn.setConnectTimeout(timeout);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(json);

			writer.flush();
			writer.close();

			os.close();
			responseCode = conn.getResponseCode();
			Log.i(TAG, "Handing response");
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				Log.i(TAG, "Response ok");
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
								conn.getInputStream()));
				StringBuffer sb = new StringBuffer("");
				String line = "";

				while ((line = in.readLine()) != null) {
					sb.append(line);
					break;
				}
				in.close();
			}

		} catch (Exception e) {
			Log.e(TAG, "Error sending data: " + e.getMessage());

		}
		return responseCode;
	}

	public static void testConnection() {
		Toast.makeText(UploaderAppContext.getAppContext(), "Testing connection...",
				Toast.LENGTH_SHORT).show();
		new SendGetAsyncTask(serverUrl + testConnectionPath,
				10000, new TestConnectionResponseHandler()).execute();

	}
	public static void setServerUrl(String url) {
		serverUrl = url;
	}

	public static String getServerUrl() { return serverUrl; }
}
