package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;

/**
 * Created by Jarno on 27.1.2018.
 */

public class SendPostRequestTask extends AsyncTask<String, Void, String> {
	private String serverUrl;
	private String json;

	private final String TAG = SendPostRequestTask.class.getSimpleName();


	public SendPostRequestTask(String serverUrl, String json) {
		this.serverUrl = serverUrl;
		this.json = json;
	}
	protected void onPreExecute(){}

	@Override
	protected String doInBackground(String... arg0) {

		try {
			URL url = new URL(serverUrl);
			Log.d(TAG, "Sending to URL" + serverUrl);

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
	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(UploaderAppContext.getAppContext(), result,
				Toast.LENGTH_LONG).show();
	}
}
