package fi.ipscresultservice.androidpractiscoreuploader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jarno on 27.1.2018.
 */

public class SendPostRequest extends AsyncTask<String, Void, String> {
	private String serverUrl;
	private String json;

	private final String TAG = SendPostRequest.class.getSimpleName();


	public SendPostRequest(String serverUrl, String json) {
		this.serverUrl = serverUrl;
		this.json = json;
	}
	protected void onPreExecute(){}

	@Override
	protected String doInBackground(String... arg0) {

		try {
			URL url = new URL("http://86.115.27.2:8080/IPSCResultServer/api/matches");
			Log.d(TAG, "Sending JSON string: " + json);
//			JSONObject postDataParams = new JSONObject();
//			postDataParams.put("name", "abc");
//			postDataParams.put("email", "abc@gmail.com");

//			Log.i("params", postDataParams.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
//			writer.write(getPostDataString(postDataParams));
			writer.write(json);
			Log.i(TAG, "Flushing writer");
			writer.flush();
			writer.close();
			os.close();
			Log.i(TAG, "Connection closed");
			int responseCode = conn.getResponseCode();
			String responseMessage = conn.getResponseMessage();
			Log.i(TAG, "Handing response");
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				Log.e(TAG, "Response ok");
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
				return new String("false : " + responseMessage);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error sending data: " + e.getMessage());
			return new String("Exception: " + e.getMessage());
		}
	}
	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(UploaderApp.getAppContext(), result,
				Toast.LENGTH_LONG).show();
	}

	public String getPostDataString(JSONObject params) throws Exception {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		Iterator<String> itr = params.keys();
		while(itr.hasNext()){
			String key= itr.next();
			Object value = params.get(key);
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(value.toString(), "UTF-8"));
		}
		return result.toString();
	}
}
