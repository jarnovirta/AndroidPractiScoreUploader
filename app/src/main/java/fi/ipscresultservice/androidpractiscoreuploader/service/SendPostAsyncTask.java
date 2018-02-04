package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * Created by Jarno on 4.2.2018.
 */

public class SendPostAsyncTask extends AsyncTask<String, Void, Boolean> {

	private final String TAG = SendPostAsyncTask.class.getSimpleName();
	private HttpResponseHandler handler;
	private PostExecuteTask postExecuteTask;
	private String urlString;
	private String json;
	private int timeout;
	private int responseCode = -1;


	public SendPostAsyncTask(String urlString, String json, int timeout, HttpResponseHandler handler,
	                         PostExecuteTask postExecuteTask) {
		super();
		this.handler = handler;
		this.urlString = urlString;
		this.json = json;
		this.timeout = timeout;
		this.postExecuteTask = postExecuteTask;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Boolean doInBackground(String... params) {
		Log.d(TAG, "Sending POST request to url: " + urlString);
		try {
			URL url = new URL(urlString);

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
			Log.e(TAG, "Error sending data: ", e);

		}
		handler.process(responseCode);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		super.onPostExecute(aBoolean);
		if (postExecuteTask != null) postExecuteTask.execute(responseCode);
	}
}
