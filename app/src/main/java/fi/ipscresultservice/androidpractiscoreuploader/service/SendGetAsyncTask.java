package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jarno on 29.1.2018.
 */

public class SendGetAsyncTask extends AsyncTask<String, Void, Boolean> {

	private final String TAG = SendGetAsyncTask.class.getSimpleName();
	private HttpResponseHandler handler;
	private String url;
	private int timeout;
	private int responseCode;

	public SendGetAsyncTask(String url, int timeout, HttpResponseHandler handler) {
		super();
		this.handler = handler;
		this.url = url;
		this.timeout = timeout;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Boolean doInBackground(String... params) {
			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");
				con.setConnectTimeout(timeout);
				con.setRequestProperty("Authorization", HttpUtil.getBasicAuthHeader());
				responseCode = con.getResponseCode();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		super.onPostExecute(aBoolean);
		handler.process(responseCode);
	}

}
