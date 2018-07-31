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
	private static final String matchUploadPath =  "/api/matches";
	private static final String testConnectionPath = "/api/testConnection";

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void sendMatchData(Match match, MatchScore matchScore) {
		try {
			String url = serverUrl + matchUploadPath;
			String matchJson = objectMapper.writeValueAsString(match);
			String scoresJson = objectMapper.writeValueAsString(matchScore);
			String json = "{\"match\": " + matchJson + ",";
			json += "\"matchScore\": " + scoresJson + "}";

			Log.d(TAG, "Sending match data json: " + json);

			HttpResponseHandler handler = new HttpResponseHandler() {
				@Override
				public void process(int responseCode, String responseMessage) {
					Log.d(TAG, "Match def data sent, response code: " + responseCode);
				}
			};
			PostExecuteTask postExecute = new PostExecuteTask() {
				@Override
				public void execute(int resultCode, String responseMessage) {
				String notification;
				if (resultCode == 200) notification = "Result data successfully sent!";
				else {
					notification = "Error";
					if (resultCode != -1) notification += " (" + resultCode + ")";
					if (responseMessage != null) notification += ": " + responseMessage;
					notification += "!";
				}
				sendResultNotifications(notification, Constants.NOTIFICATION_TYPE.LOUD);
				}
			};
			int timeout = 50000;
			new SendPostAsyncTask(url, json, timeout, handler, postExecute).execute();
		}
		catch (Exception e) {
			Log.e(TAG, "Software error sending data", e);
			sendResultNotifications("Software error: " + e.getMessage());
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
