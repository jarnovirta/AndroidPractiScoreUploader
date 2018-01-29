package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.util.Log;

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

import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileType;


public class HttpService {
	private static final String TAG = HttpService.class.getSimpleName();

	private static String serverUrl = "http://86.115.27.2:8080/IPSCResultServer";

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void sendMatchDefinitionData(Match match) {
		try {
			Log.d(TAG, "Sending match def");
			String url = serverUrl + "/api/matches";
			String json = objectMapper.writeValueAsString(match);
			sendPostRequest(json, url);
	//		new SendPostRequestTask(url, json).execute();
		}
		catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	public static void sendMatchResultData(MatchScore matchScore) {
		try {
			Log.d(TAG, "Reading match scores");
			String url = serverUrl + "/api/matches/matchId/scores";
			File file = new File(FileService.getPraciScoreExportFilePath());
			String json = objectMapper.writeValueAsString(matchScore);
			sendPostRequest(json, url);
		}
		catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
		}

	}

	private static String sendPostRequest(String json, String serverUrlString) {
		Log.d(TAG, "Sending json: " + json);
		Log.d(TAG, "Sending to url: " + serverUrlString);
		try {
			URL url = new URL(serverUrlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(60000 /* milliseconds */);
			conn.setConnectTimeout(60000/* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));

			writer.write(json);
			Log.i(TAG, "Flushing writer");
			writer.flush();
			writer.close();
			os.close();
			Log.i(TAG, "Connection closed");
			int responseCode = conn.getResponseCode();
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

				return sb.toString();

			} else {
				return new String("false : " + responseCode);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error sending data: " + e.getStackTrace());
			return new String("Exception: " + e.getMessage());
		}
	}



	public static void setServerUrl(String url) {
		serverUrl = url;
	}

	public static String getServerUrl() { return serverUrl; }

}
