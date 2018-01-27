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


public class HttpService {
	private static final String TAG = HttpService.class.getSimpleName();
	private static String serverUrl = "http://86.115.27.2:8080/IPSCResultServer/api/matches";

	public static void sendRequest() {
		serverUrl = "http://86.115.27.2:8080/IPSCResultServer/api/matches";
		Log.d(TAG, "Reading match def");
		File file = FileService.readPractiScoreExportFile();
		String json = PractiScoreFileParser.readMatchScoreData(file, PractiScoreFileType.MATCH_DEF);
		Log.d(TAG, "Sending match def");
		new SendPostRequest(serverUrl, json).execute();

	}

	// TODO: Remove test method:
	public static void sendMatchResultData() {
		Log.d(TAG, "Reading match scores");
		File file = FileService.readPractiScoreExportFile();
		String json = PractiScoreFileParser.readMatchScoreData(file, PractiScoreFileType.MATCH_SCORES);
		String matchScoresUrl = "http://86.115.27.2:8080/IPSCResultServer/api/matches/matchId/scores";
		Log.d(TAG, "Sending match scores");
		new SendPostRequest(matchScoresUrl, json).execute();
	}
	public static void setServerUrl(String url) {
		serverUrl = url;
	}

	public static String getServerUrl() { return serverUrl; }

}
