package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
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

	public static void sendMatchDefinitionData(Match match) {
		try {
			Log.d(TAG, "Sending match def");
			String url = serverUrl + matchDefPath;
			String json = objectMapper.writeValueAsString(match);

			HttpResponseHandler handler = new HttpResponseHandler() {
				@Override
				public void process(int responseCode) {
					Log.d(TAG, "Match def data sent, response code: " + responseCode);
				}
			};
			PostExecuteTask postExecute = new PostExecuteTask() {
				@Override
				public void execute(int resultCode) {
					if (resultCode == 200) {
						FileService.uploadMatchResultData();
					}
					else {
						sendResultNotifications("Error (" + resultCode + ") sending data");
					}
				}
			};
			int timeout = 60000;
			new SendPostAsyncTask(url, json, timeout, handler, postExecute).execute();

		}
		catch (Exception e) {
			Log.e(TAG, "Error sending match definition data", e);
		}
	}

	public static void sendMatchResultData(MatchScore matchScore) {
		try {
			Log.d(TAG, "Reading match scores");
			String url = serverUrl + matchResultsPath;
			String json = objectMapper.writeValueAsString(matchScore);

			HttpResponseHandler handler = new HttpResponseHandler() {
				@Override
				public void process(int responseCode) {
					Log.d(TAG, "Match result data sent, response code: " + responseCode);
					if (responseCode == 200) sendResultNotifications("Result data successfully sent!");
				}
			};

			int timeout = 90000;
			new SendPostAsyncTask(url, json, timeout, handler, null).execute();

		}
		catch (Exception e) {
			Log.e(TAG, "Error sending result data", e);
		}
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

	private static void sendResultNotifications(String info) {
		sendResultNotifications(info, Constants.NOTIFICATION_TYPE.LOUD);
	}

	public static void sendResultNotifications(String info, Constants.NOTIFICATION_TYPE notificationType) {

		ResultReceiver resultReceiver = ResultReceiverService.getFileTrackerResultReceiver();
		Bundle bundle = new Bundle();
		bundle.putString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY, info);

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timeString = dateFormat.format(cal.getTime());
		bundle.putString(Constants.RESULT_MESSAGE_TIMESTAMP_KEY, timeString);

		resultReceiver.send(1, bundle);

		NotificationService.sendServiceNotifications(info, notificationType);
	}
}
